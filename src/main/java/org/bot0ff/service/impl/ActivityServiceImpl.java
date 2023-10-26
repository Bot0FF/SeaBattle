package org.bot0ff.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bot0ff.component.button.InlineButton;
import org.bot0ff.entity.User;
import org.bot0ff.service.ActivityService;
import org.bot0ff.service.UserService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.bot0ff.entity.UserState.*;
import static org.bot0ff.service.ServiceCommands.*;

//обрабатывает запросы статуса ONLINE
@Log4j
@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {
    private final UserService userService;

    //ответы на текстовые запросы
    @Override
    public SendMessage changeOptionsFromMenu(User user, SendMessage sendMessage, String cmd) {
        if(START.equals(cmd)) {
            sendMessage.setText("Выберите действие, " + user.getName());
            sendMessage.setReplyMarkup(InlineButton.changeOptions());
        }
        else if(CANCEL.equals(cmd)) {
            sendMessage.setText("Текущих сражений нет...");
            sendMessage.setReplyMarkup(InlineButton.changeOptions());
        }
        else if(HELP.equals(cmd)) {
            sendMessage.setText("Помощь");
        }
        else {
            sendMessage.setText("Выберите действие, " + user.getName());
            sendMessage.setReplyMarkup(InlineButton.changeOptions());
        }
        return sendMessage;
    }

    //ответы на inline запросы
    @Override
    public SendMessage changeOptions(User user, SendMessage sendMessage, String cmd) {
        if(cmd.equals("newGame")) {
            user.setState(CHANGE_GAME_FILED);
            userService.saveUser(user);
            sendMessage.setText("Расстановка кораблей...");
            sendMessage.setReplyMarkup(InlineButton.changePlacementOption());
        }
        else {
            sendMessage.setText("Выберите действие, " + user.getName());
            sendMessage.setReplyMarkup(InlineButton.changeOptions());
        }
        return sendMessage;
    }
}
