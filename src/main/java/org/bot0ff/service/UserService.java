package org.bot0ff.service;

import org.bot0ff.entity.User;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UserService {
    User findOrSaveUser(Update update);
    void saveUser(User user);
}
