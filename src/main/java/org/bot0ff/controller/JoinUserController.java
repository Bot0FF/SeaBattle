package org.bot0ff.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.bot0ff.component.TelegramBot;
import org.bot0ff.component.button.InlineButton;
import org.bot0ff.dto.ResponseDto;
import org.bot0ff.entity.User;
import org.bot0ff.service.UserService;
import org.bot0ff.service.game.GameFiledService;
import org.bot0ff.service.game.GameMessageService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.*;

import static org.bot0ff.entity.UserState.*;

@Log4j
@Service
@RequiredArgsConstructor
public class JoinUserController {
    public static final Map<Long, User> joinUserMap = Collections.synchronizedMap(new HashMap<>());
    private TelegramBot telegramBot;
    private final UserService userService;
    private final GameFiledService gameFiledService;
    private final GameMessageService gameMessageService;

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    //фоновый процесс настройки игры между двумя users, которые в статусе SEARCH_GAME
    @Scheduled(fixedDelay = 5000)
    private void setUserVsUserGame() {
        System.out.println(joinUserMap.size());
        while (joinUserMap.size() > 1 && joinUserMap.size() % 2 == 0){
            var editMessageTextForOne = new EditMessageText();
            var editMessageTextForTwo = new EditMessageText();

            List<Long> randomKey = new ArrayList<>(joinUserMap.keySet());
            Long userIdOne = randomKey.get(getRNum(randomKey.size()));
            Long userIdTwo = randomKey.get(getRNum(randomKey.size()));
            User userOne = joinUserMap.get(userIdOne);
            User userTwo = joinUserMap.get(userIdTwo);
            if(userOne != null && userTwo != null
                    && userOne.getState().equals(SEARCH_GAME)
                    && userTwo.getState().equals(SEARCH_GAME)) {

                //сообщение для UserOne
                userOne.setState(IN_GAME);
                userOne.setActive(true);
                userOne.setOpponentId(userIdTwo);
                userOne.setOpponentGameFiled(userTwo.getUserGameFiled());
                editMessageTextForOne.setChatId(userIdOne);
                editMessageTextForOne.setMessageId(userOne.getMessageId());
                editMessageTextForOne.setText(gameMessageService
                        .getCurrentGameFiled("Ваш ход...", gameFiledService.convertListFiledToArr(userOne.getUserGameFiled())));
                editMessageTextForOne.setReplyMarkup(InlineButton.gameBoard(userOne.getOpponentGameFiled()));
                userOne.setEditMessageText(editMessageTextForOne);
                userService.saveUser(userOne);
                var responseOne = ResponseDto.builder()
                        .telegramBot(telegramBot)
                        .editMessageText(editMessageTextForOne)
                        .build();
                telegramBot.sendAnswer(responseOne);

                //сообщение для UserTwo
                userTwo.setState(IN_GAME);
                userTwo.setActive(false);
                userTwo.setOpponentId(userIdOne);
                userTwo.setOpponentGameFiled(userOne.getUserGameFiled());
                editMessageTextForTwo.setChatId(userIdTwo);
                editMessageTextForTwo.setMessageId(userTwo.getMessageId());
                editMessageTextForTwo.setText(gameMessageService
                        .getCurrentGameFiled("Ход противника...", gameFiledService.convertListFiledToArr(userTwo.getUserGameFiled())));
                editMessageTextForTwo.setReplyMarkup(InlineButton.gameBoard(userTwo.getOpponentGameFiled()));
                userTwo.setEditMessageText(editMessageTextForTwo);
                var responseTwo = ResponseDto.builder()
                        .telegramBot(telegramBot)
                        .editMessageText(editMessageTextForTwo)
                        .build();
                telegramBot.sendAnswer(responseTwo);
                userService.saveUser(userTwo);

                removeUserFromMap(userOne);
                removeUserFromMap(userTwo);
            }
            else {
                if(userOne != null && !userOne.getState().equals(SEARCH_GAME)) {
                    removeUserFromMap(userOne);
                }
                if(userTwo != null && !userTwo.getState().equals(SEARCH_GAME)) {
                    removeUserFromMap(userTwo);
                }
            }
        }
    }

    public static void removeUserFromMap(User user) {
        synchronized (joinUserMap) {
            joinUserMap.remove(user.getId());
        }
    }

    private int getRNum(int mapSize) {
        RandomDataGenerator randomGenerator = new RandomDataGenerator();
        return randomGenerator.nextInt(0, mapSize - 1);
    }
}
