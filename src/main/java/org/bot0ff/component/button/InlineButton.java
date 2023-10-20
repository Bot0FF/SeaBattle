package org.bot0ff.component.button;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

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
        newGame.get(0).setCallbackData("/newGame");

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
        newGameVsAI.get(0).setCallbackData("/prepareManually");
        newGameVsUser.add(new InlineKeyboardButton("Расставить автоматически"));
        newGameVsUser.get(0).setCallbackData("/prepareAutomatic");

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
        manuallyPrepare.get(0).setCallbackData("/startAutomaticPrepare");

        List<List<InlineKeyboardButton>> rowsInLine = List.of(manuallyPrepare);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }

    public static InlineKeyboardMarkup confirmAutomaticPrepare() {
        List<InlineKeyboardButton> confirm = new ArrayList<>();
        List<InlineKeyboardButton> update = new ArrayList<>();

        confirm.add(new InlineKeyboardButton("Начать поиск противника"));
        confirm.get(0).setCallbackData("/confirmAutomaticPrepare");
        update.add(new InlineKeyboardButton("Расставить еще раз"));
        update.get(0).setCallbackData("/updateAutomaticPrepare");

        List<List<InlineKeyboardButton>> rowsInLine = List.of(confirm, update);
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

//    //список вещей user для ремонта
//    public static InlineKeyboardMarkup buttonListThingForRepair(List<UserThing> userThings, User user) {
//        List<List<InlineKeyboardButton>> listButton = new ArrayList<>();
//        listButton.add(new ArrayList<>());
//
//        for(int i = 0, a = 0; i < userThings.size(); i++) {
//            UserThing userThing = userThings.get(i);
//            if(userThing.getThingId() == user.getUserHead()
//                    | userThing.getThingId() == user.getUserBody()
//                    | userThing.getThingId() == user.getUserLegs()
//                    | userThing.getThingId() == user.getUserWeapon()
//                    | userThing.getThingId() == user.getUserEffect()) {
//                userThing.setThingName("(Н)" + userThing.getThingName());
//            }
//            InlineKeyboardButton thingButton = new InlineKeyboardButton(userThing.getThingName());
//            thingButton.setCallbackData("/armory:repairThing:[" + userThing.getThingId() + "]");
//            listButton.get(a).add(thingButton);
//            if(listButton.get(a).size() >= 3) {
//                listButton.add(new ArrayList<>());
//                a++;
//            }
//        }
//
//        List<InlineKeyboardButton> buttonArmory = new ArrayList<>();
//        buttonArmory.add(new InlineKeyboardButton("Оружейная"));
//        buttonArmory.get(0).setCallbackData("/Оружейная");
//        listButton.add(listButton.size(), buttonArmory);
//
//        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
//        markupInline.setKeyboard(listButton);
//
//        return markupInline;
//    }
}
