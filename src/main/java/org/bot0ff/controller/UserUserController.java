package org.bot0ff.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bot0ff.component.TelegramBot;
import org.bot0ff.component.button.InlineButton;
import org.bot0ff.dto.ResponseDto;
import org.bot0ff.entity.User;
import org.bot0ff.service.UserService;
import org.bot0ff.service.game.GameFiledService;
import org.bot0ff.service.game.GameMessageService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Log4j
@Service
@RequiredArgsConstructor
public class UserUserController {
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();
    private TelegramBot telegramBot;
    private final UserService userService;
    private final GameFiledService gameFiledService;
    private final GameMessageService gameMessageService;

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void userUserActionMiss(Update update, EditMessageText editMessageText, User user) {
        if(update.hasCallbackQuery()) {
            editMessageText.setChatId(update.getCallbackQuery().getMessage().getChatId());
            editMessageText.setMessageId(user.getMessageId());
        }
        else return;

        EXECUTOR_SERVICE.execute(() -> {
            try {
                User usr = userService.findById(user.getOpponentId()).orElse(null);
                if (usr == null) return;
                usr.setUserGameFiled(user.getOpponentGameFiled());
                usr.setActive(true);
                userService.saveUser(usr);
                editMessageText.setChatId(usr.getId());
                editMessageText.setMessageId(usr.getMessageId());
                editMessageText.setText(gameMessageService
                        .getCurrentGameFiled("Противник не попал. Ваш ход...", gameFiledService.convertListFiledToArr(usr.getUserGameFiled())));
                editMessageText.setReplyMarkup(InlineButton.gameBoard(usr.getOpponentGameFiled()));
                var response = ResponseDto.builder()
                        .telegramBot(telegramBot)
                        .editMessageText(editMessageText)
                        .build();
                telegramBot.sendAnswer(response);
            }catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void userUserActionHit(Update update, EditMessageText editMessageText, User user) {
        if(update.hasCallbackQuery()) {
            editMessageText.setChatId(update.getCallbackQuery().getMessage().getChatId());
            editMessageText.setMessageId(user.getMessageId());
        }
        else return;

        EXECUTOR_SERVICE.execute(() -> {
            try {
                User usr = userService.findById(user.getOpponentId()).orElse(null);
                if (usr == null) return;
                usr.setUserGameFiled(user.getOpponentGameFiled());
                userService.saveUser(usr);
                editMessageText.setChatId(usr.getId());
                editMessageText.setMessageId(usr.getMessageId());
                editMessageText.setText(gameMessageService
                        .getCurrentGameFiled("Противник попал и продолжает...", gameFiledService.convertListFiledToArr(usr.getUserGameFiled())));
                editMessageText.setReplyMarkup(InlineButton.gameBoard(usr.getOpponentGameFiled()));
                var response = ResponseDto.builder()
                        .telegramBot(telegramBot)
                        .editMessageText(editMessageText)
                        .build();
                telegramBot.sendAnswer(response);
            }catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
