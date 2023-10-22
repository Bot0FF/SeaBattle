package org.bot0ff.service.game;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
    Создание игрового поля с автоматической расстановкой кораблей
    1. Создается массив 10 на 10 и заполняется нулями в методе getAutomaticGameFiled
    2. В методе getAutomaticGameFiled вызывается метод getShip в параметры которого передается аргумент со значением длины корабля
    3. При создании корабля в методе getShip выбирается тип корабля - вертикальный или горизонтальный
    4. После определения типа выбирается случайная стартовая позиция с учетом длины корабля (напр. если тип вертикальный, длина 4,
        то максимальная случайная стартовая позиция по вертикали будет 10 - 4)
    5. При создании корабля сам корабль отмечается значением 2, все квадраты вокруг него значением 1 и позиция корабля и координат вокруг него сохраняются в массив.
    6. При создании корабля с уже имеющимися в массиве другими кораблями, сначала определяется стартовая точка таким образом,
        чтобы вся длина корабля не попадала на занятые другими кораблями координаты, отмеченными 1 или 2.
 **/

@Log4j
@Service
@RequiredArgsConstructor
public class AutomaticPrepareFiled {

    private List<String> resultFiled;
    private int[][] prepareFiled;

    public List<String> getAutomaticGameFiled() {
        resultFiled = new ArrayList<>();
        prepareFiled = new int[10][10];
        for(int ver = 0; ver < 10; ver++) {
            for(int hor = 0; hor < 10; hor++) {
                prepareFiled[ver][hor] = 0;
            }
        }

        getShips(4);

        getShips(3);
        getShips(3);

        getShips(2);
        getShips(2);
        getShips(2);

        getShips(1);
        getShips(1);
        getShips(1);
        getShips(1);

        return resultFiled;
    }

