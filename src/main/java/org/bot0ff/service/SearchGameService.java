package org.bot0ff.service;

import org.bot0ff.entity.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface SearchGameService {
    SendMessage searchGame(User user, SendMessage sendMessage, String cmd);
    SendMessage stopSearchGame(User user, SendMessage sendMessage, String cmd);
}
