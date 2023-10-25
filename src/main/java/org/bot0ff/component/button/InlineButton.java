package org.bot0ff.component.button;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Component
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
        manuallyPrepare.get(0).setCallbackData("/startManuallyPrepare");

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

    //отменить поиск противника
    public static InlineKeyboardMarkup charGameBoard() {
        List<List<InlineKeyboardButton>> listButton = new ArrayList<>();
        listButton.add(new ArrayList<>());

        char[] chars = {'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ж', 'З', 'И' ,'К'};
        for(int i = 0, a = 0; i < chars.length; i++) {
            InlineKeyboardButton thingButton = new InlineKeyboardButton(String.valueOf(chars[i]));
            thingButton.setCallbackData(String.valueOf(chars[i]));
            listButton.get(a).add(thingButton);
            if(listButton.get(a).size() > 4) {
                listButton.add(new ArrayList<>());
                a++;
            }
        }

        List<InlineKeyboardButton> buttonArmory = new ArrayList<>();
//        buttonArmory.add(new InlineKeyboardButton("Оружейная"));
//        buttonArmory.get(0).setCallbackData("/Оружейная");
        listButton.add(listButton.size(), buttonArmory);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(listButton);

        return markupInline;
    }

    //отменить поиск противника
    public static InlineKeyboardMarkup numGameBoard() {
        List<List<InlineKeyboardButton>> listButton = new ArrayList<>();
        listButton.add(new ArrayList<>());

        int[] ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        for(int i = 0, a = 0; i < ints.length; i++) {
            InlineKeyboardButton thingButton = new InlineKeyboardButton(String.valueOf(ints[i]));
            thingButton.setCallbackData(String.valueOf(ints[i]));
            listButton.get(a).add(thingButton);
            if(listButton.get(a).size() > 4) {
                listButton.add(new ArrayList<>());
                a++;
            }
        }

        List<InlineKeyboardButton> buttonArmory = new ArrayList<>();
//        buttonArmory.add(new InlineKeyboardButton("Оружейная"));
//        buttonArmory.get(0).setCallbackData("/Оружейная");
        listButton.add(listButton.size(), buttonArmory);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(listButton);

        return markupInline;
    }
}
