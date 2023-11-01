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
        else if(currentCoordinate == 1
                | currentCoordinate == 2
                | currentCoordinate == 3
                | currentCoordinate == 4) {
            removeShip(user, ver, hor);
        }
        return result;
    }

    public int setShip(User user, int ver, int hor) {
        int[][] userFiledArr = ManuallyPrepareService.prepareManuallyMap.get(user.getId()).getUserFiled();

        //проверка максимальной длины корабля
        int countMaxSquare = 0;
        for(int v = 0; v < GAME_FILED_LENGTH; v++) {
            for(int h = 0; h < GAME_FILED_LENGTH; h++) {
                if(userFiledArr[v][h] == 4) {
                    countMaxSquare++;
                }
            }
        }
        if(userFiledArr[ver][hor] == 5 && countMaxSquare >= 4 && (
                (ver > 0 && userFiledArr[ver - 1][hor] == 4)
                | (ver < GAME_FILED_LENGTH - 1 && userFiledArr[ver + 1][hor] == 4)
                | (hor > 0 && userFiledArr[ver][hor - 1] == 4)
                | (hor < GAME_FILED_LENGTH - 1 && userFiledArr[ver][hor + 1] == 4))) {
            return 6;
        }

        userFiledArr[ver][hor] = 1;
        //если рядом с однопалубным выбирается квадрат, меняется на двухпалубный
        if(ver > 0 && userFiledArr[ver - 1][hor] == 1) {
            userFiledArr[ver - 1][hor] = 2;
            userFiledArr[ver][hor] = 2;
        }
        else if(ver < GAME_FILED_LENGTH - 1 && userFiledArr[ver + 1][hor] == 1) {
            userFiledArr[ver + 1][hor] = 2;
            userFiledArr[ver][hor] = 2;
        }
        else if(hor > 0 && userFiledArr[ver][hor - 1] == 1) {
            userFiledArr[ver][hor - 1] = 2;
            userFiledArr[ver][hor] = 2;
        }
        else if(hor < GAME_FILED_LENGTH - 1 && userFiledArr[ver][hor + 1] == 1) {
            userFiledArr[ver][hor + 1] = 2;
            userFiledArr[ver][hor] = 2;
        }

        //если рядом с двухпалубным выбирается квадрат, меняется на трехпалубный
        else if(ver > 1 && userFiledArr[ver - 1][hor] == 2) {
            userFiledArr[ver - 2][hor] = 3;
            userFiledArr[ver - 1][hor] = 3;
            userFiledArr[ver][hor] = 3;
        }
        else if(ver < GAME_FILED_LENGTH - 2 && userFiledArr[ver + 1][hor] == 2) {
            userFiledArr[ver + 2][hor] = 3;
            userFiledArr[ver + 1][hor] = 3;
            userFiledArr[ver][hor] = 3;
        }
        else if(hor > 1 && userFiledArr[ver][hor - 1] == 2) {
            userFiledArr[ver][hor - 2] = 3;
            userFiledArr[ver][hor - 1] = 3;
            userFiledArr[ver][hor] = 3;
        }
        else if(hor < GAME_FILED_LENGTH - 2 && userFiledArr[ver][hor + 1] == 2) {
            userFiledArr[ver][hor + 2] = 3;
            userFiledArr[ver][hor + 1] = 3;
            userFiledArr[ver][hor] = 3;
        }

        //если рядом с трехпалубным выбирается квадрат, меняется на четырехпалубный
        else if(ver > 2 && userFiledArr[ver - 1][hor] == 3) {
            userFiledArr[ver - 3][hor] = 4;
            userFiledArr[ver - 2][hor] = 4;
            userFiledArr[ver - 1][hor] = 4;
            userFiledArr[ver][hor] = 4;
        }
        else if(ver < GAME_FILED_LENGTH - 3 && userFiledArr[ver + 1][hor] == 3) {
            userFiledArr[ver + 3][hor] = 4;
            userFiledArr[ver + 2][hor] = 4;
            userFiledArr[ver + 1][hor] = 4;
            userFiledArr[ver][hor] = 4;
        }
        else if(hor > 2 && userFiledArr[ver][hor - 1] == 3) {
            userFiledArr[ver][hor - 3] = 4;
            userFiledArr[ver][hor - 2] = 4;
            userFiledArr[ver][hor - 1] = 4;
            userFiledArr[ver][hor] = 4;
        }
        else if(hor < GAME_FILED_LENGTH - 3 && userFiledArr[ver][hor + 1] == 3) {
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

        //удаляет верхнюю часть корабля
        if (ver > 0 && (userFiledArr[ver - 1][hor] == 2 | userFiledArr[ver - 1][hor] == 3 | userFiledArr[ver - 1][hor] == 4)) {
            userFiledArr[ver - 1][hor] = 0;
            if (ver > 1 && (userFiledArr[ver - 2][hor] == 3 | userFiledArr[ver - 2][hor] == 4)) {
                userFiledArr[ver - 2][hor] = 0;
                if (ver > 2 && userFiledArr[ver - 3][hor] == 4) {
                    userFiledArr[ver - 3][hor] = 0;
                }
            }
        }

        //удаляет нижнюю часть корабля
        if (ver < GAME_FILED_LENGTH - 1 && (userFiledArr[ver + 1][hor] == 2 | userFiledArr[ver + 1][hor] == 3 | userFiledArr[ver + 1][hor] == 4)) {
            userFiledArr[ver + 1][hor] = 0;
            if (ver < GAME_FILED_LENGTH - 2 && (userFiledArr[ver + 2][hor] == 3 | userFiledArr[ver + 2][hor] == 4)) {
                userFiledArr[ver + 2][hor] = 0;
                if (ver < GAME_FILED_LENGTH - 3 && userFiledArr[ver + 3][hor] == 4) {
                    userFiledArr[ver + 3][hor] = 0;
                }
            }
        }

        //удаляет левую часть корабля
        if (hor > 0 && (userFiledArr[ver][hor - 1] == 2 | userFiledArr[ver][hor - 1] == 3 | userFiledArr[ver][hor - 1] == 4)) {
            userFiledArr[ver][hor - 1] = 0;
            if (hor > 1 && (userFiledArr[ver][hor - 2] == 3 | userFiledArr[ver][hor - 2] == 4)) {
                userFiledArr[ver][hor - 2] = 0;
                if (hor > 2 && userFiledArr[ver][hor - 3] == 4) {
                    userFiledArr[ver][hor - 3] = 0;
                }
            }
        }

        //удаляет правую часть корабля
        if (hor < GAME_FILED_LENGTH - 1 && (userFiledArr[ver][hor + 1] == 2 | userFiledArr[ver][hor + 1] == 3 | userFiledArr[ver][hor + 1] == 4)) {
            userFiledArr[ver][hor + 1] = 0;
            if (hor < GAME_FILED_LENGTH - 2 && (userFiledArr[ver][hor + 2] == 3 | userFiledArr[ver][hor + 2] == 4)) {
                userFiledArr[ver][hor + 2] = 0;
                if (hor < GAME_FILED_LENGTH - 3 && userFiledArr[ver][hor + 3] == 4) {
                    userFiledArr[ver][hor + 3] = 0;
                }
            }
        }

        //удаляет клетки 5 и 6, рядом с которыми нет кораблей
        for(int v = 0; v < GAME_FILED_LENGTH; v++) {
            for(int h = 0; h < GAME_FILED_LENGTH; h++) {
                if(userFiledArr[v][h] == 5 | userFiledArr[v][h] == 6) {
                    if(v > 0 && (userFiledArr[v - 1][h] == 0 | userFiledArr[v - 1][h] == 5 | userFiledArr[v - 1][h] == 6)
                            && (v < GAME_FILED_LENGTH - 1 && (userFiledArr[v + 1][h] == 0 | userFiledArr[v + 1][h] == 5 | userFiledArr[v + 1][h] == 6))
                            && (h > 0 && (userFiledArr[v][h - 1] == 0 | userFiledArr[v][h - 1] == 5 | userFiledArr[v][h - 1] == 6))
                            && (h < GAME_FILED_LENGTH - 1 && (userFiledArr[v][h + 1] == 0 | userFiledArr[v][h + 1] == 5 | userFiledArr[v][h + 1] == 6))
                            && (userFiledArr[v - 1][h - 1] == 0 | userFiledArr[v - 1][h - 1] == 5 | userFiledArr[v - 1][h - 1] == 6)
                            && (userFiledArr[v - 1][h + 1] == 0 | userFiledArr[v - 1][h + 1] == 5 | userFiledArr[v - 1][h + 1] == 6)
                            && (userFiledArr[v + 1][h - 1] == 0 | userFiledArr[v + 1][h - 1] == 5 | userFiledArr[v + 1][h - 1] == 6)
                            && (userFiledArr[v + 1][h + 1] == 0 | userFiledArr[v + 1][h + 1] == 5 | userFiledArr[v + 1][h + 1] == 6)) {
                        userFiledArr[v][h] = 0;
                    }
                    if(v == 0 && (userFiledArr[v + 1][h] == 0 | userFiledArr[v + 1][h] == 5 | userFiledArr[v + 1][h] == 6)
                            && (h > 0 && userFiledArr[v][h - 1] == 0 | userFiledArr[v][h - 1] == 5 | userFiledArr[v][h - 1] == 6)
                            && (h < GAME_FILED_LENGTH - 1 && userFiledArr[v][h + 1] == 0 | userFiledArr[v][h + 1] == 5 | userFiledArr[v][h + 1] == 6)
                            && (userFiledArr[v + 1][h - 1] == 0 | userFiledArr[v + 1][h - 1] == 5 | userFiledArr[v + 1][h - 1] == 6)
                            && (userFiledArr[v + 1][h + 1] == 0 | userFiledArr[v + 1][h + 1] == 5 | userFiledArr[v + 1][h + 1] == 6)) {
                        userFiledArr[v][h] = 0;
                    }
                    if(v == GAME_FILED_LENGTH - 1 && (userFiledArr[v - 1][h] == 0 | userFiledArr[v - 1][h] == 5 | userFiledArr[v - 1][h] == 6)
                            && (h > 0 && userFiledArr[v][h - 1] == 0 | userFiledArr[v][h - 1] == 5 | userFiledArr[v][h - 1] == 6)
                            && (h < GAME_FILED_LENGTH - 1 && userFiledArr[v][h + 1] == 0 | userFiledArr[v][h + 1] == 5 | userFiledArr[v][h + 1] == 6)
                            && (userFiledArr[v - 1][h - 1] == 0 | userFiledArr[v - 1][h - 1] == 5 | userFiledArr[v - 1][h - 1] == 6)
                            && (userFiledArr[v - 1][h + 1] == 0 | userFiledArr[v - 1][h + 1] == 5 | userFiledArr[v - 1][h + 1] == 6)) {
                        userFiledArr[v][h] = 0;
                    }
                    if(h == 0 && (userFiledArr[v][h + 1] == 0 | userFiledArr[v][h + 1] == 5 | userFiledArr[v][h + 1] == 6)
                            && (v > 0 && userFiledArr[v - 1][h] == 0 | userFiledArr[v - 1][h] == 5 | userFiledArr[v - 1][h] == 6)
                            && (v < GAME_FILED_LENGTH - 1 && userFiledArr[v + 1][h] == 0 | userFiledArr[v + 1][h] == 5 | userFiledArr[v + 1][h] == 6)
                            && (userFiledArr[v - 1][h + 1] == 0 | userFiledArr[v - 1][h + 1] == 5 | userFiledArr[v - 1][h + 1] == 6)
                            && (userFiledArr[v + 1][h + 1] == 0 | userFiledArr[v + 1][h + 1] == 5 | userFiledArr[v + 1][h + 1] == 6)) {
                        userFiledArr[v][h] = 0;
                    }
                    if(h == GAME_FILED_LENGTH - 1 && (userFiledArr[v][h - 1] == 0 | userFiledArr[v][h - 1] == 5 | userFiledArr[v][h - 1] == 6)
                            && (v > 0 && userFiledArr[v - 1][h] == 0 | userFiledArr[v - 1][h] == 5 | userFiledArr[v - 1][h] == 6)
                            && (v < GAME_FILED_LENGTH - 1 && userFiledArr[v + 1][h] == 0 | userFiledArr[v + 1][h] == 5 | userFiledArr[v + 1][h] == 6)
                            && (userFiledArr[v - 1][h - 1] == 0 | userFiledArr[v - 1][h - 1] == 5 | userFiledArr[v - 1][h - 1] == 6)
                            && (userFiledArr[v + 1][h - 1] == 0 | userFiledArr[v + 1][h - 1] == 5 | userFiledArr[v + 1][h - 1] == 6)) {
                        userFiledArr[v][h] = 0;
                    }
                    if(h == 0 && v == 0 && (userFiledArr[v + 1][h] == 0 | userFiledArr[v + 1][h] == 5 | userFiledArr[v + 1][h] == 6)
                            && (userFiledArr[v + 1][h + 1] == 0 | userFiledArr[v + 1][h + 1] == 5 | userFiledArr[v + 1][h + 1] == 6)
                            && (userFiledArr[v][h + 1] == 0 | userFiledArr[v][h + 1] == 5 | userFiledArr[v][h + 1] == 6)) {
                        userFiledArr[v][h] = 0;
                    }
                    if(h == 0 && v == GAME_FILED_LENGTH - 1 && (userFiledArr[v - 1][h] == 0 | userFiledArr[v - 1][h] == 5 | userFiledArr[v - 1][h] == 6)
                            && (userFiledArr[v - 1][h + 1] == 0 | userFiledArr[v - 1][h + 1] == 5 | userFiledArr[v - 1][h + 1] == 6)
                            && (userFiledArr[v][h + 1] == 0 | userFiledArr[v][h + 1] == 5 | userFiledArr[v][h + 1] == 6)) {
                        userFiledArr[v][h] = 0;
                    }
                    if(h == GAME_FILED_LENGTH - 1 && v == 0 && (userFiledArr[v + 1][h] == 0 | userFiledArr[v + 1][h] == 5 | userFiledArr[v + 1][h] == 6)
                            && (userFiledArr[v + 1][h - 1] == 0 | userFiledArr[v + 1][h - 1] == 5 | userFiledArr[v + 1][h - 1] == 6)
                            && (userFiledArr[v][h - 1] == 0 | userFiledArr[v][h - 1] == 5 | userFiledArr[v][h - 1] == 6)) {
                        userFiledArr[v][h] = 0;
                    }
                    if(h == GAME_FILED_LENGTH - 1 && v == GAME_FILED_LENGTH - 1 && (userFiledArr[v - 1][h] == 0 | userFiledArr[v - 1][h] == 5 | userFiledArr[v - 1][h] == 6)
                            && (userFiledArr[v - 1][h - 1] == 0 | userFiledArr[v - 1][h - 1] == 5 | userFiledArr[v - 1][h - 1] == 6)
                            && (userFiledArr[v][h - 1] == 0 | userFiledArr[v][h - 1] == 5 | userFiledArr[v][h - 1] == 6)) {
                        userFiledArr[v][h] = 0;
                    }
                }
            }
        }

    }

    public boolean countShips(User user) {
        int[][] userFiledArr = ManuallyPrepareService.prepareManuallyMap.get(user.getId()).getUserFiled();
        var oneDeckShip = 0;
        var twoDeckShip = 0;
        var threeDeckShip = 0;
        var fourDeckShip = 0;

        for(int ver = 0; ver < GAME_FILED_LENGTH; ver++) {
            for(int hor = 0; hor < GAME_FILED_LENGTH; hor++) {
                if(userFiledArr[ver][hor] == 1) {
                    oneDeckShip++;
                }
                else if(userFiledArr[ver][hor] == 2) {
                    twoDeckShip++;
                }
                else if(userFiledArr[ver][hor] == 3) {
                    threeDeckShip++;
                }
                else if(userFiledArr[ver][hor] == 4) {
                    fourDeckShip++;
                }
            }
        }

        return oneDeckShip == 4 && twoDeckShip == 6 && threeDeckShip == 6 && fourDeckShip == 4;
    }
}
