package org.bot0ff.service.game;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.bot0ff.entity.User;
import org.springframework.stereotype.Service;

import static org.bot0ff.util.Constants.GAME_FILED_LENGTH;

/**
    Создание игрового поля с автоматической расстановкой кораблей
    1. Создается массив 10 на 10 и заполняется нулями в методе getAutomaticGameFiled
    2. В методе getAutomaticGameFiled вызывается метод getShip в параметры которого передается аргумент со значением длины корабля
    3. При создании корабля в методе getShip выбирается тип корабля - вертикальный или горизонтальный
    4. После определения типа выбирается случайная стартовая позиция с учетом длины корабля (напр. если тип вертикальный, длина 4,
        то максимальная случайная стартовая позиция по вертикали будет 10 - 4)
    5. При создании корабля сам корабль отмечается значением, соответствующим типу корабля, все квадраты вокруг него значением 6 и позиция корабля и координат вокруг него сохраняются в массив.
    6. При создании корабля с уже имеющимися в массиве другими кораблями, сначала определяется стартовая точка таким образом,
        чтобы вся длина корабля не попадала на занятые другими кораблями координаты, отмеченными 1 или 2.
    7. Если при расстановке нет возможности разместить очередной корабль на поле, расстановка начинается заново.
 **/

@Log4j
@Service
@RequiredArgsConstructor
public class AutoPrepareService {
    private final GameFiledService gameFiledService;

    //TODO оптимизация расстановки, если нет свободных мест для корабля, зависает в цикле
    public void setAutoUserGameFiled(User user) {
        int[][] userGameFiled = GameFiledService.prepareUserFiledMap.get(user.getId());

        //очистка поля перед автоматической расстановкой
        for (int ver = 0; ver < GAME_FILED_LENGTH; ver++) { //буквы по вертикали
            for (int hor = 0; hor < GAME_FILED_LENGTH; hor++) { //цифры по горизонтали
                userGameFiled[ver][hor] = 0;
            }
        }

        while (!gameFiledService.checkPreparedShips(userGameFiled)) {
            for (int ver = 0; ver < GAME_FILED_LENGTH; ver++) { //буквы по вертикали
                for (int hor = 0; hor < GAME_FILED_LENGTH; hor++) { //цифры по горизонтали
                    userGameFiled[ver][hor] = 0;
                }
            }
            setShip(4, userGameFiled);

            setShip(3, userGameFiled);
            setShip(3, userGameFiled);

            setShip(2, userGameFiled);
            setShip(2, userGameFiled);
            setShip(2, userGameFiled);

            setShip(1, userGameFiled);
            setShip(1, userGameFiled);
            setShip(1, userGameFiled);
            setShip(1, userGameFiled);
        }
    }

    public int[][] setAutoAiGameFiled() {
        int[][] aiGameFiled = new int[GAME_FILED_LENGTH][GAME_FILED_LENGTH];

        while (!gameFiledService.checkPreparedShips(aiGameFiled)) {
            for (int ver = 0; ver < GAME_FILED_LENGTH; ver++) { //буквы по вертикали
                for (int hor = 0; hor < GAME_FILED_LENGTH; hor++) { //цифры по горизонтали
                    aiGameFiled[ver][hor] = 0;
                }
            }
            setShip(4, aiGameFiled);

            setShip(3, aiGameFiled);
            setShip(3, aiGameFiled);

            setShip(2, aiGameFiled);
            setShip(2, aiGameFiled);
            setShip(2, aiGameFiled);

            setShip(1, aiGameFiled);
            setShip(1, aiGameFiled);
            setShip(1, aiGameFiled);
            setShip(1, aiGameFiled);
        }
        return aiGameFiled;
    }

