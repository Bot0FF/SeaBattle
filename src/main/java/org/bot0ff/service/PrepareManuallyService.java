package org.bot0ff.service;

import org.bot0ff.entity.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface PrepareManuallyService {
    SendMessage optionsPrepareManuallyText(User user, SendMessage sendMessage, String cmd);
    SendMessage prepareGameManuallyInline(User user, SendMessage sendMessage, String cmd);
}
