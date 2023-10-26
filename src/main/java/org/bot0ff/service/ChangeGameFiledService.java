package org.bot0ff.service;

import org.bot0ff.entity.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface ChangeGameFiledService {
    SendMessage optionsPrepareGameText(User user, SendMessage sendMessage, String cmd);
    SendMessage optionsPrepareGameInline(User user, SendMessage sendMessage, String cmd);
}
