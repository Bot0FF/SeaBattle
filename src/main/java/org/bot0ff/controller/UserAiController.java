package org.bot0ff.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.bot0ff.component.TelegramBot;
import org.bot0ff.component.button.InlineButton;
import org.bot0ff.dto.ResponseDto;
import org.bot0ff.entity.AiUser;
import org.bot0ff.entity.User;
import org.bot0ff.service.UserService;
import org.bot0ff.service.game.ActiveGames;
import org.bot0ff.service.game.GameService;
import org.bot0ff.service.game.GenerateEmojiGameFiled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Log4j
@Service
@RequiredArgsConstructor
public class UserAiController {
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();
    private TelegramBot telegramBot;
    private final UserService userService;
    private final GameService gameService;
    private final GenerateEmojiGameFiled generateEmojiGameFiled;
    private final ActiveGames activeGames;

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void userAiAction(Update update, User user, AiUser aiUser) {
        var sendMessage = new SendMessage();
        sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
        var answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(update.getCallbackQuery().getId());

        EXECUTOR_SERVICE.execute(() -> {
            try {
                boolean checkStepAi;
                do {
                    TimeUnit.SECONDS.sleep(3);
                    checkStepAi = checkAiStep(user);
                    if(checkStepAi) {
                        sendMessage.setText("Противник попал и продолжает ход. \n" +
                                "Поле противника\n" + generateEmojiGameFiled.getEmojiGameFiled(activeGames.getAiUser(user.getGameId()).getGameFiled()) +
                                "-----------------------\n" +
                                "Ваше поле\n" + generateEmojiGameFiled.getEmojiGameFiled(user.getGameFiled()));
                    }
                    else {
                        sendMessage.setText("Противник не попал. Ваш ход. \n" +
                                "Поле противника\n" + generateEmojiGameFiled.getEmojiGameFiled(activeGames.getAiUser(user.getGameId()).getGameFiled()) +
                                "-----------------------\n" +
                                "Ваше поле\n" + generateEmojiGameFiled.getEmojiGameFiled(user.getGameFiled()));
                        sendMessage.setReplyMarkup(InlineButton.charGameBoard());
                        user.setActive(true);
                        userService.saveUser(user);
                    }
                    var response = ResponseDto.builder()
                            .telegramBot(telegramBot)
                            .sendMessage(sendMessage)
                            .build();
                    telegramBot.sendAnswer(response);
                }
                while (checkStepAi);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    //проверяет попадание ИИ
    private boolean checkAiStep(User user) {
        int ver = getRNum();
        int hor = getRNum();
        String existShip = ver + ":" + hor;
        String notExistShip = ver + "_" + hor;
        List<String> userFiled = user.getGameFiled();
        for (String coordinate : userFiled) {
            if(coordinate.equals(existShip)) {
                userFiled = userFiled.stream().map(target -> target.equals(existShip) ? (ver + "-" + hor) : target).toList();
                user.setGameFiled(userFiled);
                userService.saveUser(user);
                return true;
            }
            else if(coordinate.equals(notExistShip)) {
                userFiled = userFiled.stream().map(target -> target.equals(notExistShip) ? (ver + "/" + hor) : target).toList();
                user.setGameFiled(userFiled);
                userService.saveUser(user);
                return false;
            }
        }
        return false;
    }

    //рандом 0-9
    private int getRNum() {
        RandomDataGenerator randomGenerator = new RandomDataGenerator();
        return randomGenerator.nextInt(0, 9);
    }
}
