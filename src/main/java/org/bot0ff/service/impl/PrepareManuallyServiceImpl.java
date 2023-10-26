package org.bot0ff.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bot0ff.component.button.InlineButton;
import org.bot0ff.entity.User;
import org.bot0ff.service.PrepareManuallyService;
import org.bot0ff.service.UserService;
import org.bot0ff.service.game.AutoPrepareService;
import org.bot0ff.service.game.GameMessageService;
import org.bot0ff.service.game.ManuallyPrepareService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

import static org.bot0ff.entity.UserState.ONLINE;
import static org.bot0ff.entity.UserState.PREPARE_MANUALLY;
import static org.bot0ff.service.ServiceCommands.*;

//обрабатывает запросы статуса PREPARE_MANUALLY
@Log4j
@Service
@RequiredArgsConstructor
public class PrepareManuallyServiceImpl implements PrepareManuallyService {
    private final UserService userService;
    private final ManuallyPrepareService manuallyPrepareService;
    private final GameMessageService gameMessageService;

    //ответы на текстовые запросы
    @Override
    public SendMessage optionsPrepareManuallyText(User user, SendMessage sendMessage, String cmd) {
        if(START.equals(cmd)) {
            sendMessage.setText("Идет расстановка кораблей");
            //TODO сделать продолжение текущей расстановки
        }
        else if(CANCEL.equals(cmd)) {
            user.setState(ONLINE);
            userService.saveUser(user);
            sendMessage.setText("""
                    Параметры сброшены.
                    Выберите что хотите сделать""");
            sendMessage.setReplyMarkup(InlineButton.changeOptions());
        }
        else if(HELP.equals(cmd)) {
            sendMessage.setText("Помощь");
        }
        else {
            sendMessage.setText("Идет расстановка кораблей");
            //TODO сделать продолжение текущей расстановки
        }
        return sendMessage;
    }

    //ответы на inline запросы
    @Override
    public SendMessage prepareGameManuallyInline(User user, SendMessage sendMessage, String cmd) {
        if(cmd.equals("startManuallyPrepare")) {
            user.setState(PREPARE_MANUALLY);
            var userGameFiled = manuallyPrepareService.getAutomaticGameFiled();
            user.setGameFiled(userGameFiled);
            userService.saveUser(user);
            sendMessage.setText("Четырех-палубный корабль...\n" + gameMessageService.getEmojiGameFiled(userGameFiled));
            sendMessage.setReplyMarkup(InlineButton.confirmAutomaticPrepare());
        }
        else if(cmd.equals("moveLeft")) {
            user.setGameFiled(moveShip("left", user));
        }
        else if(cmd.equals("moveRight")) {
            user.setGameFiled(moveShip("right", user));
        }
        else if(cmd.equals("rotateShip")) {
            user.setGameFiled(moveShip("rotate", user));
        }
        else if(cmd.equals("moveUp")) {
            user.setGameFiled(moveShip("up", user));
        }
        else if(cmd.equals("moveDown")) {
            user.setGameFiled(moveShip("down", user));
        }
        else if(cmd.equals("setShip")) {

        }
        else if(cmd.equals("cancelShip")) {

        }
        else {
            sendMessage.setText("Идет расстановка кораблей");
            //TODO сделать продолжение текущей расстановки
        }
        return sendMessage;
    }

    public List<String> moveShip(String direction, User user) {

        if(user.getFourDeckShip() < 1) {
            switch (direction) {
                case "left" -> manuallyPrepareService.moveLeft(-1, user.getGameFiled());
                case "right" -> manuallyPrepareService.moveRight(1, user.getGameFiled());
                case "rotate" -> manuallyPrepareService.rotateShip(0, user.getGameFiled());
                case "up" -> manuallyPrepareService.moveUp(-1, user.getGameFiled());
                case "down" -> manuallyPrepareService.moveDown(1, user.getGameFiled());
            }
        }
        else if(user.getThreeDeckShip() < 2) {
            switch (direction) {
                case "left" -> manuallyPrepareService.moveLeft(-1, user.getGameFiled());
                case "right" -> manuallyPrepareService.moveRight(1, user.getGameFiled());
                case "rotate" -> manuallyPrepareService.rotateShip(0, user.getGameFiled());
                case "up" -> manuallyPrepareService.moveUp(-1, user.getGameFiled());
                case "down" -> manuallyPrepareService.moveDown(1, user.getGameFiled());
            }
        }
        else if(user.getTwoDeckShip() < 3) {
            switch (direction) {
                case "left" -> manuallyPrepareService.moveLeft(-1, user.getGameFiled());
                case "right" -> manuallyPrepareService.moveRight(1, user.getGameFiled());
                case "rotate" -> manuallyPrepareService.rotateShip(0, user.getGameFiled());
                case "up" -> manuallyPrepareService.moveUp(-1, user.getGameFiled());
                case "down" -> manuallyPrepareService.moveDown(1, user.getGameFiled());
            }
        }
        else if(user.getOneDeckShip() < 1) {
            switch (direction) {
                case "left" -> manuallyPrepareService.moveLeft(-1, user.getGameFiled());
                case "right" -> manuallyPrepareService.moveRight(1, user.getGameFiled());
                case "rotate" -> manuallyPrepareService.rotateShip(0, user.getGameFiled());
                case "up" -> manuallyPrepareService.moveUp(-1, user.getGameFiled());
                case "down" -> manuallyPrepareService.moveDown(1, user.getGameFiled());
            }
        }
    }
}