    public void setShip(int shipType, int[][] gameFiled) {
        int vertOrHor = getR0Or1();
        int pointHorizontal = 0;
        int pointVertical = 0;
        int countCycleValue = 0;

        if(vertOrHor == 0) {
            //вертикальное расположение
            if(shipType == 1) {
                do {
                    pointHorizontal = getRNum(0);
                    pointVertical = getRNum(shipType);
                    countCycleValue++;
                    if(countCycleValue > 5) break;
                }
                while (!(gameFiled[pointVertical][pointHorizontal] == 0));
            }
            else if(shipType == 2) {
                do {
                    pointHorizontal = getRNum(0);
                    pointVertical = getRNum(shipType);
                    countCycleValue++;
                    if(countCycleValue > 5) break;
                }
                while (!(gameFiled[pointVertical][pointHorizontal] == 0
                        & gameFiled[pointVertical + 1][pointHorizontal] == 0));
            }
            else if(shipType == 3) {
                do {
                    pointHorizontal = getRNum(0);
                    pointVertical = getRNum(shipType);
                    countCycleValue++;
                    if(countCycleValue > 5) break;
                }
                while (!(gameFiled[pointVertical][pointHorizontal] == 0
                        & gameFiled[pointVertical + 1][pointHorizontal] == 0
                        & gameFiled[pointVertical + 2][pointHorizontal] == 0));
            }
            else if(shipType == 4) {
                do {
                    pointHorizontal = getRNum(0);
                    pointVertical = getRNum(shipType);
                    countCycleValue++;
                    if(countCycleValue > 5) break;
                }
                while (!(gameFiled[pointVertical][pointHorizontal] == 0
                        & gameFiled[pointVertical + 1][pointHorizontal] == 0
                        & gameFiled[pointVertical + 2][pointHorizontal] == 0
                        & gameFiled[pointVertical + 3][pointHorizontal] == 0));

            }

            if(pointVertical > 0) {
                gameFiled[(pointVertical - 1)][pointHorizontal] = 6;
            }
            if(pointVertical > 0 & pointHorizontal > 0) {
                gameFiled[(pointVertical - 1)][(pointHorizontal - 1)] = 6;
            }
            if(pointVertical > 0 & pointHorizontal < (GAME_FILED_LENGTH - 1)) {
                gameFiled[(pointVertical - 1)][(pointHorizontal + 1)] = 6;
            }
            if((pointVertical + shipType - 1) < (GAME_FILED_LENGTH - 1)) {
                gameFiled[(pointVertical + shipType)][pointHorizontal] = 6;
            }
            if((pointVertical + shipType - 1) < (GAME_FILED_LENGTH - 1) & pointHorizontal > 0) {
                gameFiled[(pointVertical + shipType)][(pointHorizontal - 1)] = 6;
            }
            if((pointVertical + shipType - 1) < (GAME_FILED_LENGTH - 1) & pointHorizontal < (GAME_FILED_LENGTH - 1)) {
                gameFiled[(pointVertical + shipType)][(pointHorizontal + 1)] = 6;
            }
            for(int ver = pointVertical; ver < pointVertical + shipType; ver++) {
                if(pointHorizontal > 0) {
                    gameFiled[ver][(pointHorizontal - 1)] = 6;
                }
                if(pointHorizontal < (GAME_FILED_LENGTH - 1)) {
                    gameFiled[ver][(pointHorizontal + 1)] = 6;
                }
                gameFiled[ver][pointHorizontal] = shipType;
            }
        }
        else {
            //горизонтальное расположение
            if(shipType == 1) {
                do {
                    pointHorizontal = getRNum(shipType);
                    pointVertical = getRNum(0);
                    countCycleValue++;
                    if(countCycleValue > 5) break;
                }
                while (!(gameFiled[pointVertical][pointHorizontal] == 0));
            }
            else if(shipType == 2) {
                do {
                    pointHorizontal = getRNum(shipType);
                    pointVertical = getRNum(0);
                    countCycleValue++;
                    if(countCycleValue > 5) break;
                }
                while (!(gameFiled[pointVertical][pointHorizontal] == 0
                        & gameFiled[pointVertical][pointHorizontal + 1] == 0));
            }
            else if(shipType == 3) {
                do {
                    pointHorizontal = getRNum(shipType);
                    pointVertical = getRNum(0);
                    countCycleValue++;
                    if(countCycleValue > 5) break;
                }
                while (!(gameFiled[pointVertical][pointHorizontal] == 0
                        & gameFiled[pointVertical][pointHorizontal + 1] == 0
                        & gameFiled[pointVertical][pointHorizontal + 2] == 0));
            }
            else if(shipType == 4) {
                do {
                    pointHorizontal = getRNum(shipType);
                    pointVertical = getRNum(0);
                    countCycleValue++;
                    if(countCycleValue > 5) break;
                }
                while (!(gameFiled[pointVertical][pointHorizontal] == 0
                        & gameFiled[pointVertical][pointHorizontal + 1] == 0
                        & gameFiled[pointVertical][pointHorizontal + 2] == 0
                        & gameFiled[pointVertical][pointHorizontal + 3] == 0));
            }

            if(pointHorizontal > 0) {
                gameFiled[pointVertical][(pointHorizontal - 1)] = 6;
            }
            if(pointHorizontal > 0 & pointVertical > 0) {
                gameFiled[(pointVertical - 1)][(pointHorizontal - 1)] = 6;
            }
            if(pointHorizontal > 0 & pointVertical < (GAME_FILED_LENGTH - 1)) {
                gameFiled[(pointVertical + 1)][(pointHorizontal - 1)] = 6;
            }
            if((pointHorizontal + shipType - 1) < (GAME_FILED_LENGTH - 1)) {
                gameFiled[pointVertical][(pointHorizontal + shipType)] = 6;
            }
            if((pointHorizontal + shipType - 1) < (GAME_FILED_LENGTH - 1) & pointVertical > 0) {
                gameFiled[(pointVertical - 1)][(pointHorizontal + shipType)] = 6;
            }
            if((pointHorizontal + shipType - 1) < (GAME_FILED_LENGTH - 1) & pointVertical < (GAME_FILED_LENGTH - 1)) {
                gameFiled[(pointVertical + 1)][(pointHorizontal + shipType)] = 6;
            }
            for(int hor = pointHorizontal; hor < pointHorizontal + shipType; hor++) {
                if(pointVertical > 0) {
                    gameFiled[(pointVertical - 1)][hor] = 6;
                }
                if(pointVertical < (GAME_FILED_LENGTH - 1)) {
                    gameFiled[(pointVertical + 1)][hor] = 6;
                }
                gameFiled[pointVertical][hor] = shipType;
            }
        }
    }

    //рандом 0-9
    private int getRNum(int shipLength) {
        RandomDataGenerator randomGenerator = new RandomDataGenerator();
        return randomGenerator.nextInt(0, (GAME_FILED_LENGTH - 1) - shipLength);
    }
    //рандом 0-1
    private int getR0Or1() {
        RandomDataGenerator randomGenerator = new RandomDataGenerator();
        return randomGenerator.nextInt(0, 1);
    }
}
