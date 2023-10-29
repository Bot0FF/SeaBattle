package org.bot0ff.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bot0ff.component.button.InlineButton;
import org.bot0ff.entity.User;
import org.bot0ff.service.ChangeGameFiledService;
import org.bot0ff.service.UserService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import static org.bot0ff.entity.UserState.*;
import static org.bot0ff.service.ServiceCommands.*;

//обрабатывает запросы статуса CHANGE_GAME_FILED
@Log4j
@Service
@RequiredArgsConstructor
public class ChangeGameFiledServiceImpl implements ChangeGameFiledService {
    private final UserService userService;

    //ответы на текстовые запросы
    @Override
    public User optionsPrepareGameText(User user, SendMessage sendMessage, String cmd) {
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
        user.setSendMessage(sendMessage);
        return user;
    }

    //ответы на inline запросы
    @Override
    public User optionsPrepareGameInline(User user, EditMessageText editMessageText, String cmd) {
        if(cmd.equals("prepareManually")) {
            user.setState(PREPARE_MANUALLY);
            userService.saveUser(user);
            editMessageText.setText("Ручная расстановка кораблей...");
            editMessageText.setReplyMarkup(InlineButton.startManuallyPrepare());
        }
        else if(cmd.equals("prepareAutomatic")) {
            user.setState(PREPARE_AUTOMATIC);
            userService.saveUser(user);
            editMessageText.setText("Автоматическая расстановка кораблей...");
            editMessageText.setReplyMarkup(InlineButton.startAutomaticPrepare());
        }
        else {
            editMessageText.setText("Расстановка кораблей...");
            editMessageText.setReplyMarkup(InlineButton.changePlacementOption());
        }
        user.setEditMessageText(editMessageText);
        return user;
    }
}
