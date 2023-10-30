package org.bot0ff.service.game;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bot0ff.entity.User;
import org.bot0ff.entity.UserFiled;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.bot0ff.util.Constants.GAME_FILED_LENGTH;

@Log4j
@Service
@RequiredArgsConstructor
public class ManuallyPrepareService {
    public static Map<Long, UserFiled> prepareManuallyMap = Collections.synchronizedMap(new HashMap<>());

    public void setUserFiled(User user, String cmd) {
        UserFiled userFiled = ManuallyPrepareService.prepareManuallyMap.get(user.getId());
        int[][] userFiledArr = ManuallyPrepareService.prepareManuallyMap.get(user.getId()).getUserFiled();
        String[] split = cmd.split(":");
        int ver = Integer.parseInt(split[0]);
        int hor = Integer.parseInt(split[1]);
        int currentCoordinate = userFiledArr[ver][hor];

        if(currentCoordinate == 0) {
            setShip(user, ver, hor);
        }
        else if(currentCoordinate == 1) {
            removeShip(user, ver, hor);
        }
    }

    public void setShip(User user, int ver, int hor) {
        int[][] userFiledArr = ManuallyPrepareService.prepareManuallyMap.get(user.getId()).getUserFiled();
        userFiledArr[ver][hor] = 1;
//        if(ver > 0) {
//            userFiledArr[(ver - 1)][hor] = 2; //клетка над кораблем
//        }
//        if(hor > 0) {
//            userFiledArr[(ver)][(hor - 1)] = 2; //клетка слева от корабля
//        }
//        if(hor < GAME_FILED_LENGTH) {
//            userFiledArr[ver][(hor + 1)] = 2; //клетка справа от корабля
//        }
//        if(ver < GAME_FILED_LENGTH) {
//            userFiledArr[(ver + 1)][hor] = 2; //клетка под кораблем
//        }
        if(ver > 0 & hor > 0) {
            userFiledArr[(ver - 1)][(hor - 1)] = 2; //клетка слева сверху
        }
        if(ver > 0 & hor < (GAME_FILED_LENGTH - 1)) {
            userFiledArr[(ver - 1)][(hor + 1)] = 2; //клетка справа сверху
        }
        if(ver < (GAME_FILED_LENGTH - 1) & hor > 0) {
            userFiledArr[(ver + 1)][(hor - 1)] = 2; //клетка слева снизу
        }
        if(ver < (GAME_FILED_LENGTH - 1) & hor < (GAME_FILED_LENGTH - 1)) {
            userFiledArr[(ver + 1)][(hor + 1)] = 2; //клетка справа снизу
        }

    }

    public void removeShip(User user, int ver, int hor) {
        int[][] userFiledArr = ManuallyPrepareService.prepareManuallyMap.get(user.getId()).getUserFiled();
        userFiledArr[ver][hor] = 0;
//        if(ver > 0) {
//            userFiledArr[(ver - 1)][hor] = 0; //клетка над кораблем
//        }
//        if(hor > 0) {
//            userFiledArr[(ver)][(hor - 1)] = 0; //клетка слева от корабля
//        }
//        if(hor < GAME_FILED_LENGTH) {
//            userFiledArr[ver][(hor + 1)] = 0; //клетка справа от корабля
//        }
//        if(ver < GAME_FILED_LENGTH) {
//            userFiledArr[(ver + 1)][hor] = 0; //клетка под кораблем
//        }
        if(ver > 0 & hor > 0) {
            userFiledArr[(ver - 1)][(hor - 1)] = 0; //клетка слева сверху
        }
        if(ver > 0 & hor < (GAME_FILED_LENGTH - 1)) {
            userFiledArr[(ver - 1)][(hor + 1)] = 0; //клетка справа сверху
        }
        if(ver < (GAME_FILED_LENGTH - 1) & hor > 0) {
            userFiledArr[(ver + 1)][(hor - 1)] = 0; //клетка слева снизу
        }
        if(ver < (GAME_FILED_LENGTH - 1) & hor < (GAME_FILED_LENGTH - 1)) {
            userFiledArr[(ver + 1)][(hor + 1)] = 0; //клетка справа снизу
        }
    }
}
