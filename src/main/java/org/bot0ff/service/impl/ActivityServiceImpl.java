package org.bot0ff.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bot0ff.component.button.InlineButton;
import org.bot0ff.entity.User;
import org.bot0ff.service.ActivityService;
import org.bot0ff.service.game.GameFiledService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import static org.bot0ff.entity.UserState.*;
import static org.bot0ff.service.ServiceCommands.*;
import static org.bot0ff.util.Constants.GAME_FILED_LENGTH;

//обрабатывает запросы статуса ONLINE
@Log4j
@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    //ответы на текстовые запросы
    @Override
    public User changeOptionsFromMenu(User user, SendMessage sendMessage, String cmd) {
        if(START.equals(cmd)) {
            sendMessage.setText("Выберите действие, " + user.getName());
            sendMessage.setReplyMarkup(InlineButton.changeOptions());
            user.setSendMessage(sendMessage);
        }
        else if(CANCEL.equals(cmd)) {
            sendMessage.setText("Текущих сражений нет...");
            sendMessage.setReplyMarkup(InlineButton.changeOptions());
            user.setSendMessage(sendMessage);
        }
        return user;
    }

    //ответы на inline запросы
    @Override
    public User changeOptions(User user, EditMessageText editMessageText, AnswerCallbackQuery answerCallbackQuery, String cmd) {
        if(cmd.equals("/newGame")) {
            user.setState(PREPARE_GAME);
            GameFiledService.prepareUserFiledMap.put(user.getId(), new int[GAME_FILED_LENGTH][GAME_FILED_LENGTH]);
            editMessageText.setText("Подготовка игрового поля...");
            editMessageText.setReplyMarkup(InlineButton.setManuallyPrepareShip(user));
            user.setEditMessageText(editMessageText);
        }
        //TODO сделать статистику по всем
        else if(cmd.equals("/userStatistic")) {
            editMessageText.setText(String.format("Статистика игрока %s \nПобед: %d \nПоражений: %d",
                    user.getName(), user.getCountVictory(), user.getCountLoss()));
            editMessageText.setReplyMarkup(InlineButton.mainPageButton());
            user.setEditMessageText(editMessageText);
        }
        //TODO сделать помощь
        else if(cmd.equals("/help")) {
            editMessageText.setText(
                    "Игра \"Морской бой\" представляет из себя пошаговое сражение, " +
                    "где у каждого игрока есть свое поле с расставленными кораблями. " +
                    "Задача каждого игрока - потопить все корабли противника, " +
                    "выбирая координаты для атаки. Если атака удачная, то ход продолжается, " +
                    "если нет, то ходит противник." +
                    "\nДля начала игры на поле должны быть размещены следующие корабли в количестве: " +
                    "\nЧетырехпалубный - 1" +
                    "\nТрехпалубный - 2" +
                    "\nДвухпалубный - 3" +
                    "\nОднопалубный - 4" +
                    "Удачного сражения!");
            editMessageText.setReplyMarkup(InlineButton.mainPageButton());
            user.setEditMessageText(editMessageText);
        }
        else if(cmd.equals("/mainPage")) {
            editMessageText.setText("Выберите действие, " + user.getName());
            editMessageText.setReplyMarkup(InlineButton.changeOptions());
            user.setEditMessageText(editMessageText);
        }

        user.setAnswerCallbackQuery(answerCallbackQuery);
        return user;
    }
}
