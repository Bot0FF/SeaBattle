package org.bot0ff.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bot0ff.component.button.InlineButton;
import org.bot0ff.entity.User;
import org.bot0ff.service.InGameService;
import org.bot0ff.service.UserService;
import org.bot0ff.service.game.ActiveGames;
import org.bot0ff.service.game.GameService;
import org.bot0ff.service.game.GenerateEmojiGameFiled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.bot0ff.entity.UserState.ONLINE;
import static org.bot0ff.service.ServiceCommands.*;

//обрабатывает запросы статуса IN_GAME
@Log4j
@Service
@RequiredArgsConstructor
public class InGameServiceImpl implements InGameService {
    private final UserService userService;
    private final GameService gameService;
    private final GenerateEmojiGameFiled generateEmojiGameFiled;
    private final ActiveGames activeGames;

    @Override
    public SendMessage processTextMessage(User user, SendMessage sendMessage, String cmd) {
        if(START.equals(cmd)) {
            sendMessage.setText("Идет сражение...");
            sendMessage.setReplyMarkup(InlineButton.startAutomaticPrepare());
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
            sendMessage.setText("Идет сражение");
            sendMessage.setReplyMarkup(InlineButton.startAutomaticPrepare());
        }
        return sendMessage;
    }

    @Override
    public SendMessage processCallbackQuery(User user, SendMessage sendMessage, String cmd) {
        if(user.isActive() & cmd.matches("[А-К]") & user.getChangeTarget().equals("")) {
            user.setChangeTarget(cmd);
            userService.saveUser(user);
            sendMessage.setText("Ваш ход \n" +
                    "Поле противника\n" + generateEmojiGameFiled.getEmojiGameFiled(activeGames.getAiUser(user.getGameId()).getGameFiled()) +
                    "-----------------------\n" +
                    "Ваше поле\n" + generateEmojiGameFiled.getEmojiGameFiled(user.getGameFiled()));
            sendMessage.setReplyMarkup(InlineButton.numGameBoard());
        }
        else if(user.isActive() & cmd.matches("[1-9]") & user.getChangeTarget().matches("[А-К]")) {
            var userChangeTarget = user.getChangeTarget() + ":" + cmd;
            var userAi = activeGames.getAiUser(user.getGameId());
            System.out.println(userAi.getGameFiled());
            var check = gameService.checkEndStep(userChangeTarget, userAi);
            System.out.println(userAi.getGameFiled());
            user.setChangeTarget("");
            userService.saveUser(user);
            if(check) {
                sendMessage.setText("Вы попали. Ваш ход \n" +
                        "Поле противника\n" + generateEmojiGameFiled.getEmojiGameFiled(activeGames.getAiUser(user.getGameId()).getGameFiled()) +
                        "-----------------------\n" +
                        "Ваше поле\n" + generateEmojiGameFiled.getEmojiGameFiled(user.getGameFiled()));
                sendMessage.setReplyMarkup(InlineButton.charGameBoard());
            } else {
                user.setActive(false);
                userService.saveUser(user);
                sendMessage.setText("Вы не попали. Ход противника \n" +
                        "Поле противника\n" + generateEmojiGameFiled.getEmojiGameFiled(activeGames.getAiUser(user.getGameId()).getGameFiled()) +
                        "-----------------------\n" +
                        "Ваше поле\n" + generateEmojiGameFiled.getEmojiGameFiled(user.getGameFiled()));
            }
        }
        else {
            sendMessage.setText(
                    "Поле противника\n" + generateEmojiGameFiled.getEmojiGameFiled(activeGames.getAiUser(user.getGameId()).getGameFiled()) +
                    "-----------------------\n" +
                    "Ваше поле\n" + generateEmojiGameFiled.getEmojiGameFiled(user.getGameFiled()));
            if(user.isActive() & user.getChangeTarget() == null) {
                sendMessage.setReplyMarkup(InlineButton.charGameBoard());
            }
            else if(user.isActive() & user.getChangeTarget() != null) {
                sendMessage.setReplyMarkup(InlineButton.numGameBoard());
            }
        }

        return sendMessage;
    }
}
