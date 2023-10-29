package org.bot0ff.service;

import org.bot0ff.entity.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

public interface PrepareAutoService {
    User optionsPrepareAutomaticText(User user, SendMessage sendMessage, String cmd);
    User prepareGameAutomaticInline(User user, EditMessageText editMessageText, String cmd);
}
