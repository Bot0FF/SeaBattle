package org.bot0ff.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.bot0ff.entity.UserState.ONLINE;

@Log4j
@Service
@RequiredArgsConstructor
public class EndGameController {
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();
    private TelegramBot telegramBot;
    private final UserService userService;
    private final GameService gameService;
    private final GenerateEmojiGameFiled generateEmojiGameFiled;

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void endGame(Update update) {
        EXECUTOR_SERVICE.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                User user = userService.findOrSaveUser(update);
                var sendMessage = new SendMessage();
                sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                var answerCallbackQuery = new AnswerCallbackQuery();
                answerCallbackQuery.setCallbackQueryId(update.getCallbackQuery().getId());
                user.setState(ONLINE);
                user.setChangeTarget("");
                user.setActive(false);
                user.setGameFiled(new ArrayList<>());
                user.setAiGameFiled(new ArrayList<>());
                userService.saveUser(user);
                sendMessage.setText("Выберите действие, " + user.getName());
                sendMessage.setReplyMarkup(InlineButton.changeOptions());
                var response = ResponseDto.builder()
                        .telegramBot(telegramBot)
                        .sendMessage(sendMessage)
                        .answerCallbackQuery(answerCallbackQuery)
                        .build();
                telegramBot.sendAnswer(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
