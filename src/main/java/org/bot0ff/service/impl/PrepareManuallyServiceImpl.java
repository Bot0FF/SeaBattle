package org.bot0ff.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bot0ff.component.button.InlineButton;
import org.bot0ff.entity.User;
import org.bot0ff.entity.UserFiled;
import org.bot0ff.service.PrepareManuallyService;
import org.bot0ff.service.UserService;
import org.bot0ff.service.game.GameMessageService;
import org.bot0ff.service.game.ManuallyPrepareService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import static org.bot0ff.entity.UserState.*;
import static org.bot0ff.service.ServiceCommands.*;
import static org.bot0ff.util.Constants.GAME_FILED_LENGTH;

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
            ManuallyPrepareService.prepareManuallyMap.put(user.getId(), UserFiled.builder()
                    .userFiled(new int[GAME_FILED_LENGTH][GAME_FILED_LENGTH])
                    .build());
            editMessageText.setText("Ручная расстановка...");
            editMessageText.setReplyMarkup(InlineButton.setManuallyPrepareShip(user));
        }
        else if(cmd.matches("[0-9]:[0-9]")) {
            int result;
            String[] split = cmd.split(":");
            result = manuallyPrepareService.setUserFiled(user, cmd);
            int currentCoordinate = ManuallyPrepareService.prepareManuallyMap.get(user.getId()).getUserFiled()[Integer.parseInt(split[0])][Integer.parseInt(split[1])];
            if(result == 6 | currentCoordinate == 6) {
                return null;
            }
            else  {
                editMessageText.setText("Идет расстановка кораблей...(test)");
                editMessageText.setReplyMarkup(InlineButton.setManuallyPrepareShip(user));
            }
        }
        else if(cmd.equals("confirmPrepare")) {
            if(manuallyPrepareService.countShips(user)) {
                user.setState(SEARCH_GAME);
                editMessageText.setText("Поиск противника...");
            }
            else {
                editMessageText.setText("Неверно расставлены корабли...");
                editMessageText.setReplyMarkup(InlineButton.setManuallyPrepareShip(user));
            }
        }
        else {
            editMessageText.setText("Идет расстановка кораблей...");
            //TODO сделать продолжение текущей расстановки
        }
        editMessageText.setMessageId(user.getMessageId());
        editMessageText.setReplyMarkup(InlineButton.setManuallyPrepareShip(user));
        return editMessageText;
    }

}
