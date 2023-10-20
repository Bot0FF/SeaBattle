package org.bot0ff.service;

import org.bot0ff.entity.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface RegistrationService {
    SendMessage processRegistration(User user, String cmd, SendMessage sendMessage);
    SendMessage processRegistrationAuto(User user, SendMessage sendMessage);
}
