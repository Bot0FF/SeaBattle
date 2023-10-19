package org.bot0ff.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bot0ff.component.TelegramBot;
import org.bot0ff.component.button.InlineButton;
import org.bot0ff.dto.ResponseDto;
import org.bot0ff.entity.User;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.bot0ff.entity.UserState.*;
import static org.bot0ff.service.ServiceCommands.*;

@Log4j
@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService{
    private final UserService userService;

    @Override
    public void processTextMessage(TelegramBot telegramBot, Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        ResponseDto response;

        var user = userService.findOrSaveUser(update);
        var userState = user.getState();
        var inputMessage = update.getMessage().getText();

        if(WAIT_REGISTRATION.equals(userState)) {
            response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .sendMessage(processRegistration(user, inputMessage, sendMessage))
                    .build();
        }
        else if(ONLINE.equals(userState)) {
            response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .sendMessage(processServiceCommand(user, inputMessage, sendMessage))
                    .build();
        }
        else if(WAIT_FOR_GAME.equals(userState)) {
            response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .sendMessage(processSearchGame(user, inputMessage, sendMessage))
                    .build();
        }
        else if(PREPARE_GAME.equals(userState)) {
            response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .sendMessage(processPrepareGame(user, inputMessage, sendMessage))
                    .build();
        }
        else if(IN_GAME.equals(userState)) {
            response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .sendMessage(processGame(user, inputMessage, sendMessage))
                    .build();
        }
        else {
            log.error("Unknown user state: " + userState);
            sendMessage.setText("Неизвестная ошибка! Введите /cancel и попробуйте снова!");
            response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .sendMessage(sendMessage)
                    .build();
        }

        telegramBot.sendAnswer(response);
    }

    @Override
    public void processCallbackQuery(TelegramBot telegramBot, Update update) {
        var user = userService.findOrSaveUser(update);
        var userState = user.getState();
        var inputMessage = update.getCallbackQuery().getData();
        var sendMessage = new SendMessage();
        sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
        var answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(update.getCallbackQuery().getId());

        if(WAIT_REGISTRATION.equals(userState)) {
            var response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .sendMessage(processRegistrationAuto(user, sendMessage))
                    .answerCallbackQuery(answerCallbackQuery)
                    .build();
            telegramBot.sendAnswer(response);
        }

    }

    //отмена активной игры, возврат к базовому состоянию
    private SendMessage cancelProcess(User user, SendMessage sendMessage) {
        user.setState(ONLINE);
        userService.saveUser(user);
        sendMessage.setText("Игра отменена, Вы потерпели поражение");
        return sendMessage;
    }

    //ожидание выбора имени
    private SendMessage processRegistration(User user, String cmd, SendMessage sendMessage) {
        if(START.equals(cmd)) {
            sendMessage.setText("""
                    Добро пожаловать в игру "Морской Бой"!
                    Это классическая игра, где можно играть с реальными противниками или с ИИ.
                    Для начала введите желаемое имя и отправьте его или нажмите кнопку "Оставить как есть\"""");
            sendMessage.setReplyMarkup(InlineButton.registrationButton());
        }
        else if(HELP.equals(cmd)) {
            sendMessage.setText("Внимательно прочтите правила игры");
        }
        else if(CANCEL.equals(cmd)) {
            sendMessage.setText("Для начала введите желаемое имя и отправьте его или нажмите кнопку \"Оставить как есть\"");
            sendMessage.setReplyMarkup(InlineButton.registrationButton());
        }
        else if(cmd.length() > 10 | cmd.length() < 3){
            sendMessage.setText("Имя должно быть не больше 10 символов и не меньше 3. " +
                    "Для продолжения введите желаемое имя и отправьте его или нажмите кнопку \"Оставить как есть\"");
            sendMessage.setReplyMarkup(InlineButton.registrationButton());
        }
        else {
            user.setName(cmd);
            user.setState(ONLINE);
            userService.saveUser(user);
            sendMessage.setText("Для начала игры выберите \"Поиск игры\"");
        }
        return sendMessage;
    }

    //выбор имени по умолчанию при регистрации
    private SendMessage processRegistrationAuto(User user, SendMessage sendMessage) {
        user.setName(user.getName());
        user.setState(ONLINE);
        userService.saveUser(user);
        sendMessage.setText("Для начала игры выберите \"Поиск игры\"");
        return sendMessage;
    }

    //ожидание ввода сервисных команд
    private SendMessage processServiceCommand(User user, String cmd, SendMessage sendMessage) {
        sendMessage.setText("Введите команду из меню");
        return sendMessage;
    }

    //процесс поиска игры
    private SendMessage processSearchGame(User user, String cmd, SendMessage sendMessage) {
        sendMessage.setText("Поиск игры");
        return sendMessage;
    }

    //процесс подготовки игрового поля
    private SendMessage processPrepareGame(User user, String cmd, SendMessage sendMessage) {
        sendMessage.setText("Процесс подготовки игрового поля");
        return sendMessage;
    }

    //процесс игры
    private SendMessage processGame(User user, String cmd, SendMessage sendMessage) {
        sendMessage.setText("В игре");
        return sendMessage;
    }
}
