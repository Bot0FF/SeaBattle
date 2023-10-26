package org.bot0ff.service.game;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.bot0ff.util.Constants.GAME_FILED_LENGTH;

@Log4j
@Service
@RequiredArgsConstructor
public class ManuallyPrepareService {

    //добавляет 4 палубный корабль и заполняет пустыми значениями стартовое поле для ручной расстановки
    public List<String> getAutomaticGameFiled() {
        List<String> resultFiled = new ArrayList<>();
        resultFiled.add((GAME_FILED_LENGTH % 2 - 2) + ":" + (GAME_FILED_LENGTH % 2 - 2));
        resultFiled.add((GAME_FILED_LENGTH % 2 - 1) + ":" + (GAME_FILED_LENGTH % 2 - 1));
        resultFiled.add((GAME_FILED_LENGTH % 2 ) + ":" + (GAME_FILED_LENGTH % 2));
        resultFiled.add((GAME_FILED_LENGTH % 2 + 1) + ":" + (GAME_FILED_LENGTH % 2 + 1));

        //заполняет оставшиеся координаты в поле
        for(int ver = 0; ver < GAME_FILED_LENGTH; ver++) { //буквы по вертикали
            for(int hor = 0; hor < GAME_FILED_LENGTH; hor++) { //цифры по горизонтали
                String exist = ver + ":" + hor;
                String notExist = ver + "_" + hor;
                if(resultFiled.stream().noneMatch(coordinates -> coordinates.equals(exist))) {
                    resultFiled.add(notExist);
                }
            }
        }

        return resultFiled;
    }

    public List<String> moveLeft(int moveNum, List<String> userGameFiled) {

    }

    public List<String> moveRight(int moveNum, List<String> userGameFiled) {

    }

    public List<String> rotateShip(int moveNum, List<String> userGameFiled) {

    }

    public List<String> moveUp(int moveNum, List<String> userGameFiled) {

    }

    public List<String> moveDown(int moveNum, List<String> userGameFiled) {

    }
}
