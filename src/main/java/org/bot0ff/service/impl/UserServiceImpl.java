package org.bot0ff.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bot0ff.entity.User;
import org.bot0ff.entity.UserState;
import org.bot0ff.repository.UserRepository;
import org.bot0ff.service.UserService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

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
                    .name(telegramUser.getFirstName())
                    .registerDate(LocalDateTime.now())
                    .state(UserState.WAIT_REGISTRATION)
                    .userGameFiled(new ArrayList<>())
                    .opponentGameFiled(new ArrayList<>())
                    .opponentId(0L)
                    .isActive(false)
                    .countVictory(0)
                    .countLoss(0)
                    .build();
            log.info("Новый пользователь: " + newUser);
            return userRepository.save(newUser);
        }
        return persistentUser;
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }
}
