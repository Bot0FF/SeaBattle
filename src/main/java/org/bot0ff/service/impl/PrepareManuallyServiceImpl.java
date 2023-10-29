package org.bot0ff.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bot0ff.component.button.InlineButton;
import org.bot0ff.entity.User;
import org.bot0ff.service.PrepareManuallyService;
import org.bot0ff.service.UserService;
import org.bot0ff.service.game.GameMessageService;
import org.bot0ff.service.game.ManuallyPrepareService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import static org.bot0ff.entity.UserState.ONLINE;
import static org.bot0ff.entity.UserState.PREPARE_MANUALLY;
import static org.bot0ff.service.ServiceCommands.*;

//обрабатывает запросы статуса PREPARE_MANUALLY
@Log4j
@Service
@RequiredArgsConstructor
public class PrepareManuallyServiceImpl implements PrepareManuallyService {
    private final UserService userService;
    private final ManuallyPrepareService manuallyPrepareService;
    private final GameMessageService gameMessageService;

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
    public EditMessageText prepareGameManuallyInline(User user, EditMessageText editMessageText, String cmd) {
        if(cmd.equals("startManuallyPrepare")) {
            user.setState(PREPARE_MANUALLY);
            userService.saveUser(user);
            editMessageText.setText("Ручная расстановка...");
            editMessageText.setReplyMarkup(InlineButton.setManuallyPrepareShip(user));
        }

        else {
            editMessageText.setText("Идет расстановка кораблей");
            //TODO сделать продолжение текущей расстановки
        }
        editMessageText.setMessageId(user.getMessageId());
        editMessageText.setReplyMarkup(InlineButton.setManuallyPrepareShip(user));
        return editMessageText;
    }

}
