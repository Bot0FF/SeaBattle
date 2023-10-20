package org.bot0ff.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bot0ff.component.button.InlineButton;
import org.bot0ff.entity.User;
import org.bot0ff.service.PrepareManuallyService;
import org.bot0ff.service.UserService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.bot0ff.entity.UserState.ONLINE;
import static org.bot0ff.service.ServiceCommands.*;

//обрабатывает запросы статуса PREPARE_MANUALLY
@Log4j
@Service
@RequiredArgsConstructor
public class PrepareManuallyServiceImpl implements PrepareManuallyService {
    private final UserService userService;

    //ответы на текстовые запросы
    @Override
    public SendMessage optionsPrepareManuallyText(User user, SendMessage sendMessage, String cmd) {
        if(START.equals(cmd)) {
            sendMessage.setText("Идет расстановка кораблей");
            //TODO сделать продолжение текущей расстановки
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
            sendMessage.setText("Идет расстановка кораблей");
            //TODO сделать продолжение текущей расстановки
        }
        return sendMessage;
    }

    //ответы на inline запросы
    @Override
    public SendMessage prepareGameManuallyInline(User user, SendMessage sendMessage, String cmd) {
        if(cmd.equals("/startManuallyPrepare")) {
            //TODO продолжение ручной расстановки кораблей
        }
        else {
            sendMessage.setText("Идет расстановка кораблей");
            //TODO сделать продолжение текущей расстановки
        }
        return sendMessage;
    }
}
