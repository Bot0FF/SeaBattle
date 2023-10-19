package org.bot0ff.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bot0ff.component.TelegramBot;
import org.bot0ff.controller.UpdateController;
import org.bot0ff.entity.User;
import org.springframework.stereotype.Service;
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
        var user = userService.findOrSaveUser(update);
        var userState = user.getState();
        var text = update.getMessage().getText();
        var answer = "";

        //выход из игры, автоматическое поражение, переход в статус ACTIVE
        if(CANCEL.equals(text)) {
            answer = cancelProcess(user);
        }
        else if(WAIT_REGISTRATION.equals(userState)) {
            answer = processRegistration(user, text);
        }
        else if(ONLINE.equals(userState)) {
            answer = processServiceCommand(user);
        }
        else if(WAIT_FOR_GAME.equals(userState)) {
            answer = processSearchGame(user);
        }
        else if(PREPARE_GAME.equals(userState)) {
            answer = processPrepareGame(user);
        }
        else if(IN_GAME.equals(userState)) {
            answer = processGame(user);
        }
        else {
            log.error("Unknown user state: " + userState);
            answer = "Неизвестная ошибка! Введите /cancel и попробуйте снова!";
        }

        var chatId = update.getMessage().getChatId();
        sendAnswer(telegramBot, answer, chatId);
    }

    @Override
    public void processCallbackQuery(TelegramBot telegramBot, Update update) {
        var user = userService.findOrSaveUser(update);
        var userState = user.getState();
        var text = update.getCallbackQuery().getData();
        var answer = "";

        var chatId = update.getCallbackQuery().getId();
        sendAnswer(telegramBot, answer, Long.valueOf(chatId));
    }


    private void sendAnswer(TelegramBot telegramBot, String answer, Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(answer);
        telegramBot.sendTextMessage(sendMessage);
    }

    //отмена активной игры, возврат к базовому состоянию
    private String cancelProcess(User user) {
        return "Добро пожаловать";
    }

    //ожидание выбора имени
    private String processRegistration(User user, String cmd) {
        if(START.equals(cmd)) {
            return "Выберете имя или оставьте как есть";
        }
        else if(HELP.equals(cmd)) {
            return "Внимательно прочтите правила игры";
        }
        else if(CANCEL.equals(cmd)) {
            return "Для продолжения игры необходимо выбрать имя или оставить как есть";
        }
        else if(cmd.length() > 10 | cmd.length() < 3){
            return "Имя должно быть не больше 10 символов и не меньше 3";
        }
        else {
            user.setName(cmd);
            user.setState(ONLINE);
            userService.saveUser(user);
            return "Для начала игры выберете \"Поиск игры\"";
        }
    }

    //ожидание ввода сервисных команд
    private String processServiceCommand(User user) {
        return "Введите команду из меню";
    }

    //процесс поиска игры
    private String processSearchGame(User user) {
        return "Поиск игры";
    }

    //процесс подготовки игрового поля
    private String processPrepareGame(User user) {
        return "Процесс подготовки игрового поля";
    }

    //процесс игры
    private String processGame(User user) {
        return "В игре";
    }
}
