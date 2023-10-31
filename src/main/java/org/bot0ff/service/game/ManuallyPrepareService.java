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

    public int setUserFiled(User user, String cmd) {
        int result = 0;
        int[][] userFiledArr = ManuallyPrepareService.prepareManuallyMap.get(user.getId()).getUserFiled();
        String[] split = cmd.split(":");
        int ver = Integer.parseInt(split[0]);
        int hor = Integer.parseInt(split[1]);
        int currentCoordinate = userFiledArr[ver][hor];

        if(currentCoordinate == 0 | currentCoordinate == 5) {
            result = setShip(user, ver, hor);
        }
        else if(currentCoordinate == 1) {
            removeShip(user, ver, hor);
        }
        return result;
    }

    public int setShip(User user, int ver, int hor) {
        int[][] userFiledArr = ManuallyPrepareService.prepareManuallyMap.get(user.getId()).getUserFiled();

        //при попытке установки длины корабля 5 выходит из метода
        if(ver > 3 && userFiledArr[ver - 4][hor] == 4) {
            return 6;
        }
        if(ver < GAME_FILED_LENGTH - 4 && userFiledArr[ver + 4][hor] == 4) {
            return 6;
        }
        if(hor > 3 && userFiledArr[ver][hor - 4] == 4) {
            return 6;
        }
        if(hor < GAME_FILED_LENGTH - 4 && userFiledArr[ver][hor + 4] == 4) {
            return 6;
        }

        userFiledArr[ver][hor] = 1;
        //если рядом с однопалубным выбирается квадрат, меняется на двухпалубный
        if(ver > 0 && userFiledArr[ver - 1][hor] == 1) {
            userFiledArr[ver - 1][hor] = 2;
            userFiledArr[ver][hor] = 2;
        }
        if(ver < GAME_FILED_LENGTH - 1 && userFiledArr[ver + 1][hor] == 1) {
            userFiledArr[ver + 1][hor] = 2;
            userFiledArr[ver][hor] = 2;
        }
        if(hor > 0 && userFiledArr[ver][hor - 1] == 1) {
            userFiledArr[ver][hor - 1] = 2;
            userFiledArr[ver][hor] = 2;
        }
        if(hor < GAME_FILED_LENGTH - 1 && userFiledArr[ver][hor + 1] == 1) {
            userFiledArr[ver][hor + 1] = 2;
            userFiledArr[ver][hor] = 2;
        }

        //если рядом с двухпалубным выбирается квадрат, меняется на трехпалубный
        if(ver > 1 && userFiledArr[ver - 2][hor] == 2) {
            userFiledArr[ver - 2][hor] = 3;
            userFiledArr[ver - 1][hor] = 3;
            userFiledArr[ver][hor] = 3;
        }
        if(ver < GAME_FILED_LENGTH - 2 && userFiledArr[ver + 2][hor] == 2) {
            userFiledArr[ver + 2][hor] = 3;
            userFiledArr[ver + 1][hor] = 3;
            userFiledArr[ver][hor] = 3;
        }
        if(hor > 1 && userFiledArr[ver][hor - 2] == 2) {
            userFiledArr[ver][hor - 2] = 3;
            userFiledArr[ver][hor - 1] = 3;
            userFiledArr[ver][hor] = 3;
        }
        if(hor < GAME_FILED_LENGTH - 2 && userFiledArr[ver][hor + 2] == 2) {
            userFiledArr[ver][hor + 2] = 3;
            userFiledArr[ver][hor + 1] = 3;
            userFiledArr[ver][hor] = 3;
        }

        //если рядом с трехпалубным выбирается квадрат, меняется на четырехпалубный
        if(ver > 2 && userFiledArr[ver - 3][hor] == 3) {
            userFiledArr[ver - 3][hor] = 4;
            userFiledArr[ver - 2][hor] = 4;
            userFiledArr[ver - 1][hor] = 4;
            userFiledArr[ver][hor] = 4;
        }
        if(ver < GAME_FILED_LENGTH - 3 && userFiledArr[ver + 3][hor] == 3) {
            userFiledArr[ver + 3][hor] = 4;
            userFiledArr[ver + 2][hor] = 4;
            userFiledArr[ver + 1][hor] = 4;
            userFiledArr[ver][hor] = 4;
        }
        if(hor > 2 && userFiledArr[ver][hor - 3] == 3) {
            userFiledArr[ver][hor - 3] = 4;
            userFiledArr[ver][hor - 2] = 4;
            userFiledArr[ver][hor - 1] = 4;
            userFiledArr[ver][hor] = 4;
        }
        if(hor < GAME_FILED_LENGTH - 3 && userFiledArr[ver][hor + 3] == 3) {
            userFiledArr[ver][hor + 3] = 4;
            userFiledArr[ver][hor + 2] = 4;
            userFiledArr[ver][hor + 1] = 4;
            userFiledArr[ver][hor] = 4;
        }

        //отмечаем клетки вокруг корабля
        if(ver > 0 && userFiledArr[(ver - 1)][hor] == 0) {
            userFiledArr[(ver - 1)][hor] = 5; //клетка над кораблем
        }
        if(ver < GAME_FILED_LENGTH - 1 && userFiledArr[ver + 1][hor] == 0) {
            userFiledArr[(ver + 1)][hor] = 5; //клетка под кораблем
        }
        if(hor > 0 && userFiledArr[ver][hor - 1] == 0) {
            userFiledArr[(ver)][hor - 1] = 5; //клетка слева от корабля
        }
        if(hor < GAME_FILED_LENGTH - 1  && userFiledArr[ver][hor + 1] == 0) {
            userFiledArr[ver][hor + 1] = 5; //клетка справа от корабля
        }

        //отмечаем клетки по углам корабля
        if(ver > 0 & hor > 0) {
            userFiledArr[(ver - 1)][(hor - 1)] = 6; //клетка слева сверху
        }
        if(ver > 0 & hor < (GAME_FILED_LENGTH - 1)) {
            userFiledArr[(ver - 1)][(hor + 1)] = 6; //клетка справа сверху
        }
        if(ver < (GAME_FILED_LENGTH - 1) & hor > 0) {
            userFiledArr[(ver + 1)][(hor - 1)] = 6; //клетка слева снизу
        }
        if(ver < (GAME_FILED_LENGTH - 1) & hor < (GAME_FILED_LENGTH - 1)) {
            userFiledArr[(ver + 1)][(hor + 1)] = 6; //клетка справа снизу
        }
        return 0;
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

    public boolean countShip(User user) {
        int[][] userFiledArr = ManuallyPrepareService.prepareManuallyMap.get(user.getId()).getUserFiled();
        var result = 0;

        for(int ver = 0; ver < GAME_FILED_LENGTH; ver++) {
            for(int hor = 0; hor < GAME_FILED_LENGTH; hor++) {
                if(userFiledArr[ver][hor] == 1) {
                    boolean isNewShip = checkIsNewShip(userFiledArr, ver, hor);
                    if(isNewShip) {
                        result++;
                    }
                }
            }
        }

        return result == 10;
    }

    private boolean checkIsNewShip(int[][] userFiled, int ver, int hor) {
        return !((ver > 0 && userFiled[ver - 1][hor] == 1)
                | (hor > 0 && userFiled[ver][hor - 1] == 1));
    }

    public boolean countSquare(User user) {
        int[][] userFiledArr = ManuallyPrepareService.prepareManuallyMap.get(user.getId()).getUserFiled();
        var result = 0;

        for(int ver = 0; ver < GAME_FILED_LENGTH; ver++) {
            for(int hor = 0; hor < GAME_FILED_LENGTH; hor++) {
                if(userFiledArr[ver][hor] == 1) {
                    result++;
                }
            }
        }

        return result == 20;
    }
}
