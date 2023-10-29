package org.bot0ff.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bot0ff.component.button.InlineButton;
import org.bot0ff.entity.User;
import org.bot0ff.service.RegistrationService;
import org.bot0ff.service.UserService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import static org.bot0ff.entity.UserState.ONLINE;
import static org.bot0ff.service.ServiceCommands.*;

//обрабатывает запросы статуса WAIT_FOR_REGISTRATION
@Log4j
@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    private final UserService userService;

    //ответы на текстовые запросы
    @Override
    public User processRegistrationText(User user, SendMessage sendMessage, String cmd) {
        if(START.equals(cmd)) {
            sendMessage.setText("""
                    Добро пожаловать в "Морской Бой"!
                    Введите имя или оставьте как есть...""");
            sendMessage.setReplyMarkup(InlineButton.registrationButton());
        }
        else if(HELP.equals(cmd)) {
            sendMessage.setText("Помощь");
        }
        else if(CANCEL.equals(cmd)) {
            sendMessage.setText("Введите имя или оставьте как есть...");
            sendMessage.setReplyMarkup(InlineButton.registrationButton());
        }
        else if(cmd.length() > 10 | cmd.length() < 3){
            sendMessage.setText("""
                    Имя должно быть не больше 10 символов и не меньше 3. 
                    Введите имя или оставьте как есть...""");
            sendMessage.setReplyMarkup(InlineButton.registrationButton());
        }
        else {
            user.setName(cmd);
            user.setState(ONLINE);
            userService.saveUser(user);
            sendMessage.setText("Выберите действие, " + user.getName());
            sendMessage.setReplyMarkup(InlineButton.changeOptions());
        }
        user.setSendMessage(sendMessage);
        return user;
    }

    //ответы на inline запросы
    @Override
    public User processRegistrationInline(User user, EditMessageText editMessageText, String cmd) {
        if(cmd.equals("/newUserWithCurrentName")) {
            user.setName(user.getName());
            user.setState(ONLINE);
            editMessageText.setText("Выберите действие, " + user.getName());
            editMessageText.setReplyMarkup(InlineButton.changeOptions());
        }
        else {
            editMessageText.setText("Введите имя или оставьте как есть...");
            editMessageText.setReplyMarkup(InlineButton.registrationButton());
        }
        user.setEditMessageText(editMessageText);
        return user;
    }
}
