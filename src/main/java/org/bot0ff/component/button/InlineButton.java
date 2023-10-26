package org.bot0ff.component.button;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static org.bot0ff.util.Constants.GAME_FILED_LENGTH;

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
    public static InlineKeyboardMarkup moveShip() {
        List<InlineKeyboardButton> firstLine = new ArrayList<>();
        List<InlineKeyboardButton> secondLine = new ArrayList<>();

        firstLine.add(new InlineKeyboardButton("<"));
        firstLine.add(new InlineKeyboardButton(">"));
        firstLine.add(new InlineKeyboardButton("()"));
        firstLine.add(new InlineKeyboardButton("A"));
        firstLine.add(new InlineKeyboardButton("V"));
        firstLine.get(0).setCallbackData("moveLeft");
        firstLine.get(1).setCallbackData("moveRight");
        firstLine.get(2).setCallbackData("rotateShip");
        firstLine.get(3).setCallbackData("moveUp");
        firstLine.get(4).setCallbackData("moveDown");

        secondLine.add(new InlineKeyboardButton("Подтвердить"));
        secondLine.add(new InlineKeyboardButton("Отменить"));
        secondLine.get(0).setCallbackData("setShip");
        secondLine.get(1).setCallbackData("cancelShip");

        List<List<InlineKeyboardButton>> rowsInLine = List.of(firstLine, secondLine);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

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
