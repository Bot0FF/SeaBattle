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
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.bot0ff.entity.UserState.ONLINE;
import static org.bot0ff.util.Constants.GAME_FILED_LENGTH;

/**
 Контроллер ИИ. При передаче управления производит ход до тех пор,
 пока попадает, либо пока не осталось кораблей у игрока.
 Если ИИ попадает по ранее выбранной координате, ход выполняется еще раз
 до тех пор, пока попадет по ранее не выбранной координате.
**/

//TODO сделать выбор координат по соседним квадратам, в случае попадания
@Log4j
@Service
@RequiredArgsConstructor
public class UserAiController {
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();
    private TelegramBot telegramBot;
    private final UserService userService;
    private final GameFiledService gameFiledService;
    private final GameMessageService gameMessageService;

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void userAiAction(Update update, EditMessageText editMessageText, User user) {
        if(update.hasCallbackQuery()) {
            editMessageText.setChatId(update.getCallbackQuery().getMessage().getChatId());
            editMessageText.setMessageId(user.getMessageId());
        }
        else return;

        EXECUTOR_SERVICE.execute(() -> {
            try {
                var checkAiStep = 0;
                do {
                    User usr = userService.findOrSaveUser(update);
                    TimeUnit.SECONDS.sleep(2);
                    checkAiStep = checkAiStep(usr);
                    if(checkAiStep == 1) {
                        editMessageText.setText(gameMessageService
                                .getCurrentGameFiled("Противник попал и продолжает...", gameFiledService.convertListFiledToArr(usr.getUserGameFiled())));
                        editMessageText.setReplyMarkup(InlineButton.gameBoard(usr.getOpponentGameFiled()));
                    }
                    else if(checkAiStep == 0){
                        editMessageText.setText(gameMessageService
                                .getCurrentGameFiled("Противник не попал. Ваш ход...", gameFiledService.convertListFiledToArr(usr.getUserGameFiled())));
                        editMessageText.setReplyMarkup(InlineButton.gameBoard(usr.getOpponentGameFiled()));
                    }
                    else {
                        editMessageText.setText("Поражение...\nВыберите действие, " + usr.getName());
                        editMessageText.setReplyMarkup(InlineButton.changeOptions());
                    }
                    var response = ResponseDto.builder()
                            .telegramBot(telegramBot)
                            .editMessageText(editMessageText)
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
        int result;
        int targetVer = getRNum();
        int targetHor = getRNum();

        int[][] userGameFiled = gameFiledService.convertListFiledToArr(user.getUserGameFiled());

        //проверка наличия оставшихся кораблей user
        int countShips = 0;
        for(int ver = 0; ver < GAME_FILED_LENGTH; ver++) {
            for(int hor = 0; hor < GAME_FILED_LENGTH; hor++) {
                if(userGameFiled[ver][hor] == 1
                        | userGameFiled[ver][hor] == 2
                        | userGameFiled[ver][hor] == 3
                        | userGameFiled[ver][hor] == 4) {
                    countShips++;
                }
            }
        }

        if(countShips < 1) {
            user.setState(ONLINE);
            user.setActive(false);
            user.setOpponentId(0L);
            user.setUserGameFiled(new ArrayList<>());
            user.setOpponentGameFiled(new ArrayList<>());
            result = -1;
        }
        else if(userGameFiled[targetVer][targetHor] == 1
                | userGameFiled[targetVer][targetHor] == 2
                | userGameFiled[targetVer][targetHor] == 3
                | userGameFiled[targetVer][targetHor] == 4) {
            userGameFiled[targetVer][targetHor] = -1;
            user.setUserGameFiled(gameFiledService.convertArrFiledToList(userGameFiled));
            result = 1;
        }
        else {
            userGameFiled[targetVer][targetHor] = -2;
            user.setActive(true);
            user.setUserGameFiled(gameFiledService.convertArrFiledToList(userGameFiled));
            result = 0;
        }
        userService.saveUser(user);
        return result;
    }

    //рандом 0-9
    private int getRNum() {
        RandomDataGenerator randomGenerator = new RandomDataGenerator();
        return randomGenerator.nextInt(0, (GAME_FILED_LENGTH - 1));
    }
}
