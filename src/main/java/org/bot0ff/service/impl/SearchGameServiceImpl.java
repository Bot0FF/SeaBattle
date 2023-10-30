package org.bot0ff.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bot0ff.component.button.InlineButton;
import org.bot0ff.entity.User;
import org.bot0ff.service.SearchGameService;
import org.bot0ff.service.UserService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.bot0ff.entity.UserState.ONLINE;
import static org.bot0ff.entity.UserState.SEARCH_GAME;
import static org.bot0ff.service.ServiceCommands.CANCEL;
import static org.bot0ff.service.ServiceCommands.HELP;

@Log4j
@Service
@RequiredArgsConstructor
public class SearchGameServiceImpl implements SearchGameService {
    private final UserService userService;

    //ответы на текстовые запросы
    @Override
    public User searchGameText(User user, SendMessage sendMessage, String cmd) {
        if(CANCEL.equals(cmd)) {
            user.setState(ONLINE);
            sendMessage.setText("Сброс настроек игры...\nВыберите действие, " + user.getName());
            sendMessage.setReplyMarkup(InlineButton.changeOptions());
        }
        else if(HELP.equals(cmd)) {
            sendMessage.setText("Помощь");
        }
        user.setSendMessage(sendMessage);
        return user;
    }

    @Override
    public User searchGame(User user, SendMessage sendMessage, String cmd) {
        if(cmd.equals("/newGameVsAI")) {
            user.setState(SEARCH_GAME);
            userService.saveUser(user);
            sendMessage.setText("Подготовка сражения с ИИ...");
            sendMessage.setReplyMarkup(InlineButton.stopSearchGameButton());
        }
        else if(cmd.equals("/newGameVsUser")) {
            user.setState(SEARCH_GAME);
            userService.saveUser(user);
            sendMessage.setText("Идет поиск противника...");
            sendMessage.setReplyMarkup(InlineButton.stopSearchGameButton());
        }
        else {
            sendMessage.setText("Выберите противника");
            sendMessage.setReplyMarkup(InlineButton.changeOptions());
        }
        user.setSendMessage(sendMessage);
        return user;
    }

    @Override
    public User stopSearchGame(User user, EditMessageText editMessageText, String cmd) {
        if(cmd.equals("/stopSearchGame")) {
            user.setState(ONLINE);
            userService.saveUser(user);
            editMessageText.setText("""
                    Поиск игры отменен.
                    Выберите противника для новой игры""");
            editMessageText.setReplyMarkup(InlineButton.changeOptions());
        }
        else {
            editMessageText.setText("Идет подготовка сражения...");
            editMessageText.setReplyMarkup(InlineButton.stopSearchGameButton());
        }
        user.setEditMessageText(editMessageText);
        return user;
    }
}
