package org.bot0ff.service.game;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bot0ff.entity.User;
import org.springframework.stereotype.Service;

import static org.bot0ff.util.Constants.GAME_FILED_LENGTH;

@Log4j
@Service
@RequiredArgsConstructor
public class ManuallyPrepareService {

    public int setUserGameFiled(User user, String cmd) {
        int result = 0;
        int[][] userFiledArr = GameFiledService.prepareUserFiledMap.get(user.getId());
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
        int[][] userGameFiledArr = GameFiledService.prepareUserFiledMap.get(user.getId());

        //проверка максимальной длины корабля
        int countMaxSquare = 0;
        for(int v = 0; v < GAME_FILED_LENGTH; v++) {
            for(int h = 0; h < GAME_FILED_LENGTH; h++) {
                if(userGameFiledArr[v][h] == 4) {
                    countMaxSquare++;
                }
            }
        }
        if(userGameFiledArr[ver][hor] == 5 && countMaxSquare >= 4 && (
                (ver > 0 && userGameFiledArr[ver - 1][hor] == 4)
                | (ver < GAME_FILED_LENGTH - 1 && userGameFiledArr[ver + 1][hor] == 4)
                | (hor > 0 && userGameFiledArr[ver][hor - 1] == 4)
                | (hor < GAME_FILED_LENGTH - 1 && userGameFiledArr[ver][hor + 1] == 4))) {
            return 6;
        }

        userGameFiledArr[ver][hor] = 1;
        //если рядом с однопалубным выбирается квадрат, меняется на двухпалубный
        if(ver > 0 && userGameFiledArr[ver - 1][hor] == 1) {
            userGameFiledArr[ver - 1][hor] = 2;
            userGameFiledArr[ver][hor] = 2;
        }
        else if(ver < GAME_FILED_LENGTH - 1 && userGameFiledArr[ver + 1][hor] == 1) {
            userGameFiledArr[ver + 1][hor] = 2;
            userGameFiledArr[ver][hor] = 2;
        }
        else if(hor > 0 && userGameFiledArr[ver][hor - 1] == 1) {
            userGameFiledArr[ver][hor - 1] = 2;
            userGameFiledArr[ver][hor] = 2;
        }
        else if(hor < GAME_FILED_LENGTH - 1 && userGameFiledArr[ver][hor + 1] == 1) {
            userGameFiledArr[ver][hor + 1] = 2;
            userGameFiledArr[ver][hor] = 2;
        }

        //если рядом с двухпалубным выбирается квадрат, меняется на трехпалубный
        else if(ver > 1 && userGameFiledArr[ver - 1][hor] == 2) {
            userGameFiledArr[ver - 2][hor] = 3;
            userGameFiledArr[ver - 1][hor] = 3;
            userGameFiledArr[ver][hor] = 3;
        }
        else if(ver < GAME_FILED_LENGTH - 2 && userGameFiledArr[ver + 1][hor] == 2) {
            userGameFiledArr[ver + 2][hor] = 3;
            userGameFiledArr[ver + 1][hor] = 3;
            userGameFiledArr[ver][hor] = 3;
        }
        else if(hor > 1 && userGameFiledArr[ver][hor - 1] == 2) {
            userGameFiledArr[ver][hor - 2] = 3;
            userGameFiledArr[ver][hor - 1] = 3;
            userGameFiledArr[ver][hor] = 3;
        }
        else if(hor < GAME_FILED_LENGTH - 2 && userGameFiledArr[ver][hor + 1] == 2) {
            userGameFiledArr[ver][hor + 2] = 3;
            userGameFiledArr[ver][hor + 1] = 3;
            userGameFiledArr[ver][hor] = 3;
        }

        //если рядом с трехпалубным выбирается квадрат, меняется на четырехпалубный
        else if(ver > 2 && userGameFiledArr[ver - 1][hor] == 3) {
            userGameFiledArr[ver - 3][hor] = 4;
            userGameFiledArr[ver - 2][hor] = 4;
            userGameFiledArr[ver - 1][hor] = 4;
            userGameFiledArr[ver][hor] = 4;
        }
        else if(ver < GAME_FILED_LENGTH - 3 && userGameFiledArr[ver + 1][hor] == 3) {
            userGameFiledArr[ver + 3][hor] = 4;
            userGameFiledArr[ver + 2][hor] = 4;
            userGameFiledArr[ver + 1][hor] = 4;
            userGameFiledArr[ver][hor] = 4;
        }
        else if(hor > 2 && userGameFiledArr[ver][hor - 1] == 3) {
            userGameFiledArr[ver][hor - 3] = 4;
            userGameFiledArr[ver][hor - 2] = 4;
            userGameFiledArr[ver][hor - 1] = 4;
            userGameFiledArr[ver][hor] = 4;
        }
        else if(hor < GAME_FILED_LENGTH - 3 && userGameFiledArr[ver][hor + 1] == 3) {
            userGameFiledArr[ver][hor + 3] = 4;
            userGameFiledArr[ver][hor + 2] = 4;
            userGameFiledArr[ver][hor + 1] = 4;
            userGameFiledArr[ver][hor] = 4;
        }

        //отмечаем клетки вокруг корабля
        if(ver > 0 && userGameFiledArr[(ver - 1)][hor] == 0) {
            userGameFiledArr[(ver - 1)][hor] = 5; //клетка над кораблем
        }
        if(ver < GAME_FILED_LENGTH - 1 && userGameFiledArr[ver + 1][hor] == 0) {
            userGameFiledArr[(ver + 1)][hor] = 5; //клетка под кораблем
        }
        if(hor > 0 && userGameFiledArr[ver][hor - 1] == 0) {
            userGameFiledArr[(ver)][hor - 1] = 5; //клетка слева от корабля
        }
        if(hor < GAME_FILED_LENGTH - 1  && userGameFiledArr[ver][hor + 1] == 0) {
            userGameFiledArr[ver][hor + 1] = 5; //клетка справа от корабля
        }

        //отмечаем клетки по углам корабля
        if(ver > 0 & hor > 0) {
            userGameFiledArr[(ver - 1)][(hor - 1)] = 6; //клетка слева сверху
        }
        if(ver > 0 & hor < (GAME_FILED_LENGTH - 1)) {
            userGameFiledArr[(ver - 1)][(hor + 1)] = 6; //клетка справа сверху
        }
        if(ver < (GAME_FILED_LENGTH - 1) & hor > 0) {
            userGameFiledArr[(ver + 1)][(hor - 1)] = 6; //клетка слева снизу
        }
        if(ver < (GAME_FILED_LENGTH - 1) & hor < (GAME_FILED_LENGTH - 1)) {
            userGameFiledArr[(ver + 1)][(hor + 1)] = 6; //клетка справа снизу
        }
        return 0;
    }

