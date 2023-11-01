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
        newGame.get(0).setCallbackData("/newGame");

        List<List<InlineKeyboardButton>> rowsInLine = List.of(newGame);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }

    //управление расстановкой кораблей
    public static InlineKeyboardMarkup setManuallyPrepareShip(User user) {
        int[][] tempUserFiled = ManuallyPrepareService.prepareManuallyMap.get(user.getId());
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
