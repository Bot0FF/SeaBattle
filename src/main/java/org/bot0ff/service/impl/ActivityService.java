package org.bot0ff.service.impl;

import org.bot0ff.entity.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface ActivityService {
    SendMessage searchGame(User user, SendMessage sendMessage, String cmd);
}
