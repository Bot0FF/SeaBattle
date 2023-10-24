package org.bot0ff.service.game;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.bot0ff.entity.AiUser;
import org.bot0ff.entity.User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
@Component
public class ActiveGames {
    public static final Map<Long, AiUser> currentGames = Collections.synchronizedMap(new HashMap<>());

    public void addUsers(AiUser aiUser) {
        currentGames.put(aiUser.getGameId(), aiUser);
    }

    public void removeUsers(Long gameId) {
        currentGames.remove(gameId);
    }

    public AiUser getAiUser(Long gameId) {
        return currentGames.get(gameId);
    }
}
