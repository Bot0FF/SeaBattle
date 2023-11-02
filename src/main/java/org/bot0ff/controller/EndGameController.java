package org.bot0ff.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bot0ff.component.TelegramBot;
import org.bot0ff.component.button.InlineButton;
import org.bot0ff.dto.ResponseDto;
import org.bot0ff.entity.User;
import org.bot0ff.service.UserService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

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

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    //сбрасывает настройки user и отправляет главную страницу по завершению игры
    public void endGameOpponent(Long opponentId) {
        EXECUTOR_SERVICE.execute(() -> {
            try {
                User opponent = userService.findById(opponentId).orElse(null);
                if(opponent == null) return;

                TimeUnit.SECONDS.sleep(1);
                var editMessageText = new EditMessageText();
                editMessageText.setChatId(opponent.getId());
                opponent.setState(ONLINE);
                opponent.setActive(false);
                opponent.setUserGameFiled(new ArrayList<>());
                opponent.setOpponentGameFiled(new ArrayList<>());
                userService.saveUser(opponent);
                editMessageText.setText("Поражение...\nВыберите действие, " + opponent.getName());
                editMessageText.setReplyMarkup(InlineButton.changeOptions());
                var response = ResponseDto.builder()
                        .telegramBot(telegramBot)
                        .editMessageText(editMessageText)
                        .build();
                telegramBot.sendAnswer(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
