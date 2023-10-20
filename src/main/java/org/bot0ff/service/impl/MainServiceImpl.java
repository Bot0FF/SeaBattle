package org.bot0ff.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bot0ff.component.TelegramBot;
import org.bot0ff.component.button.InlineButton;
import org.bot0ff.dto.ResponseDto;
import org.bot0ff.entity.User;
import org.bot0ff.service.*;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.bot0ff.entity.UserState.*;

@Log4j
@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {
    private final UserService userService;
    private final RegistrationService registrationService;
    private final SearchGameService searchGameService;
    private final PrepareGameService prepareGameService;
    private final GameService gameService;

    @Override
    public void processTextMessage(TelegramBot telegramBot, Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());

        var user = userService.findOrSaveUser(update);
        var userState = user.getState();
        var inputMessage = update.getMessage().getText();

        if(WAIT_REGISTRATION.equals(userState)) {
            var answer = registrationService.processRegistration(user, inputMessage, sendMessage);
            var response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .sendMessage(answer)
                    .build();
            telegramBot.sendAnswer(response);
        }
        else if(ONLINE.equals(userState)) {
            var answer = processServiceCommand(user, inputMessage, sendMessage);
            var response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .sendMessage(answer)
                    .build();
            telegramBot.sendAnswer(response);
        }
        else if(SEARCH_GAME.equals(userState)) {
            var answer = getInfo("Идет подготовка сражения...", sendMessage);
            var response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .sendMessage(answer)
                    .build();
            telegramBot.sendAnswer(response);
        }
        else if(PREPARE_GAME.equals(userState)) {
            var answer = getInfo("Идет расстановка кораблей...", sendMessage);
                    var response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .sendMessage(answer)
                    .build();
            telegramBot.sendAnswer(response);
        }
        else if(IN_GAME.equals(userState)) {
            var answer = getInfo("Идет сражение...", sendMessage);
            var response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .sendMessage(answer)
                    .build();
            telegramBot.sendAnswer(response);
        }
        else {
            log.error("Unknown user state: " + userState);
            var answer = getInfo("Неизвестная ошибка! Введите /cancel и попробуйте снова!", sendMessage);
            var response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .sendMessage(answer)
                    .build();
            telegramBot.sendAnswer(response);
        }
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
            var answer = registrationService.processRegistrationAuto(user, sendMessage);
            var response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .sendMessage(answer)
                    .answerCallbackQuery(answerCallbackQuery)
                    .build();
            telegramBot.sendAnswer(response);
        }
        else if(ONLINE.equals(userState)) {
            var answer = searchGameService.searchGame(user, sendMessage, inputMessage);
            var response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .sendMessage(answer)
                    .answerCallbackQuery(answerCallbackQuery)
                    .build();
            telegramBot.sendAnswer(response);
        }
        else if(SEARCH_GAME.equals(userState)) {
            var answer = getInfo("Идет подготовка сражения...", sendMessage);
            var response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .sendMessage(sendMessage)
                    .answerCallbackQuery(answerCallbackQuery)
                    .build();
            telegramBot.sendAnswer(response);
        }
    }

    //ожидание ввода сервисных команд
    private SendMessage processServiceCommand(User user, String cmd, SendMessage sendMessage) {
        sendMessage.setText("Введите команду из меню");
        return sendMessage;
    }

    private SendMessage getInfo(String msg, SendMessage sendMessage) {
        sendMessage.setText(msg);
        return sendMessage;
    }
}
