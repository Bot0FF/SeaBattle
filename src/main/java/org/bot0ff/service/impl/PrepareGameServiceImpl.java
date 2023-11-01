package org.bot0ff.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bot0ff.component.button.InlineButton;
import org.bot0ff.entity.User;
import org.bot0ff.service.PrepareGameService;
import org.bot0ff.service.UserService;
import org.bot0ff.service.game.AutoPrepareService;
import org.bot0ff.service.game.GameMessageService;
import org.bot0ff.service.game.ManuallyPrepareService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import static org.bot0ff.entity.UserState.*;
import static org.bot0ff.service.ServiceCommands.*;

//обрабатывает запросы статуса CHANGE_GAME_FILED
@Log4j
@Service
@RequiredArgsConstructor
public class PrepareGameServiceImpl implements PrepareGameService {
    private final UserService userService;
    private final ManuallyPrepareService manuallyPrepareService;
    private final AutoPrepareService autoPrepareService;
    private final GameMessageService gameMessageService;

    //ответы на текстовые запросы
    @Override
    public User optionsPrepareGameText(User user, SendMessage sendMessage, String cmd) {
        if(START.equals(cmd)) {
            sendMessage.setText("Идет расстановка кораблей...");
            sendMessage.setReplyMarkup(InlineButton.setManuallyPrepareShip(user));
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
            sendMessage.setText("Идет расстановка кораблей...");
            sendMessage.setReplyMarkup(InlineButton.setManuallyPrepareShip(user));
        }
        user.setSendMessage(sendMessage);
        return user;
    }

    //ответы на inline запросы
    @Override
    public User optionsPrepareGameInline(User user, EditMessageText editMessageText, String cmd) {
        if(cmd.matches("[0-9]:[0-9]")) {
            int result;
            String[] split = cmd.split(":");
            result = manuallyPrepareService.setUserFiled(user, cmd);
            int currentCoordinate = ManuallyPrepareService.prepareManuallyMap.get(user.getId())[Integer.parseInt(split[0])][Integer.parseInt(split[1])];
            if(result == 6 | currentCoordinate == 6) {
                return null;
            }
            else  {
                editMessageText.setText("Идет расстановка кораблей...");
                editMessageText.setReplyMarkup(InlineButton.setManuallyPrepareShip(user));
            }
        }
        else if(cmd.equals("/autoPrepareGameFiled")) {
            autoPrepareService.setAutoUserGameFiled(user);
            editMessageText.setText("Автоматическая расстановка...");
            editMessageText.setReplyMarkup(InlineButton.setManuallyPrepareShip(user));
        }
        else if(cmd.equals("/searchGameVsUser")) {
            if(manuallyPrepareService.checkPreparedShips(ManuallyPrepareService.prepareManuallyMap.get(user.getId()))) {
                user.setState(SEARCH_GAME);
                editMessageText.setText("Поиск противника...");
            }
            else {
                editMessageText.setText("Неверно расставлены корабли...");
                editMessageText.setReplyMarkup(InlineButton.setManuallyPrepareShip(user));
            }
        }
        else if(cmd.equals("/searchGameVsAi")) {
            if(manuallyPrepareService.checkPreparedShips(ManuallyPrepareService.prepareManuallyMap.get(user.getId()))) {
                //set userAi
                var aiGameFiled = autoPrepareService.setAutoAiGameFiled();
                user.setAiGameFiled(aiGameFiled);
                //set User
                user.setState(IN_GAME);
                user.setActive(true);
                editMessageText.setText(gameMessageService.getCurrentGameFiled("Началась игра против ИИ\n Первый ход ваш...", user));
                editMessageText.setReplyMarkup(InlineButton.charGameBoard());
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
        user.setEditMessageText(editMessageText);
        return user;
    }
}
