package org.bot0ff.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bot0ff.component.button.InlineButton;
import org.bot0ff.controller.EndGameController;
import org.bot0ff.entity.User;
import org.bot0ff.service.InGameService;
import org.bot0ff.service.UserService;
import org.bot0ff.controller.UserAiController;
import org.bot0ff.service.game.GameService;
import org.bot0ff.service.game.GameMessageService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

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
    private final UserAiController userAiController;
    private final EndGameController endGameController;

    //ответы на текстовые запросы
    @Override
    public SendMessage processTextMessage(Update update, User user, SendMessage sendMessage, String cmd) {
        if(CANCEL.equals(cmd)) {
            user.setState(ONLINE);
            userService.saveUser(user);
            sendMessage.setText("Сброс настроек игры...\nВыберите действие, " + user.getName());
            sendMessage.setReplyMarkup(InlineButton.changeOptions());
        }
        else if(HELP.equals(cmd)) {
            sendMessage.setText("Помощь");
        }
        else {
            if(user.isActive() & user.getChangeTarget().isEmpty()) {
                sendMessage.setText(gameMessageService.getCurrentGameFiled("Ваш ход...", user));
                sendMessage.setReplyMarkup(InlineButton.charGameBoard());
            }
            else if(user.isActive() & !user.getChangeTarget().isEmpty()) {
                sendMessage.setText(gameMessageService.getCurrentGameFiled("Ваш ход...", user));
                sendMessage.setReplyMarkup(InlineButton.numGameBoard());
            }
            else {
                sendMessage.setText("Идет сражение...");
                userAiController.userAiAction(update, user);
            }
        }
        return sendMessage;
    }

    //ответы на inline запросы
    @Override
    public SendMessage processCallbackQuery(Update update, User user, SendMessage sendMessage, String cmd) {
        if(user.isActive() & cmd.matches("[А-К]") & user.getChangeTarget().isEmpty()) {
            user.setChangeTarget(cmd);
            userService.saveUser(user);
            sendMessage.setText(gameMessageService.getCurrentGameFiled("Ваш ход...", user));
            sendMessage.setReplyMarkup(InlineButton.numGameBoard());
        }
        else if(user.isActive() & cmd.matches("[1-9]|10") & !user.getChangeTarget().isEmpty()) {
            var userChangeTarget = user.getChangeTarget() + ":" + cmd;
            var checkStepUser = gameService.checkUserStep(userChangeTarget, user);
            if(checkStepUser == 1) {
                sendMessage.setText(gameMessageService.getCurrentGameFiled("Вы попали! Ваш ход...", user));
                sendMessage.setReplyMarkup(InlineButton.charGameBoard());
            }
            else if(checkStepUser == 0) {
                sendMessage.setText(gameMessageService.getCurrentGameFiled("Вы не попали! Ход противника...", user));
                userAiController.userAiAction(update, user);
            }
            else {
                sendMessage.setText("Победа!");
                endGameController.endGame(update);
            }
        }
        else {
            if(user.isActive()) {
                if(user.getChangeTarget().isEmpty()) {
                    sendMessage.setText(gameMessageService.getCurrentGameFiled("Ваш ход", user));
                    sendMessage.setReplyMarkup(InlineButton.charGameBoard());
                }
                else {
                    sendMessage.setText(gameMessageService.getCurrentGameFiled("Ваш ход", user));
                    sendMessage.setReplyMarkup(InlineButton.numGameBoard());
                }
            }
            else {
                sendMessage.setText("Идет сражение...");
                userAiController.userAiAction(update, user);
            }
        }
        return sendMessage;
    }
}
