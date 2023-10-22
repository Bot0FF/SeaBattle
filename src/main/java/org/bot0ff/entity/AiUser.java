package org.bot0ff.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
@AllArgsConstructor
public class AiUser extends User{

    @Override
    public void setGameId(Long gameId) {
        super.setGameId(gameId);
    }

    @Override
    public void setGameFiled(List<String> gameFiled) {
        super.setGameFiled(gameFiled);
    }
}
