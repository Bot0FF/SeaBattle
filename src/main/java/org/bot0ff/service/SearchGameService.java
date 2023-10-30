package org.bot0ff.service;

import org.bot0ff.entity.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

public interface SearchGameService {
    User searchGameText(User user, SendMessage sendMessage, String cmd);
    User searchGame(User user, SendMessage sendMessage, String cmd);
    User stopSearchGame(User user, EditMessageText editMessageText, String cmd);
}
