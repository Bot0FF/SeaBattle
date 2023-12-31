package org.bot0ff.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bot0ff.component.button.InlineButton;
import org.bot0ff.controller.EndGameController;
import org.bot0ff.controller.UserUserController;
import org.bot0ff.entity.User;
import org.bot0ff.service.InGameService;
import org.bot0ff.service.UserService;
import org.bot0ff.controller.UserAiController;
import org.bot0ff.service.game.GameFiledService;
import org.bot0ff.service.game.GameService;
import org.bot0ff.service.game.GameMessageService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;

import static org.bot0ff.entity.UserState.ONLINE;
import static org.bot0ff.service.ServiceCommands.*;

//обрабатывает запросы статуса IN_GAME
@Log4j
@Service
@RequiredArgsConstructor
public class InGameServiceImpl implements InGameService {
    private final UserService userService;
    private final GameService gameService;
    private final GameMessageService gameMessageService;
    private final GameFiledService gameFiledService;
    private final UserAiController userAiController;
    private final UserUserController userUserController;
    private final EndGameController endGameController;

    //ответы на текстовые запросы
    @Override
    public User processTextMessage(Update update, User user, SendMessage sendMessage, String cmd) {
        if(START.equals(cmd)) {
            if(user.isActive()) {
                sendMessage.setText(gameMessageService
                        .getCurrentGameFiled("Ваш ход...", gameFiledService.convertListFiledToArr(user.getUserGameFiled())));
            }
            else {
                sendMessage.setText(gameMessageService
                        .getCurrentGameFiled("Ход противника...", gameFiledService.convertListFiledToArr(user.getUserGameFiled())));
            }
            sendMessage.setReplyMarkup(InlineButton.gameBoard(user.getOpponentGameFiled()));
            user.setSendMessage(sendMessage);
        }
        else if(CANCEL.equals(cmd)) {
            endGameController.endGameOpponent(true, user.getOpponentId());
            user.setState(ONLINE);
            user.setActive(false);
            user.setOpponentId(0L);
            user.setUserGameFiled(new ArrayList<>());
            user.setOpponentGameFiled(new ArrayList<>());
            user.setCountLoss(user.getCountLoss() + 1);
            sendMessage.setText("Поражение...\nВыберите действие, " + user.getName());
            sendMessage.setReplyMarkup(InlineButton.changeOptions());
            user.setSendMessage(sendMessage);
        }
        return user;
    }

    //ответы на inline запросы
    @Override
    public User processCallbackQuery(Update update, User user, EditMessageText editMessageText, AnswerCallbackQuery answerCallbackQuery, String cmd) {
        if(user.isActive() && cmd.matches("[0-9]:[0-9]")) {
            int checkUserStep = gameService.setUserStep(cmd, user);
            if(checkUserStep == 1) {
                //попадание user
                User usr = userService.findOrSaveUser(update);
                user.setOpponentGameFiled(usr.getOpponentGameFiled());
                editMessageText.setText(gameMessageService
                        .getCurrentGameFiled("Попадание. Вы продолжаете...", gameFiledService.convertListFiledToArr(user.getUserGameFiled())));
                editMessageText.setReplyMarkup(InlineButton.gameBoard(user.getOpponentGameFiled()));
                user.setEditMessageText(editMessageText);
                if(!user.getOpponentId().equals(0L)) {
                    userUserController.userUserActionHit(update, editMessageText, user);
                }
            }
            else if(checkUserStep == 0) {
                //промах user
                User usr = userService.findOrSaveUser(update);
                user.setActive(false);
                user.setOpponentGameFiled(usr.getOpponentGameFiled());
                editMessageText.setText(gameMessageService
                        .getCurrentGameFiled("Промах. Ход противника...", gameFiledService.convertListFiledToArr(user.getUserGameFiled())));
                editMessageText.setReplyMarkup(InlineButton.gameBoard(user.getOpponentGameFiled()));
                user.setEditMessageText(editMessageText);
                if(!user.getOpponentId().equals(0L)) {
                    userUserController.userUserActionMiss(update, editMessageText, user);
                }
                else {
                    userAiController.userAiAction(update, editMessageText, user);
                }
            }
            else {
                //победа user
                endGameController.endGameOpponent(false, user.getOpponentId());
                user.setState(ONLINE);
                user.setActive(false);
                user.setOpponentId(0L);
                user.setUserGameFiled(new ArrayList<>());
                user.setOpponentGameFiled(new ArrayList<>());
                user.setCountVictory(user.getCountVictory() + 1);
                editMessageText.setText("Победа!\nВыберите действие, " + user.getName());
                editMessageText.setReplyMarkup(InlineButton.changeOptions());
                user.setEditMessageText(editMessageText);
            }
        }
        else if(!user.isActive()) {
            answerCallbackQuery.setText("Ход противника...");
        }
        user.setAnswerCallbackQuery(answerCallbackQuery);
        return user;
    }
}
