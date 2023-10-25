package org.bot0ff.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.bot0ff.component.TelegramBot;
import org.bot0ff.component.button.InlineButton;
import org.bot0ff.dto.ResponseDto;
import org.bot0ff.entity.User;
import org.bot0ff.service.UserService;
import org.bot0ff.service.game.GameService;
import org.bot0ff.service.game.GenerateEmojiGameFiled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.bot0ff.entity.UserState.ONLINE;
import static org.bot0ff.util.Constants.VERTICAL_LENGTH;

@Log4j
@Service
@RequiredArgsConstructor
public class UserAiController {
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();
    private TelegramBot telegramBot;
    private final UserService userService;
    private final GenerateEmojiGameFiled generateEmojiGameFiled;

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void userAiAction(Update update, User user) {
        var sendMessage = new SendMessage();
        sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
        var answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(update.getCallbackQuery().getId());

        EXECUTOR_SERVICE.execute(() -> {
            try {
                var checkAiStep = 0;
                do {
                    TimeUnit.SECONDS.sleep(3);
                    checkAiStep = checkAiStep(user);
                    if(checkAiStep == 1) {
                        sendMessage.setText(getCurrentGameFiled("Противник попал и продолжает ход", user));
                    }
                    else if(checkAiStep == 0) {
                        sendMessage.setText(getCurrentGameFiled("Противник не попал. Ваш ход", user));
                        sendMessage.setReplyMarkup(InlineButton.charGameBoard());
                    }
                    else {
                        sendMessage.setText("Поражение...\nВыберите действие, " + user.getName());
                        sendMessage.setReplyMarkup(InlineButton.changeOptions());
                    }
                    var response = ResponseDto.builder()
                            .telegramBot(telegramBot)
                            .sendMessage(sendMessage)
                            .build();
                    telegramBot.sendAnswer(response);
                }
                while (checkAiStep == 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    //проверяет попадание ИИ
    private int checkAiStep(User user) {
        int ver = getRNum();
        int hor = getRNum();
        String existShip = ver + ":" + hor;
        String notExistShip = ver + "_" + hor;
        List<String> userFiled = user.getGameFiled();
        int result = 0;
        for (String coordinate : userFiled) {
            if(coordinate.equals(existShip)) {
                userFiled = userFiled.stream().map(target -> target.equals(existShip) ? (ver + "-" + hor) : target).toList();
                user.setGameFiled(userFiled);
                result = 1;
            }
            else if(coordinate.equals(notExistShip)) {
                userFiled = userFiled.stream().map(target -> target.equals(notExistShip) ? (ver + "/" + hor) : target).toList();
                user.setActive(true);
                user.setGameFiled(userFiled);
                result = 0;
            }
        }
        //проверка на оставшиеся корабли
        if(userFiled.stream().noneMatch(aliveShip -> aliveShip.contains(":"))) {
            user.setState(ONLINE);
            user.setActive(false);
            user.setGameFiled(new ArrayList<>());
            user.setAiGameFiled(new ArrayList<>());
            result = -1;
        }
        userService.saveUser(user);
        return result;
    }

    //рандом 0-9
    private int getRNum() {
        RandomDataGenerator randomGenerator = new RandomDataGenerator();
        return randomGenerator.nextInt(0, VERTICAL_LENGTH - 1);
    }

    private String getCurrentGameFiled(String notification, User user) {
        return  notification +
                "\nПоле противника\n" + generateEmojiGameFiled.getEmojiGameFiled(user.getAiGameFiled()) +
                "-----------------------\n" +
                "Ваше поле\n" + generateEmojiGameFiled.getEmojiGameFiled(user.getGameFiled());
    }
}
