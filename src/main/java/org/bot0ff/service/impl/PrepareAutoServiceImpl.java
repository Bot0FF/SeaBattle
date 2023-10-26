package org.bot0ff.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bot0ff.component.button.InlineButton;
import org.bot0ff.entity.User;
import org.bot0ff.service.PrepareAutomaticService;
import org.bot0ff.service.UserService;
import org.bot0ff.service.game.AutoPrepareService;
import org.bot0ff.service.game.GameMessageService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.bot0ff.entity.UserState.*;
import static org.bot0ff.service.ServiceCommands.*;

//обрабатывает запросы статуса PREPARE_AUTOMATIC
@Log4j
@Service
@RequiredArgsConstructor
public class PrepareAutoServiceImpl implements PrepareAutomaticService {
    private final UserService userService;
    private final AutoPrepareService autoPrepareService;
    private final GameMessageService gameMessageService;

    //ответы на текстовые запросы
    @Override
    public SendMessage optionsPrepareAutomaticText(User user, SendMessage sendMessage, String cmd) {
        if(START.equals(cmd)) {
            sendMessage.setText("Автоматическая расстановка кораблей...");
            sendMessage.setReplyMarkup(InlineButton.startAutomaticPrepare());
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
            sendMessage.setText("Автоматическая расстановка кораблей...");
            sendMessage.setReplyMarkup(InlineButton.startAutomaticPrepare());
        }
        return sendMessage;
    }

    //ответы на inline запросы
    @Override
    public SendMessage prepareGameAutomaticInline(User user, SendMessage sendMessage, String cmd) {
        switch (cmd) {
            case "startAutomaticPrepare", "updateAutomaticPrepare" -> {
                var userGameFiled = autoPrepareService.getAutomaticGameFiled();
                user.setGameFiled(userGameFiled);
                userService.saveUser(user);
                sendMessage.setText("Продолжить с текущей расстановкой?\n" + gameMessageService.getEmojiGameFiled(userGameFiled));
                sendMessage.setReplyMarkup(InlineButton.confirmAutomaticPrepare());
            }
            case "confirmFindOpponent" -> {
                user.setState(SEARCH_GAME);
                userService.saveUser(user);
                sendMessage.setText("Идет поиск противника...");
            }
            case "confirmGameVsAi" -> {
                //set userAi
                var aiGameFiled = autoPrepareService.getAutomaticGameFiled();
                user.setAiGameFiled(aiGameFiled);
                //set User
                user.setState(IN_GAME);
                user.setActive(true);
                userService.saveUser(user);
                sendMessage.setText(gameMessageService.getCurrentGameFiled("Началась игра против ИИ\n Первый ход ваш...", user));
                sendMessage.setReplyMarkup(InlineButton.charGameBoard());
            }
            default -> {
                sendMessage.setText("Автоматическая расстановка кораблей...");
                sendMessage.setReplyMarkup(InlineButton.startAutomaticPrepare());
            }
        }
        return sendMessage;
    }
}
