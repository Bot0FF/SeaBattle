package org.bot0ff.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bot0ff.component.button.InlineButton;
import org.bot0ff.entity.User;
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
            user.setState(SEARCH_GAME);
            userService.saveUser(user);
            sendMessage.setText("Подготовка сражения с ИИ...");
        }
        else if(cmd.equals("/newGameVsUser")) {
            user.setState(ONLINE);
            userService.saveUser(user);
            sendMessage.setText("Идет поиск противника...");
        }
        else {
            sendMessage.setText("Выберите противника");
            sendMessage.setReplyMarkup(InlineButton.startNewGameButton());
        }
        return sendMessage;
    }
}