    public void removeShip(User user, int ver, int hor) {
        int[][] userGameFiledArr = GameFiledService.prepareUserFiledMap.get(user.getId());
        userGameFiledArr[ver][hor] = 0;

        //удаляет верхнюю часть корабля
        if (ver > 0 && (userGameFiledArr[ver - 1][hor] == 2 | userGameFiledArr[ver - 1][hor] == 3 | userGameFiledArr[ver - 1][hor] == 4)) {
            userGameFiledArr[ver - 1][hor] = 0;
            if (ver > 1 && (userGameFiledArr[ver - 2][hor] == 3 | userGameFiledArr[ver - 2][hor] == 4)) {
                userGameFiledArr[ver - 2][hor] = 0;
                if (ver > 2 && userGameFiledArr[ver - 3][hor] == 4) {
                    userGameFiledArr[ver - 3][hor] = 0;
                }
            }
        }

        //удаляет нижнюю часть корабля
        if (ver < GAME_FILED_LENGTH - 1 && (userGameFiledArr[ver + 1][hor] == 2 | userGameFiledArr[ver + 1][hor] == 3 | userGameFiledArr[ver + 1][hor] == 4)) {
            userGameFiledArr[ver + 1][hor] = 0;
            if (ver < GAME_FILED_LENGTH - 2 && (userGameFiledArr[ver + 2][hor] == 3 | userGameFiledArr[ver + 2][hor] == 4)) {
                userGameFiledArr[ver + 2][hor] = 0;
                if (ver < GAME_FILED_LENGTH - 3 && userGameFiledArr[ver + 3][hor] == 4) {
                    userGameFiledArr[ver + 3][hor] = 0;
                }
            }
        }

        //удаляет левую часть корабля
        if (hor > 0 && (userGameFiledArr[ver][hor - 1] == 2 | userGameFiledArr[ver][hor - 1] == 3 | userGameFiledArr[ver][hor - 1] == 4)) {
            userGameFiledArr[ver][hor - 1] = 0;
            if (hor > 1 && (userGameFiledArr[ver][hor - 2] == 3 | userGameFiledArr[ver][hor - 2] == 4)) {
                userGameFiledArr[ver][hor - 2] = 0;
                if (hor > 2 && userGameFiledArr[ver][hor - 3] == 4) {
                    userGameFiledArr[ver][hor - 3] = 0;
                }
            }
        }

        //удаляет правую часть корабля
        if (hor < GAME_FILED_LENGTH - 1 && (userGameFiledArr[ver][hor + 1] == 2 | userGameFiledArr[ver][hor + 1] == 3 | userGameFiledArr[ver][hor + 1] == 4)) {
            userGameFiledArr[ver][hor + 1] = 0;
            if (hor < GAME_FILED_LENGTH - 2 && (userGameFiledArr[ver][hor + 2] == 3 | userGameFiledArr[ver][hor + 2] == 4)) {
                userGameFiledArr[ver][hor + 2] = 0;
                if (hor < GAME_FILED_LENGTH - 3 && userGameFiledArr[ver][hor + 3] == 4) {
                    userGameFiledArr[ver][hor + 3] = 0;
                }
            }
        }

        //удаляет клетки 5 и 6, рядом с которыми нет кораблей
        for(int v = 0; v < GAME_FILED_LENGTH; v++) {
            for(int h = 0; h < GAME_FILED_LENGTH; h++) {
                if(userGameFiledArr[v][h] == 5 | userGameFiledArr[v][h] == 6) {
                    if(v > 0 && (userGameFiledArr[v - 1][h] == 0 | userGameFiledArr[v - 1][h] == 5 | userGameFiledArr[v - 1][h] == 6)
                            && (v < GAME_FILED_LENGTH - 1 && (userGameFiledArr[v + 1][h] == 0 | userGameFiledArr[v + 1][h] == 5 | userGameFiledArr[v + 1][h] == 6))
                            && (h > 0 && (userGameFiledArr[v][h - 1] == 0 | userGameFiledArr[v][h - 1] == 5 | userGameFiledArr[v][h - 1] == 6))
                            && (h < GAME_FILED_LENGTH - 1 && (userGameFiledArr[v][h + 1] == 0 | userGameFiledArr[v][h + 1] == 5 | userGameFiledArr[v][h + 1] == 6))
                            && (userGameFiledArr[v - 1][h - 1] == 0 | userGameFiledArr[v - 1][h - 1] == 5 | userGameFiledArr[v - 1][h - 1] == 6)
                            && (userGameFiledArr[v - 1][h + 1] == 0 | userGameFiledArr[v - 1][h + 1] == 5 | userGameFiledArr[v - 1][h + 1] == 6)
                            && (userGameFiledArr[v + 1][h - 1] == 0 | userGameFiledArr[v + 1][h - 1] == 5 | userGameFiledArr[v + 1][h - 1] == 6)
                            && (userGameFiledArr[v + 1][h + 1] == 0 | userGameFiledArr[v + 1][h + 1] == 5 | userGameFiledArr[v + 1][h + 1] == 6)) {
                        userGameFiledArr[v][h] = 0;
                    }
                    if(v == 0 && (userGameFiledArr[v + 1][h] == 0 | userGameFiledArr[v + 1][h] == 5 | userGameFiledArr[v + 1][h] == 6)
                            && (h > 0 && userGameFiledArr[v][h - 1] == 0 | userGameFiledArr[v][h - 1] == 5 | userGameFiledArr[v][h - 1] == 6)
                            && (h < GAME_FILED_LENGTH - 1 && userGameFiledArr[v][h + 1] == 0 | userGameFiledArr[v][h + 1] == 5 | userGameFiledArr[v][h + 1] == 6)
                            && (userGameFiledArr[v + 1][h - 1] == 0 | userGameFiledArr[v + 1][h - 1] == 5 | userGameFiledArr[v + 1][h - 1] == 6)
                            && (userGameFiledArr[v + 1][h + 1] == 0 | userGameFiledArr[v + 1][h + 1] == 5 | userGameFiledArr[v + 1][h + 1] == 6)) {
                        userGameFiledArr[v][h] = 0;
                    }
                    if(v == GAME_FILED_LENGTH - 1 && (userGameFiledArr[v - 1][h] == 0 | userGameFiledArr[v - 1][h] == 5 | userGameFiledArr[v - 1][h] == 6)
                            && (h > 0 && userGameFiledArr[v][h - 1] == 0 | userGameFiledArr[v][h - 1] == 5 | userGameFiledArr[v][h - 1] == 6)
                            && (h < GAME_FILED_LENGTH - 1 && userGameFiledArr[v][h + 1] == 0 | userGameFiledArr[v][h + 1] == 5 | userGameFiledArr[v][h + 1] == 6)
                            && (userGameFiledArr[v - 1][h - 1] == 0 | userGameFiledArr[v - 1][h - 1] == 5 | userGameFiledArr[v - 1][h - 1] == 6)
                            && (userGameFiledArr[v - 1][h + 1] == 0 | userGameFiledArr[v - 1][h + 1] == 5 | userGameFiledArr[v - 1][h + 1] == 6)) {
                        userGameFiledArr[v][h] = 0;
                    }
                    if(h == 0 && (userGameFiledArr[v][h + 1] == 0 | userGameFiledArr[v][h + 1] == 5 | userGameFiledArr[v][h + 1] == 6)
                            && (v > 0 && userGameFiledArr[v - 1][h] == 0 | userGameFiledArr[v - 1][h] == 5 | userGameFiledArr[v - 1][h] == 6)
                            && (v < GAME_FILED_LENGTH - 1 && userGameFiledArr[v + 1][h] == 0 | userGameFiledArr[v + 1][h] == 5 | userGameFiledArr[v + 1][h] == 6)
                            && (userGameFiledArr[v - 1][h + 1] == 0 | userGameFiledArr[v - 1][h + 1] == 5 | userGameFiledArr[v - 1][h + 1] == 6)
                            && (userGameFiledArr[v + 1][h + 1] == 0 | userGameFiledArr[v + 1][h + 1] == 5 | userGameFiledArr[v + 1][h + 1] == 6)) {
                        userGameFiledArr[v][h] = 0;
                    }
                    if(h == GAME_FILED_LENGTH - 1 && (userGameFiledArr[v][h - 1] == 0 | userGameFiledArr[v][h - 1] == 5 | userGameFiledArr[v][h - 1] == 6)
                            && (v > 0 && userGameFiledArr[v - 1][h] == 0 | userGameFiledArr[v - 1][h] == 5 | userGameFiledArr[v - 1][h] == 6)
                            && (v < GAME_FILED_LENGTH - 1 && userGameFiledArr[v + 1][h] == 0 | userGameFiledArr[v + 1][h] == 5 | userGameFiledArr[v + 1][h] == 6)
                            && (userGameFiledArr[v - 1][h - 1] == 0 | userGameFiledArr[v - 1][h - 1] == 5 | userGameFiledArr[v - 1][h - 1] == 6)
                            && (userGameFiledArr[v + 1][h - 1] == 0 | userGameFiledArr[v + 1][h - 1] == 5 | userGameFiledArr[v + 1][h - 1] == 6)) {
                        userGameFiledArr[v][h] = 0;
                    }
                    if(h == 0 && v == 0 && (userGameFiledArr[v + 1][h] == 0 | userGameFiledArr[v + 1][h] == 5 | userGameFiledArr[v + 1][h] == 6)
                            && (userGameFiledArr[v + 1][h + 1] == 0 | userGameFiledArr[v + 1][h + 1] == 5 | userGameFiledArr[v + 1][h + 1] == 6)
                            && (userGameFiledArr[v][h + 1] == 0 | userGameFiledArr[v][h + 1] == 5 | userGameFiledArr[v][h + 1] == 6)) {
                        userGameFiledArr[v][h] = 0;
                    }
                    if(h == 0 && v == GAME_FILED_LENGTH - 1 && (userGameFiledArr[v - 1][h] == 0 | userGameFiledArr[v - 1][h] == 5 | userGameFiledArr[v - 1][h] == 6)
                            && (userGameFiledArr[v - 1][h + 1] == 0 | userGameFiledArr[v - 1][h + 1] == 5 | userGameFiledArr[v - 1][h + 1] == 6)
                            && (userGameFiledArr[v][h + 1] == 0 | userGameFiledArr[v][h + 1] == 5 | userGameFiledArr[v][h + 1] == 6)) {
                        userGameFiledArr[v][h] = 0;
                    }
                    if(h == GAME_FILED_LENGTH - 1 && v == 0 && (userGameFiledArr[v + 1][h] == 0 | userGameFiledArr[v + 1][h] == 5 | userGameFiledArr[v + 1][h] == 6)
                            && (userGameFiledArr[v + 1][h - 1] == 0 | userGameFiledArr[v + 1][h - 1] == 5 | userGameFiledArr[v + 1][h - 1] == 6)
                            && (userGameFiledArr[v][h - 1] == 0 | userGameFiledArr[v][h - 1] == 5 | userGameFiledArr[v][h - 1] == 6)) {
                        userGameFiledArr[v][h] = 0;
                    }
                    if(h == GAME_FILED_LENGTH - 1 && v == GAME_FILED_LENGTH - 1 && (userGameFiledArr[v - 1][h] == 0 | userGameFiledArr[v - 1][h] == 5 | userGameFiledArr[v - 1][h] == 6)
                            && (userGameFiledArr[v - 1][h - 1] == 0 | userGameFiledArr[v - 1][h - 1] == 5 | userGameFiledArr[v - 1][h - 1] == 6)
                            && (userGameFiledArr[v][h - 1] == 0 | userGameFiledArr[v][h - 1] == 5 | userGameFiledArr[v][h - 1] == 6)) {
                        userGameFiledArr[v][h] = 0;
                    }
                }
            }
        }
    }
}
