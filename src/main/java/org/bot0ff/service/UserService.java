package org.bot0ff.service;

import org.bot0ff.entity.User;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

public interface UserService {
    Optional<User> findById(Long userId);
    User findOrSaveUser(Update update);
    void saveUser(User user);
}
