package org.bot0ff.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bot0ff.component.button.InlineButton;
import org.bot0ff.entity.User;
import org.bot0ff.service.PrepareAutomaticService;
import org.bot0ff.service.UserService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.bot0ff.entity.UserState.ONLINE;
import static org.bot0ff.entity.UserState.SEARCH_GAME;
import static org.bot0ff.service.ServiceCommands.CANCEL;

@Log4j
@Service
@RequiredArgsConstructor
public class PrepareAutomaticServiceImpl implements PrepareAutomaticService {
    private final UserService userService;

    @Override
    public SendMessage optionsPrepareAutomaticText(User user, SendMessage sendMessage, String cmd) {
        if(CANCEL.equals(cmd)) {
            user.setState(ONLINE);
            userService.saveUser(user);
            sendMessage.setText("""
                    Параметры сброшены.
                    Выберите что хотите сделать""");
            sendMessage.setReplyMarkup(InlineButton.changeOptions());
        }
        else {
            sendMessage.setText("Идет автоматическая расстановка кораблей...");
        }
        return sendMessage;
    }

    @Override
    public SendMessage prepareGameAutomaticInline(User user, SendMessage sendMessage, String cmd) {
        if(cmd.equals("/confirmAutomaticPrepare")) {
            user.setState(SEARCH_GAME);
            userService.saveUser(user);
            sendMessage.setText("Идет поиск противника");
        }
        else if(cmd.equals("/updateAutomaticPrepare")) {
            sendMessage.setText("Сохранить результат расстановки? \n++++++");
            sendMessage.setReplyMarkup(InlineButton.confirmAutomaticPrepare());
        }
        else {
            sendMessage.setText("Идет автоматическая расстановка кораблей...");
        }
        return sendMessage;
    }
}
