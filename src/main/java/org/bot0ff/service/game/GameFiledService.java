package org.bot0ff.service.game;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.bot0ff.util.Constants.GAME_FILED_LENGTH;

@Log4j
@Service
@RequiredArgsConstructor
public class GameFiledService {
    public static Map<Long, int[][]> prepareUserFiledMap = Collections.synchronizedMap(new HashMap<>());

    //проверка правильности подготовленного игрового поля
    public boolean checkPreparedShips(int[][] gameFiled) {
        var oneDeckShip = 0;
        var twoDeckShip = 0;
        var threeDeckShip = 0;
        var fourDeckShip = 0;

        for(int ver = 0; ver < GAME_FILED_LENGTH; ver++) {
            for(int hor = 0; hor < GAME_FILED_LENGTH; hor++) {
                if(gameFiled[ver][hor] == 1) {
                    oneDeckShip++;
                }
                else if(gameFiled[ver][hor] == 2) {
                    twoDeckShip++;
                }
                else if(gameFiled[ver][hor] == 3) {
                    threeDeckShip++;
                }
                else if(gameFiled[ver][hor] == 4) {
                    fourDeckShip++;
                }
            }
        }

        return oneDeckShip == 4 && twoDeckShip == 6 && threeDeckShip == 6 && fourDeckShip == 4;
    }

    //преобразует игровое поле типа массив в лист
    public List<String> convertArrFiledToList(int[][] userGameFiled) {
        List<String> tempList = new ArrayList<>();
        for(int ver = 0; ver < GAME_FILED_LENGTH; ver++) {
            for(int hor = 0; hor < GAME_FILED_LENGTH; hor++) {
                if(userGameFiled[ver][hor] == 1) {
                    tempList.add(ver + "-" + hor);
                }
                else if(userGameFiled[ver][hor] == 2) {
                    tempList.add(ver + "=" + hor);
                }
                else if(userGameFiled[ver][hor] == 3) {
                    tempList.add(ver + ">" + hor);
                }
                else if(userGameFiled[ver][hor] == 4) {
                    tempList.add(ver + "#" + hor);
                }
                else if(userGameFiled[ver][hor] == -1) {
                    tempList.add(ver + "x" + hor);
                }
                else if(userGameFiled[ver][hor] == -2) {
                    tempList.add(ver + "o" + hor);
                }
                else {
                    tempList.add(ver + ":" + hor);
                }
            }
        }
        return tempList;
    }

    //преобразует игровое поле типа лист в массив
    public int[][] convertListFiledToArr(List<String> userGameFiled) {
        int[][] tempArr = new int[GAME_FILED_LENGTH][GAME_FILED_LENGTH];
        for(String coordinate : userGameFiled) {
            String[] split;
            if(coordinate.contains("-")) {
                split = coordinate.split("-");
                tempArr[Integer.parseInt(split[0])][Integer.parseInt(split[1])] = 1;
            }
            else if(coordinate.contains("=")) {
                split = coordinate.split("=");
                tempArr[Integer.parseInt(split[0])][Integer.parseInt(split[1])] = 2;
            }
            else if(coordinate.contains(">")) {
                split = coordinate.split(">");
                tempArr[Integer.parseInt(split[0])][Integer.parseInt(split[1])] = 3;
            }
            else if(coordinate.contains("#")) {
                split = coordinate.split("#");
                tempArr[Integer.parseInt(split[0])][Integer.parseInt(split[1])] = 4;
            }
            else if(coordinate.contains("x")) {
                split = coordinate.split("x");
                tempArr[Integer.parseInt(split[0])][Integer.parseInt(split[1])] = -1;
            }
            else if(coordinate.contains("o")) {
                split = coordinate.split("o");
                tempArr[Integer.parseInt(split[0])][Integer.parseInt(split[1])] = -2;
            }
            else if(coordinate.contains(":")) {
                split = coordinate.split(":");
                tempArr[Integer.parseInt(split[0])][Integer.parseInt(split[1])] = 0;
            }
        }
        return tempArr;
    }
}
