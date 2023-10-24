package org.bot0ff.service.game;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bot0ff.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j
@Service
@RequiredArgsConstructor
public class GameService {

    public void startRound(User user) {

    }

    public boolean checkEndStep(String userChangeTarget, User userAi) {
        String[] split = userChangeTarget.split(":");
        switch (split[0]) {
            case "А" -> split[0] = "0";
            case "Б" -> split[0] = "1";
            case "В" -> split[0] = "2";
            case "Г" -> split[0] = "3";
            case "Д" -> split[0] = "4";
            case "Е" -> split[0] = "5";
            case "Ж" -> split[0] = "6";
            case "З" -> split[0] = "7";
            case "И" -> split[0] = "8";
            case "К" -> split[0] = "9";
        }
        switch (split[1]) {
            case "1" -> split[1] = "0";
            case "2" -> split[1] = "1";
            case "3" -> split[1] = "2";
            case "4" -> split[1] = "3";
            case "5" -> split[1] = "4";
            case "6" -> split[1] = "5";
            case "7" -> split[1] = "6";
            case "8" -> split[1] = "7";
            case "9" -> split[1] = "8";
            case "10" -> split[1] = "9";
        }
        String temp = split[0] + ":" + split[1];
        System.out.println(temp);
        List<String> userAiFiled = userAi.getGameFiled();
        if(userAiFiled.stream().anyMatch(userAiShip -> userAiShip.equals(temp))) {
            userAiFiled = userAiFiled.stream().map(target -> target.equals(temp) ? ("-" + split[0] + ":" + split[1]) : target).toList();
            userAi.setGameFiled(userAiFiled);
            return true;
        }
        else return false;
    }

    public void calculateResult(User user) {

    }
}
