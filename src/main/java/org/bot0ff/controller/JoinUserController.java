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

import static org.bot0ff.entity.UserState.ONLINE;
import static org.bot0ff.entity.UserState.PREPARE_GAME;
import static org.bot0ff.util.Constants.GAME_FILED_LENGTH;

@Log4j
@Service
@RequiredArgsConstructor
public class JoinUserController {
    public static Map<Long, User> joinUserMap = Collections.synchronizedMap(new HashMap<>());
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
            joinUserMap.remove(userIdOne);
            var response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .editMessageText(editMessageText)
                    .build();
            telegramBot.sendAnswer(response);
        }
        else if(joinUserMap.size() > 1){
            Long userIdOne = randomKey.get(getRNum(randomKey.size()));
            Long userIdTwo = randomKey.get(getRNum(randomKey.size()));
            User userOne = joinUserMap.get(userIdOne);
            User userTwo = joinUserMap.get(userIdTwo);
        }
    }

    private int getRNum(int mapSize) {
        RandomDataGenerator randomGenerator = new RandomDataGenerator();
        return randomGenerator.nextInt(0, mapSize - 1);
    }
}
