package org.bot0ff.service;

import org.bot0ff.entity.User;

public interface GameService {
    void prepareGameFiled(User user);
    void startRound(User user);
    void checkEndStep(User user);
    void calculateResult(User user);
}
