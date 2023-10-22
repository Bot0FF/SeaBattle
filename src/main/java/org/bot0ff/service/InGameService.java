package org.bot0ff.service;

import org.bot0ff.entity.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface InGameService {
    SendMessage processTextMessage(User user, SendMessage sendMessage, String cmd);
    SendMessage processCallbackQuery(User user, SendMessage sendMessage, String cmd);
}
