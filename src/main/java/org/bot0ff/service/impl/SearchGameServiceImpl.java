package org.bot0ff.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bot0ff.component.button.InlineButton;
import org.bot0ff.entity.User;
import org.bot0ff.service.SearchGameService;
import org.bot0ff.service.UserService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.bot0ff.entity.UserState.ONLINE;
import static org.bot0ff.entity.UserState.SEARCH_GAME;

@Log4j
@Service
@RequiredArgsConstructor
public class SearchGameServiceImpl implements SearchGameService {
    private final UserService userService;

    @Override
    public SendMessage searchGame(User user, SendMessage sendMessage, String cmd) {
        if(cmd.equals("/newGameVsAI")) {
            user.setSearch(true);
            user.setState(SEARCH_GAME);
            userService.saveUser(user);
            sendMessage.setText("Подготовка сражения с ИИ...");
            sendMessage.setReplyMarkup(InlineButton.stopSearchGameButton());
        }
        else if(cmd.equals("/newGameVsUser")) {
            user.setSearch(true);
            user.setState(SEARCH_GAME);
            userService.saveUser(user);
            sendMessage.setText("Идет поиск противника...");
            sendMessage.setReplyMarkup(InlineButton.stopSearchGameButton());
        }
        else {
            sendMessage.setText("Выберите противника");
            sendMessage.setReplyMarkup(InlineButton.changeOptions());
        }
        return sendMessage;
    }

    @Override
    public SendMessage stopSearchGame(User user, SendMessage sendMessage, String cmd) {
        if(cmd.equals("/stopSearchGame")) {
            user.setSearch(false);
            user.setState(ONLINE);
            userService.saveUser(user);
            sendMessage.setText("""
                    Поиск игры отменен.
                    Выберите противника для новой игры""");
            sendMessage.setReplyMarkup(InlineButton.changeOptions());
        }
        else {
            sendMessage.setText("Идет подготовка сражения...");
            sendMessage.setReplyMarkup(InlineButton.stopSearchGameButton());
        }
        return sendMessage;
    }
}
