package org.bot0ff.service.game;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.bot0ff.entity.User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
@Component
public class ActiveGames {
    public final Map<Long, Map<User, User>> activeGame = Collections.synchronizedMap(new HashMap<>());

    public void addUsers(User userOne, User userTwo) {
        activeGame.put(userOne.getGameId(), new HashMap<>(Map.of(userOne, userOne)));
    }

    public void removeUsers(Long gameId) {
        activeGame.remove(gameId);
    }
}
