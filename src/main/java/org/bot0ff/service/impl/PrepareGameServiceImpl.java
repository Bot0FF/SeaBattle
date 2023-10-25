package org.bot0ff.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bot0ff.component.button.InlineButton;
import org.bot0ff.entity.User;
import org.bot0ff.service.PrepareGameService;
import org.bot0ff.service.UserService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.bot0ff.entity.UserState.*;
import static org.bot0ff.service.ServiceCommands.*;

//обрабатывает запросы статуса PREPARE_GAME
@Log4j
@Service
@RequiredArgsConstructor
public class PrepareGameServiceImpl implements PrepareGameService {
    private final UserService userService;

    //ответы на текстовые запросы
    @Override
    public SendMessage optionsPrepareGameText(User user, SendMessage sendMessage, String cmd) {
        if(START.equals(cmd)) {
            sendMessage.setText("Расстановка кораблей...");
            sendMessage.setReplyMarkup(InlineButton.changePlacementOption());
        }
        else if(CANCEL.equals(cmd)) {
            user.setState(ONLINE);
            userService.saveUser(user);
            sendMessage.setText("Сброс настроек игры...\nВыберите действие, " + user.getName());
            sendMessage.setReplyMarkup(InlineButton.changeOptions());
        }
        else if(HELP.equals(cmd)) {
            sendMessage.setText("Помощь");
        }
        else {
            sendMessage.setText("Расстановка кораблей...");
            sendMessage.setReplyMarkup(InlineButton.changePlacementOption());
        }
        return sendMessage;
    }

    //ответы на inline запросы
    @Override
    public SendMessage optionsPrepareGameInline(User user, SendMessage sendMessage, String cmd) {
        if(cmd.equals("prepareManually")) {
            user.setState(PREPARE_MANUALLY);
            userService.saveUser(user);
            sendMessage.setText("Ручная расстановка кораблей...");
            sendMessage.setReplyMarkup(InlineButton.startManuallyPrepare());
        }
        else if(cmd.equals("prepareAutomatic")) {
            user.setState(PREPARE_AUTOMATIC);
            userService.saveUser(user);
            sendMessage.setText("Автоматическая расстановка кораблей...");
            sendMessage.setReplyMarkup(InlineButton.startAutomaticPrepare());
        }
        else {
            sendMessage.setText("Расстановка кораблей...");
            sendMessage.setReplyMarkup(InlineButton.changePlacementOption());
        }
        return sendMessage;
    }
}
