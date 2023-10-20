package org.bot0ff.service.impl;

import lombok.RequiredArgsConstructor;
import org.bot0ff.controller.UpdateController;
import org.bot0ff.entity.User;
import org.bot0ff.entity.UserState;
import org.bot0ff.repository.UserRepository;
import org.bot0ff.service.UserService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User findOrSaveUser(Update update) {
        org.telegram.telegrambots.meta.api.objects.User telegramUser = null;
        if(update.hasMessage()) {
            telegramUser = update.getMessage().getFrom();
        }
        if(update.hasCallbackQuery()) {
            telegramUser = update.getCallbackQuery().getFrom();
        }

        User persistentUser = userRepository.findById(telegramUser.getId()).orElse(null);
        if(persistentUser == null) {
            User newUser = User.builder()
                    .id(telegramUser.getId())
                    .name(telegramUser.getUserName())
                    .registerDate(LocalDateTime.now())
                    .state(UserState.WAIT_REGISTRATION)
                    .build();
            return userRepository.save(newUser);
        }
        return persistentUser;
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }
}
