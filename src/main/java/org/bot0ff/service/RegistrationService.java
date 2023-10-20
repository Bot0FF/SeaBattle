package org.bot0ff.service;

import org.bot0ff.entity.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface RegistrationService {
    SendMessage processRegistrationText(User user, SendMessage sendMessage, String cmd);
    SendMessage processRegistrationInline(User user, SendMessage sendMessage, String cmd);
}
