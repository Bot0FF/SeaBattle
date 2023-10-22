package org.bot0ff.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bot0ff.component.button.InlineButton;
import org.bot0ff.entity.User;
import org.bot0ff.service.PrepareAutomaticService;
import org.bot0ff.service.UserService;
import org.bot0ff.service.prepare.AutomaticPrepareFiled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.bot0ff.entity.UserState.ONLINE;
import static org.bot0ff.entity.UserState.SEARCH_GAME;
import static org.bot0ff.service.ServiceCommands.*;

//обрабатывает запросы статуса PREPARE_AUTOMATIC
@Log4j
@Service
@RequiredArgsConstructor
public class PrepareAutomaticServiceImpl implements PrepareAutomaticService {
    private final UserService userService;
    private final AutomaticPrepareFiled automaticPrepareFiled;

    //ответы на текстовые запросы
    @Override
    public SendMessage optionsPrepareAutomaticText(User user, SendMessage sendMessage, String cmd) {
        if(START.equals(cmd)) {
            sendMessage.setText("Выбрана автоматическая расстановка кораблей");
            sendMessage.setReplyMarkup(InlineButton.startAutomaticPrepare());
        }
        else if(CANCEL.equals(cmd)) {
            user.setState(ONLINE);
            userService.saveUser(user);
            sendMessage.setText("""
                    Параметры сброшены.
                    Выберите что хотите сделать""");
            sendMessage.setReplyMarkup(InlineButton.changeOptions());
        }
        else if(HELP.equals(cmd)) {
            sendMessage.setText("Помощь");
        }
        else {
            sendMessage.setText("Выбрана автоматическая расстановка кораблей");
            sendMessage.setReplyMarkup(InlineButton.startAutomaticPrepare());
        }
        return sendMessage;
    }

    //ответы на inline запросы
    @Override
    public SendMessage prepareGameAutomaticInline(User user, SendMessage sendMessage, String cmd) {
        if(cmd.equals("/confirmAutomaticPrepare")) {
            user.setState(SEARCH_GAME);
            userService.saveUser(user);
            sendMessage.setText("Идет поиск противника");
        }
        else if(cmd.equals("/startAutomaticPrepare")) {
            var currentGameFiled = automaticPrepareFiled.getAutomaticGameFiled(user);
            sendMessage.setText("Продолжить с текущей расстановкой?\n" + "currentGameFiled");
            sendMessage.setReplyMarkup(InlineButton.confirmAutomaticPrepare());
        }
        else if(cmd.equals("/updateAutomaticPrepare")) {
            var currentGameFiled = automaticPrepareFiled.getAutomaticGameFiled(user);
            sendMessage.setText("Продолжить с текущей расстановкой?\n" + "currentGameFiled");
            sendMessage.setReplyMarkup(InlineButton.confirmAutomaticPrepare());
        }
        else {
            sendMessage.setText("Выбрана автоматическая расстановка кораблей");
            sendMessage.setReplyMarkup(InlineButton.startAutomaticPrepare());
        }
        return sendMessage;
    }
}
