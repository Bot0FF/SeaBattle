package org.bot0ff.component.button;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bot0ff.entity.User;
import org.bot0ff.service.game.GameFiledService;
import org.bot0ff.service.game.GameMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static org.bot0ff.util.Constants.GAME_FILED_LENGTH;

@Data
@Component
@RequiredArgsConstructor
public class InlineButton {

    //оставить имя как есть при регистрации
    public static InlineKeyboardMarkup registrationButton() {
        List<InlineKeyboardButton> registration = new ArrayList<>();

        registration.add(new InlineKeyboardButton("Оставить как есть"));
        registration.get(0).setCallbackData("/newUserWithCurrentName");

        List<List<InlineKeyboardButton>> rowsInLine = List.of(registration);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }

    //на главную страницу
    public static InlineKeyboardMarkup mainPageButton() {
        List<InlineKeyboardButton> mainPage = new ArrayList<>();

        mainPage.add(new InlineKeyboardButton("На главную"));
        mainPage.get(0).setCallbackData("/mainPage");

        List<List<InlineKeyboardButton>> rowsInLine = List.of(mainPage);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }

    //старт новой игры
    public static InlineKeyboardMarkup changeOptions() {
        List<InlineKeyboardButton> newGame = new ArrayList<>();
        List<InlineKeyboardButton> userStatistic = new ArrayList<>();
        List<InlineKeyboardButton> help = new ArrayList<>();

        newGame.add(new InlineKeyboardButton("Начать новую игру"));
        userStatistic.add(new InlineKeyboardButton("Статистика"));
        help.add(new InlineKeyboardButton("Помощь"));
        newGame.get(0).setCallbackData("/newGame");
        userStatistic.get(0).setCallbackData("/userStatistic");
        help.get(0).setCallbackData("/help");

        List<List<InlineKeyboardButton>> rowsInLine = List.of(newGame, userStatistic, help);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }

    //управление расстановкой кораблей
    public static InlineKeyboardMarkup setManuallyPrepareShip(User user) {
        int[][] tempUserFiled = GameFiledService.prepareUserFiledMap.get(user.getId());
        List<List<InlineKeyboardButton>> listButton = new ArrayList<>();

        for(int ver = 0; ver < GAME_FILED_LENGTH; ver++) {
            for(int hor = 0; hor < GAME_FILED_LENGTH; hor++) {
                if(hor == 0) {
                    listButton.add(new ArrayList<>());
                }
                InlineKeyboardButton button = new InlineKeyboardButton();
                if(tempUserFiled[ver][hor] == 1) {
                    button.setText(GameMessageService.emojiPars(":sailboat:"));
                }
                else if(tempUserFiled[ver][hor] == 2){
                    button.setText(GameMessageService.emojiPars(":speedboat:"));
                }
                else if(tempUserFiled[ver][hor] == 3){
                    button.setText(GameMessageService.emojiPars(":ferry:"));
                }
                else if(tempUserFiled[ver][hor] == 4){
                    button.setText(GameMessageService.emojiPars(":ship:"));
                }
                else if(tempUserFiled[ver][hor] == 5){
                    button.setText(GameMessageService.emojiPars(":anchor:"));
                }
                else if(tempUserFiled[ver][hor] == 6){
                    button.setText(GameMessageService.emojiPars(":anchor:"));
                }
                else {
                    button.setText(" ");
                }
                button.setCallbackData(ver + ":" + hor);
                listButton.get(ver).add(button);
            }
        }

        List<InlineKeyboardButton> autoPrepareButton = new ArrayList<>();
        autoPrepareButton.add(new InlineKeyboardButton("Расставить автоматически"));
        autoPrepareButton.get(0).setCallbackData("/autoPrepareGameFiled");

        List<InlineKeyboardButton> startGameButton = new ArrayList<>();
        startGameButton.add(new InlineKeyboardButton("Игрок VS Игрок"));
        startGameButton.add(new InlineKeyboardButton("Игрок VS ИИ"));
        startGameButton.get(0).setCallbackData("/searchGameVsUser");
        startGameButton.get(1).setCallbackData("/searchGameVsAi");

        listButton.add(listButton.size(), autoPrepareButton);
        listButton.add(listButton.size(), startGameButton);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(listButton);

        return markupInline;
    }

    //отменить поиск противника
    public static InlineKeyboardMarkup stopSearchGameButton() {
        List<InlineKeyboardButton> stopSearchGame = new ArrayList<>();

        stopSearchGame.add(new InlineKeyboardButton("Отменить поиск сражения"));
        stopSearchGame.get(0).setCallbackData("/stopSearchGame");

        List<List<InlineKeyboardButton>> rowsInLine = List.of(stopSearchGame);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }

    //активное игровое поле
    public static InlineKeyboardMarkup gameBoard(List<String> opponentFiled) {
        int[][] tempArr = new int[GAME_FILED_LENGTH][GAME_FILED_LENGTH];
        for(String coordinate : opponentFiled) {
            String[] split;
            if(coordinate.contains("x")) {
                split = coordinate.split("x");
                tempArr[Integer.parseInt(split[0])][Integer.parseInt(split[1])] = -1;
            }
            else if(coordinate.contains("o")) {
                split = coordinate.split("o");
                tempArr[Integer.parseInt(split[0])][Integer.parseInt(split[1])] = -2;
            }
        }
        List<List<InlineKeyboardButton>> listButton = new ArrayList<>();

        for(int ver = 0; ver < GAME_FILED_LENGTH; ver++) {
            for(int hor = 0; hor < GAME_FILED_LENGTH; hor++) {
                if(hor == 0) {
                    listButton.add(new ArrayList<>());
                }
                InlineKeyboardButton button = new InlineKeyboardButton();
                if(tempArr[ver][hor] == -1) {
                    button.setText(GameMessageService.emojiPars(":red_circle:"));
                    button.setCallbackData(ver + "x" + hor);
                }
                else if(tempArr[ver][hor] == -2){
                    button.setText(GameMessageService.emojiPars(":white_circle:"));
                    button.setCallbackData(ver + "o" + hor);
                }
                else {
                    button.setText(" ");
                    button.setCallbackData(ver + ":" + hor);
                }
                listButton.get(ver).add(button);
            }
        }

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(listButton);

        return markupInline;
    }
}
