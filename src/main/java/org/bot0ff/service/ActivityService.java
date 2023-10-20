package org.bot0ff.service;

import org.bot0ff.entity.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface ActivityService {
    SendMessage changeOptionsFromMenu(User user, SendMessage sendMessage, String cmd);
    SendMessage changeOptions(User user, SendMessage sendMessage, String cmd);
}
