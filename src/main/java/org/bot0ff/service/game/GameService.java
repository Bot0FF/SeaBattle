package org.bot0ff.service.game;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bot0ff.entity.User;
import org.bot0ff.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Arrays;

import static org.bot0ff.util.Constants.GAME_FILED_LENGTH;

@Log4j
@Service
@RequiredArgsConstructor
public class GameService {
    private final UserService userService;
    private final GameFiledService gameFiledService;

    //проверяет попадание user
    public int checkUserStep(String userTarget, User user) {
        int result;
        String[] split = userTarget.split(":");
        int[][] opponentGameFiled = gameFiledService.convertListFiledToArr(user.getOpponentGameFiled());
        int targetVer = Integer.parseInt(split[0]);
        int targetHor = Integer.parseInt(split[1]);

        if(opponentGameFiled[targetVer][targetHor] == 1
                | opponentGameFiled[targetVer][targetHor] == 2
                | opponentGameFiled[targetVer][targetHor] == 3
                | opponentGameFiled[targetVer][targetHor] == 4) {
            opponentGameFiled[targetVer][targetHor] = -1;
            user.setOpponentGameFiled(gameFiledService.convertArrFiledToList(opponentGameFiled));
            result = 1;
        }
        else {
            opponentGameFiled[targetVer][targetHor] = -2;
            user.setOpponentGameFiled(gameFiledService.convertArrFiledToList(opponentGameFiled));
            result = 0;
        }

        //проверка наличия не подбитых кораблей opponent
        int countShips = 0;
        for(int ver = 0; ver < GAME_FILED_LENGTH; ver++) {
            for(int hor = 0; hor < GAME_FILED_LENGTH; hor++) {
                if(opponentGameFiled[ver][hor] == 1
                        | opponentGameFiled[ver][hor] == 2
                        | opponentGameFiled[ver][hor] == 3
                        | opponentGameFiled[ver][hor] == 4) {
                    countShips++;
                }
            }
        }
        if(countShips < 1) {
            return -1;
        }

        userService.saveUser(user);
        return result;
    }
}
