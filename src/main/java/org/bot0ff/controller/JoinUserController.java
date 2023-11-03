package org.bot0ff.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.bot0ff.component.TelegramBot;
import org.bot0ff.component.button.InlineButton;
import org.bot0ff.dto.ResponseDto;
import org.bot0ff.entity.User;
import org.bot0ff.service.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.bot0ff.entity.UserState.*;
import static org.bot0ff.util.Constants.GAME_FILED_LENGTH;

@Log4j
@Service
@RequiredArgsConstructor
public class JoinUserController {
    private static ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();
    public static final Map<Long, User> joinUserMap = Collections.synchronizedMap(new HashMap<>());
    private TelegramBot telegramBot;
    private final UserService userService;

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    //фоновый процесс настройки игры между двумя users, которые в статусе SEARCH_GAME
    @Scheduled(fixedDelay = 3000)
    private void setUserVsUserGame() {
        var editMessageText = new EditMessageText();
        var sendMessageForTwo = new SendMessage();
        List<Long> randomKey = new ArrayList<>(joinUserMap.keySet());
        if(!joinUserMap.isEmpty() && randomKey.size() < 2) {
            Long userIdOne = randomKey.get(getRNum(randomKey.size()));
            User userOne = joinUserMap.get(userIdOne);
            userOne.setState(PREPARE_GAME);
            userOne.setActive(false);
            editMessageText.setChatId(userIdOne);
            editMessageText.setMessageId(userOne.getMessageId());
            editMessageText.setText("Противник не найден...");
            editMessageText.setReplyMarkup(InlineButton.setManuallyPrepareShip(userOne));
            userService.saveUser(userOne);
            removeUserFromMap(userOne);
            var response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .editMessageText(editMessageText)
                    .build();
            telegramBot.sendAnswer(response);
        }
        else if(joinUserMap.size() > 1){
            while (joinUserMap.size() > 1) {
                EXECUTOR_SERVICE.execute(() -> {
                    Long userIdOne = randomKey.get(getRNum(randomKey.size()));
                    Long userIdTwo = randomKey.get(getRNum(randomKey.size()));
                    User userOne = userService.findById(userIdOne).orElse(null);
                    User userTwo = userService.findById(userIdTwo).orElse(null);
                    int firstStep = getFirstStep();
                    if(userOne != null && userTwo != null
                            && userOne.getState().equals(SEARCH_GAME)
                            && userTwo.getState().equals(SEARCH_GAME)) {
                        if(firstStep == 1) {
                            userOne.setActive(true);
                        }
                        else {
                            userTwo.setActive(true);
                        }
                        userOne.setState(IN_GAME);
                        userTwo.setState(IN_GAME);
                        userOne.setOpponentGameFiled(userTwo.getUserGameFiled());
                        userTwo.setOpponentGameFiled(userOne.getUserGameFiled());
                        userService.saveUser(userOne);
                        userService.saveUser(userTwo);
                    }
                    else {
                        if(userOne != null && !userOne.getState().equals(SEARCH_GAME)) {
                            removeUserFromMap(userOne);
                        }
                        if(userTwo != null && !userTwo.getState().equals(SEARCH_GAME)) {
                            removeUserFromMap(userTwo);
                        }
                    }
                });
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

    private int getFirstStep() {
        RandomDataGenerator randomGenerator = new RandomDataGenerator();
        return randomGenerator.nextInt(1, 2);
    }
}
