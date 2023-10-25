package org.bot0ff.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bot0ff.component.button.InlineButton;
import org.bot0ff.component.button.TextButton;
import org.bot0ff.entity.AiUser;
import org.bot0ff.entity.User;
import org.bot0ff.service.PrepareAutomaticService;
import org.bot0ff.service.UserService;
import org.bot0ff.service.game.ActiveGames;
import org.bot0ff.service.game.AutomaticPrepareFiled;
import org.bot0ff.service.game.GenerateEmojiGameFiled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.bot0ff.entity.UserState.*;
import static org.bot0ff.service.ServiceCommands.*;

//обрабатывает запросы статуса PREPARE_AUTOMATIC
@Log4j
@Service
@RequiredArgsConstructor
public class PrepareAutomaticServiceImpl implements PrepareAutomaticService {
    private final UserService userService;
    private final AutomaticPrepareFiled automaticPrepareFiled;
    private final GenerateEmojiGameFiled generateEmojiGameFiled;
    private final ActiveGames activeGames;

    //ответы на текстовые запросы
    @Override
    public SendMessage optionsPrepareAutomaticText(User user, SendMessage sendMessage, String cmd) {
        if(START.equals(cmd)) {
            sendMessage.setText("Выбрана автоматическая расстановка кораблей");
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
            sendMessage.setText("Выбрана автоматическая расстановка кораблей");
            sendMessage.setReplyMarkup(InlineButton.startAutomaticPrepare());
        }
        return sendMessage;
    }

    //ответы на inline запросы
    @Override
    public SendMessage prepareGameAutomaticInline(User user, SendMessage sendMessage, String cmd) {
        if(cmd.equals("/startAutomaticPrepare")) {
            var currentGameFiled = automaticPrepareFiled.getAutomaticGameFiled();
            user.setGameFiled(currentGameFiled);
            userService.saveUser(user);
            sendMessage.setText("Продолжить с текущей расстановкой?\n" + generateEmojiGameFiled.getEmojiGameFiled(currentGameFiled));
            sendMessage.setReplyMarkup(InlineButton.confirmAutomaticPrepare());
        }
        else if(cmd.equals("/updateAutomaticPrepare")) {
            var currentGameFiled = automaticPrepareFiled.getAutomaticGameFiled();
            user.setGameFiled(currentGameFiled);
            userService.saveUser(user);
            sendMessage.setText("Продолжить с текущей расстановкой?\n" + generateEmojiGameFiled.getEmojiGameFiled(currentGameFiled));
            sendMessage.setReplyMarkup(InlineButton.confirmAutomaticPrepare());
        }
        else if(cmd.equals("/confirmFindOpponent")) {
            user.setState(SEARCH_GAME);
            userService.saveUser(user);
            sendMessage.setText("Идет поиск противника");
        }
        else if(cmd.equals("/confirmGameVsAi")) {
            var aiGameFiled = automaticPrepareFiled.getAutomaticGameFiled();
            Long newActiveGame = (long) ActiveGames.currentGames.size();
            //set User
            user.setState(IN_GAME);
            user.setGameId(newActiveGame);
            user.setActive(true);
            userService.saveUser(user);
            //set userAi
            AiUser aiUser = new AiUser();
            aiUser.setGameId(newActiveGame);
            aiUser.setGameFiled(aiGameFiled);
            aiUser.setActive(false);
            //set ActiveGame
            activeGames.addUsers(aiUser);
            sendMessage.setText("Началась игра против ИИ. Первый ход ваш.\n" +
                    "Поле противника\n" + generateEmojiGameFiled.getEmojiGameFiled(aiGameFiled) +
                    "-----------------------\n" +
                    "Ваше поле\n" + generateEmojiGameFiled.getEmojiGameFiled(user.getGameFiled()));
            sendMessage.setReplyMarkup(InlineButton.charGameBoard());
        }
        else {
            sendMessage.setText("Выбрана автоматическая расстановка кораблей");
            sendMessage.setReplyMarkup(InlineButton.startAutomaticPrepare());
        }
        return sendMessage;
    }
}
