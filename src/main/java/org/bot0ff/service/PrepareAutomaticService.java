package org.bot0ff.service;

import org.bot0ff.entity.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface PrepareAutomaticService {
    SendMessage optionsPrepareAutomaticText(User user, SendMessage sendMessage, String cmd);
    SendMessage prepareGameAutomaticInline(User user, SendMessage sendMessage, String cmd);
}
