package org.bot0ff.service;

import org.bot0ff.entity.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface InGameService {
    SendMessage processTextMessage(Update update, User user, SendMessage sendMessage, String cmd);
    SendMessage processCallbackQuery(Update update, User user, SendMessage sendMessage, String cmd);
}
