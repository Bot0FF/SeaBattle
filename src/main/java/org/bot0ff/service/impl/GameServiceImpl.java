package org.bot0ff.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bot0ff.entity.User;
import org.bot0ff.service.GameService;
import org.springframework.stereotype.Service;

@Log4j
@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
    @Override
    public void prepareGameFiled(User user) {

    }

    @Override
    public void startRound(User user) {

    }

    @Override
    public void checkEndStep(User user) {

    }

    @Override
    public void calculateResult(User user) {

    }
}
