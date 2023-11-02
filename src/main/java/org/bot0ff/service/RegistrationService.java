package org.bot0ff.service;

import org.bot0ff.entity.User;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

public interface RegistrationService {
    User processRegistrationText(User user, SendMessage sendMessage, String cmd);
    User processRegistrationInline(User user, EditMessageText editMessageText, AnswerCallbackQuery answerCallbackQuery, String cmd);
}
