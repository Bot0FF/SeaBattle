package org.bot0ff.component.button;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bot0ff.entity.User;
import org.bot0ff.service.game.GameMessageService;
import org.bot0ff.service.game.ManuallyPrepareService;
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

    //старт новой игры
    public static InlineKeyboardMarkup changeOptions() {
        List<InlineKeyboardButton> newGame = new ArrayList<>();

        newGame.add(new InlineKeyboardButton("Начать новую игру"));
        newGame.get(0).setCallbackData("newGame");

        List<List<InlineKeyboardButton>> rowsInLine = List.of(newGame);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }

    //варианты расстановки кораблей
    public static InlineKeyboardMarkup changePlacementOption() {
        List<InlineKeyboardButton> newGameVsAI = new ArrayList<>();
        List<InlineKeyboardButton> newGameVsUser = new ArrayList<>();

        newGameVsAI.add(new InlineKeyboardButton("Расставить вручную"));
        newGameVsAI.get(0).setCallbackData("prepareManually");
        newGameVsUser.add(new InlineKeyboardButton("Расставить автоматически"));
        newGameVsUser.get(0).setCallbackData("prepareAutomatic");

        List<List<InlineKeyboardButton>> rowsInLine = List.of(newGameVsAI, newGameVsUser);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }

    public static InlineKeyboardMarkup startManuallyPrepare() {
        List<InlineKeyboardButton> manuallyPrepare = new ArrayList<>();

        manuallyPrepare.add(new InlineKeyboardButton("Начать расстановку"));
        manuallyPrepare.get(0).setCallbackData("startManuallyPrepare");

        List<List<InlineKeyboardButton>> rowsInLine = List.of(manuallyPrepare);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }

    public static InlineKeyboardMarkup startAutomaticPrepare() {
        List<InlineKeyboardButton> manuallyPrepare = new ArrayList<>();

        manuallyPrepare.add(new InlineKeyboardButton("Начать расстановку"));
        manuallyPrepare.get(0).setCallbackData("startAutomaticPrepare");

        List<List<InlineKeyboardButton>> rowsInLine = List.of(manuallyPrepare);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }

    public static InlineKeyboardMarkup confirmAutomaticPrepare() {
        List<InlineKeyboardButton> updateGameFiled = new ArrayList<>();
        List<InlineKeyboardButton> findOpponent = new ArrayList<>();
        List<InlineKeyboardButton> gameVsAi = new ArrayList<>();

        updateGameFiled.add(new InlineKeyboardButton("Расставить еще раз"));
        updateGameFiled.get(0).setCallbackData("updateAutomaticPrepare");
        findOpponent.add(new InlineKeyboardButton("Начать поиск противника"));
        findOpponent.get(0).setCallbackData("confirmFindOpponent");
        gameVsAi.add(new InlineKeyboardButton("Начать игру против ИИ"));
        gameVsAi.get(0).setCallbackData("confirmGameVsAi");


        List<List<InlineKeyboardButton>> rowsInLine = List.of(updateGameFiled, findOpponent, gameVsAi);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }

    //управление расстановкой кораблей
    public static InlineKeyboardMarkup setManuallyPrepareShip(User user) {
        int[][] tempUserFiled = ManuallyPrepareService.prepareManuallyMap.get(user.getId()).getUserFiled();
        List<List<InlineKeyboardButton>> listButton = new ArrayList<>();


        for(int ver = 0; ver < GAME_FILED_LENGTH; ver++) {
            for(int hor = 0; hor < GAME_FILED_LENGTH; hor++) {
                if(hor == 0) {
                    listButton.add(new ArrayList<>());
                }
                InlineKeyboardButton thingButton = new InlineKeyboardButton();
                if(tempUserFiled[ver][hor] == 1) {
                    thingButton.setText(GameMessageService.emojiPars(":sailboat:"));
                }
                else if(tempUserFiled[ver][hor] == 2){
                    thingButton.setText(GameMessageService.emojiPars(":speedboat:"));
                }
                else if(tempUserFiled[ver][hor] == 3){
                    thingButton.setText(GameMessageService.emojiPars(":ferry:"));
                }
                else if(tempUserFiled[ver][hor] == 4){
                    thingButton.setText(GameMessageService.emojiPars(":ship:"));
                }
                else if(tempUserFiled[ver][hor] == 5){
                    thingButton.setText(GameMessageService.emojiPars(":anchor:"));
                }
                else if(tempUserFiled[ver][hor] == 6){
                    thingButton.setText(GameMessageService.emojiPars(":anchor:"));
                }
                else {
                    thingButton.setText(" ");
                }
                thingButton.setCallbackData(ver + ":" + hor);
                listButton.get(ver).add(thingButton);
            }
        }

        List<InlineKeyboardButton> autoButton = new ArrayList<>();
        autoButton.add(new InlineKeyboardButton("Автоматически"));
        autoButton.add(new InlineKeyboardButton("Подтвердить"));
        autoButton.get(0).setCallbackData("autoPrepare");
        autoButton.get(1).setCallbackData("confirmPrepare");
        listButton.add(listButton.size(), autoButton);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(listButton);

        return markupInline;
    }

    //отменить поиск противника
    public static InlineKeyboardMarkup stopSearchGameButton() {
        List<InlineKeyboardButton> stopSearchGame = new ArrayList<>();

        stopSearchGame.add(new InlineKeyboardButton("Отменить поиск сражения"));
        stopSearchGame.get(0).setCallbackData("stopSearchGame");

        List<List<InlineKeyboardButton>> rowsInLine = List.of(stopSearchGame);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }

    //кнопки А-К
    public static InlineKeyboardMarkup charGameBoard() {
        List<List<InlineKeyboardButton>> listButton = new ArrayList<>();
        listButton.add(new ArrayList<>());

        char[] chars = {'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ж', 'З', 'И' ,'К'};
        for(int i = 0, a = 0; i < GAME_FILED_LENGTH; i++) {
            InlineKeyboardButton thingButton = new InlineKeyboardButton(String.valueOf(chars[i]));
            thingButton.setCallbackData(String.valueOf(chars[i]));
            listButton.get(a).add(thingButton);
            if(listButton.get(a).size() > 4) {
                listButton.add(new ArrayList<>());
                a++;
            }
        }

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(listButton);

        return markupInline;
    }

    //кнопки 1-10
    public static InlineKeyboardMarkup numGameBoard() {
        List<List<InlineKeyboardButton>> listButton = new ArrayList<>();
        listButton.add(new ArrayList<>());

        int[] ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        for(int i = 0, a = 0; i < GAME_FILED_LENGTH; i++) {
            InlineKeyboardButton thingButton = new InlineKeyboardButton(String.valueOf(ints[i]));
            thingButton.setCallbackData(String.valueOf(ints[i]));
            listButton.get(a).add(thingButton);
            if(listButton.get(a).size() > 4) {
                listButton.add(new ArrayList<>());
                a++;
            }
        }

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(listButton);

        return markupInline;
    }
}
