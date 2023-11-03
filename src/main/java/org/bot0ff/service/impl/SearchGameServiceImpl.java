package org.bot0ff.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bot0ff.component.button.InlineButton;
import org.bot0ff.controller.JoinUserController;
import org.bot0ff.entity.User;
import org.bot0ff.service.SearchGameService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import static org.bot0ff.entity.UserState.ONLINE;
import static org.bot0ff.service.ServiceCommands.CANCEL;

@Log4j
@Service
@RequiredArgsConstructor
public class SearchGameServiceImpl implements SearchGameService {
    @Override
    public User searchGameText(User user, SendMessage sendMessage, String cmd) {
//        if(CANCEL.equals(cmd)) {
//            user.setState(ONLINE);
//            JoinUserController.removeUserFromMap(user);
//            sendMessage.setText("Сброс настроек игры...\nВыберите действие, " + user.getName());
//            sendMessage.setReplyMarkup(InlineButton.changeOptions());
//            user.setSendMessage(sendMessage);
//        }
        return user;
    }

    @Override
    public User searchGameInline(User user, EditMessageText editMessageText, AnswerCallbackQuery answerCallbackQuery, String cmd) {
        user.setAnswerCallbackQuery(answerCallbackQuery);
        return user;
    }
}
