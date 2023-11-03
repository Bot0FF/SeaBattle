package org.bot0ff.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bot0ff.component.button.InlineButton;
import org.bot0ff.controller.JoinUserController;
import org.bot0ff.entity.User;
import org.bot0ff.service.PrepareGameService;
import org.bot0ff.service.game.*;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import static org.bot0ff.entity.UserState.*;
import static org.bot0ff.service.ServiceCommands.*;

//обрабатывает запросы статуса CHANGE_GAME_FILED
@Log4j
@Service
@RequiredArgsConstructor
public class PrepareGameServiceImpl implements PrepareGameService {
    private final ManuallyPrepareService manuallyPrepareService;
    private final AutoPrepareService autoPrepareService;
    private final JoinUserController joinUserController;
    private final GameFiledService gameFiledService;
    private final GameMessageService gameMessageService;

    //ответы на текстовые запросы
    @Override
    public User optionsPrepareGameText(User user, SendMessage sendMessage, String cmd) {
        if(START.equals(cmd)) {
            sendMessage.setText("Подготовка игрового поля...");
            sendMessage.setReplyMarkup(InlineButton.setManuallyPrepareShip(user));
            user.setSendMessage(sendMessage);
        }
        else if(CANCEL.equals(cmd)) {
            user.setState(ONLINE);
            sendMessage.setText("Сброс настроек игры...\nВыберите действие, " + user.getName());
            sendMessage.setReplyMarkup(InlineButton.changeOptions());
            user.setSendMessage(sendMessage);
        }
        return user;
    }

    //ответы на inline запросы
    @Override
    public User optionsPrepareGameInline(User user, EditMessageText editMessageText, AnswerCallbackQuery answerCallbackQuery, String cmd) {
        if(cmd.matches("[0-9]:[0-9]")) {
            int result;
            String[] split = cmd.split(":");
            result = manuallyPrepareService.setUserGameFiled(user, cmd);
            int currentCoordinate = GameFiledService.prepareUserFiledMap.get(user.getId())[Integer.parseInt(split[0])][Integer.parseInt(split[1])];
            if(result != 6 | currentCoordinate != 6) {
                editMessageText.setText("Ручная расстановка...");
                editMessageText.setReplyMarkup(InlineButton.setManuallyPrepareShip(user));
                user.setEditMessageText(editMessageText);
            }
        }
        else if(cmd.equals("/autoPrepareGameFiled")) {
            autoPrepareService.setAutoUserGameFiled(user);
            editMessageText.setText("Автоматическая расстановка...");
            editMessageText.setReplyMarkup(InlineButton.setManuallyPrepareShip(user));
            user.setEditMessageText(editMessageText);
        }
        else if(cmd.equals("/searchGameVsUser")) {
            if(gameFiledService.checkPreparedShips(GameFiledService.prepareUserFiledMap.get(user.getId()))) {
                var userGameFiled = GameFiledService.prepareUserFiledMap.get(user.getId());
                user.setUserGameFiled(gameFiledService.convertArrFiledToList(userGameFiled));
                user.setState(SEARCH_GAME);
                JoinUserController.joinUserMap.put(user.getId(), user);
                editMessageText.setText("Поиск противника...");
                user.setEditMessageText(editMessageText);
            }
            else {
                answerCallbackQuery.setText("Неверно расставлены корабли...");
            }
        }
        else if(cmd.equals("/searchGameVsAi")) {
            if(gameFiledService.checkPreparedShips(GameFiledService.prepareUserFiledMap.get(user.getId()))) {
                //set userAi
                var aiGameFiled = autoPrepareService.setAutoAiGameFiled();
                user.setOpponentGameFiled(gameFiledService.convertArrFiledToList(aiGameFiled));
                //set User
                var userGameFiled = GameFiledService.prepareUserFiledMap.get(user.getId());
                user.setUserGameFiled(gameFiledService.convertArrFiledToList(userGameFiled));
                GameFiledService.prepareUserFiledMap.remove(user.getId());
                user.setState(IN_GAME);
                user.setActive(true);
                editMessageText.setText(gameMessageService
                        .getCurrentGameFiled("Ваш ход...", gameFiledService.convertListFiledToArr(user.getUserGameFiled())));
                editMessageText.setReplyMarkup(InlineButton.gameBoard(user.getOpponentGameFiled()));
                user.setEditMessageText(editMessageText);
            }
            else {
                answerCallbackQuery.setText("Неверно расставлены корабли...");
            }
        }
        user.setAnswerCallbackQuery(answerCallbackQuery);
        return user;
    }
}