    private void getShips(int shipLength) {
        int vertOrHor = getR0Or1();
        int pointHorizontal = 0;
        int pointVertical = 0;
        if(vertOrHor == 0) {
            //вертикальное расположение
            if(shipLength == 4) {
                do {
                    pointHorizontal = getRNum(0);
                    pointVertical = getRNum(shipLength);
                }
                while (!(prepareFiled[pointVertical][pointHorizontal] == 0
                        & prepareFiled[pointVertical + 1][pointHorizontal] == 0
                        & prepareFiled[pointVertical + 2][pointHorizontal] == 0
                        & prepareFiled[pointVertical + 3][pointHorizontal] == 0));

            }
            else if(shipLength == 3) {
                do {
                    pointHorizontal = getRNum(0);
                    pointVertical = getRNum(shipLength);
                }
                while (!(prepareFiled[pointVertical][pointHorizontal] == 0
                        & prepareFiled[pointVertical + 1][pointHorizontal] == 0
                        & prepareFiled[pointVertical + 2][pointHorizontal] == 0));
            }
            else if(shipLength == 2) {
                do {
                    pointHorizontal = getRNum(0);
                    pointVertical = getRNum(shipLength);
                }
                while (!(prepareFiled[pointVertical][pointHorizontal] == 0
                        & prepareFiled[pointVertical + 1][pointHorizontal] == 0));
            }
            else if(shipLength == 1) {
                do {
                    pointHorizontal = getRNum(0);
                    pointVertical = getRNum(shipLength);
                }
                while (!(prepareFiled[pointVertical][pointHorizontal] == 0));
            }

            if(pointVertical > 0) {
                this.prepareFiled[(pointVertical - 1)][pointHorizontal] = 1;
            }
            if(pointVertical > 0 & pointHorizontal > 0) {
                this.prepareFiled[(pointVertical - 1)][(pointHorizontal - 1)] = 1;
            }
            if(pointVertical > 0 & pointHorizontal < 9) {
                this.prepareFiled[(pointVertical - 1)][(pointHorizontal + 1)] = 1;
            }
            if((pointVertical + shipLength - 1) < 9) {
                this.prepareFiled[(pointVertical + shipLength)][pointHorizontal] = 1;
            }
            if((pointVertical + shipLength - 1) < 9 & pointHorizontal > 0) {
                this.prepareFiled[(pointVertical + shipLength)][(pointHorizontal - 1)] = 1;
            }
            if((pointVertical + shipLength - 1) < 9 & pointHorizontal < 9) {
                this.prepareFiled[(pointVertical + shipLength)][(pointHorizontal + 1)] = 1;
            }
            for(int ver = pointVertical; ver < pointVertical + shipLength; ver++) {
                if(pointHorizontal > 0) {
                    this.prepareFiled[ver][(pointHorizontal - 1)] = 1;
                }
                if(pointHorizontal < 9) {
                    this.prepareFiled[ver][(pointHorizontal + 1)] = 1;
                }
                this.prepareFiled[ver][pointHorizontal] = 2;
                this.resultFiled.add(ver + ":" + pointHorizontal);
            }
        }
        else {
            //горизонтальное расположение
            if(shipLength == 4) {
                do {
                    pointHorizontal = getRNum(shipLength);
                    pointVertical = getRNum(0);
                }
                while (!(prepareFiled[pointVertical][pointHorizontal] == 0
                        & prepareFiled[pointVertical][pointHorizontal + 1] == 0
                        & prepareFiled[pointVertical][pointHorizontal + 2] == 0
                        & prepareFiled[pointVertical][pointHorizontal + 3] == 0));
            }
            else if(shipLength == 3) {
                do {
                    pointHorizontal = getRNum(shipLength);
                    pointVertical = getRNum(0);
                }
                while (!(prepareFiled[pointVertical][pointHorizontal] == 0
                        & prepareFiled[pointVertical][pointHorizontal + 1] == 0
                        & prepareFiled[pointVertical][pointHorizontal + 2] == 0));
            }
            else if(shipLength == 2) {
                do {
                    pointHorizontal = getRNum(shipLength);
                    pointVertical = getRNum(0);
                }
                while (!(prepareFiled[pointVertical][pointHorizontal] == 0
                        & prepareFiled[pointVertical][pointHorizontal + 1] == 0));
            }
            else if(shipLength == 1) {
                do {
                    pointHorizontal = getRNum(shipLength);
                    pointVertical = getRNum(0);
                }
                while (!(prepareFiled[pointVertical][pointHorizontal] == 0));
            }

            if(pointHorizontal > 0) {
                this.prepareFiled[pointVertical][(pointHorizontal - 1)] = 1;
            }
            if(pointHorizontal > 0 & pointVertical > 0) {
                this.prepareFiled[(pointVertical - 1)][(pointHorizontal - 1)] = 1;
            }
            if(pointHorizontal > 0 & pointVertical < 9) {
                this.prepareFiled[(pointVertical + 1)][(pointHorizontal - 1)] = 1;
            }
            if((pointHorizontal + shipLength - 1) < 9) {
                this.prepareFiled[pointVertical][(pointHorizontal + shipLength)] = 1;
            }
            if((pointHorizontal + shipLength - 1) < 9 & pointVertical > 0) {
                this.prepareFiled[(pointVertical - 1)][(pointHorizontal + shipLength)] = 1;
            }
            if((pointHorizontal + shipLength - 1) < 9 & pointVertical < 9) {
                this.prepareFiled[(pointVertical + 1)][(pointHorizontal + shipLength)] = 1;
            }
            for(int hor = pointHorizontal; hor < pointHorizontal + shipLength; hor++) {
                if(pointVertical > 0) {
                    this.prepareFiled[(pointVertical - 1)][hor] = 1;
                }
                if(pointVertical < 9) {
                    this.prepareFiled[(pointVertical + 1)][hor] = 1;
                }
                this.prepareFiled[pointVertical][hor] = 2;
                this.resultFiled.add(pointVertical + ":" + hor);
            }
        }
    }

    //рандом 0-9
    private int getRNum(int shipLength) {
        RandomDataGenerator randomGenerator = new RandomDataGenerator();
        return randomGenerator.nextInt(0, 9 - shipLength);
    }
    //рандом 0-1
    private int getR0Or1() {
        RandomDataGenerator randomGenerator = new RandomDataGenerator();
        return randomGenerator.nextInt(0, 1);
    }
}
