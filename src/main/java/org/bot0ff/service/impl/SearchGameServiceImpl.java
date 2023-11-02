package org.bot0ff.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bot0ff.entity.User;
import org.bot0ff.service.SearchGameService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

@Log4j
@Service
@RequiredArgsConstructor
public class SearchGameServiceImpl implements SearchGameService {
    @Override
    public User searchGameText(User user, SendMessage sendMessage, String cmd) {
        return null;
    }

    @Override
    public User searchGameInline(User user, EditMessageText editMessageText, AnswerCallbackQuery answerCallbackQuery, String cmd) {
        return null;
    }
}
