package org.bot0ff.service.game;

import com.vdurmont.emoji.EmojiParser;
import lombok.extern.log4j.Log4j;
import org.bot0ff.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.bot0ff.util.Constants.GAME_FILED_LENGTH;

@Log4j
@Service
public class GameMessageService {
    private final String water = EmojiParser.parseToUnicode(":droplet:");
    private final String ship = EmojiParser.parseToUnicode(":sailboat:");
    private final String success = EmojiParser.parseToUnicode(":anchor:");
    private final String miss = EmojiParser.parseToUnicode(":mag:");

    public String emojiParser(String emojiName) {
        return EmojiParser.parseToUnicode(emojiName);
    }

    //возвращает поле в виде смайлового представления
    public StringBuilder getEmojiGameFiled(List<String> gameFiled) {
        int [][] tempArr = new int[GAME_FILED_LENGTH][GAME_FILED_LENGTH];
        for(String coordinate : gameFiled) {
            if(coordinate.contains(":")) {
                String[] split = coordinate.split(":");
                tempArr[Integer.parseInt(split[0])][Integer.parseInt(split[1])] = 1; //корабль есть
            }
            if(coordinate.contains("_")) {
                String[] split = coordinate.split("_");
                tempArr[Integer.parseInt(split[0])][Integer.parseInt(split[1])] = 2; //вода
            }
            else if(coordinate.contains("-")) {
                String[] split = coordinate.split("-");
                tempArr[Integer.parseInt(split[0])][Integer.parseInt(split[1])] = -1; //корабль подбит
            }
            else if(coordinate.contains("/")) {
                String[] split = coordinate.split("/");
                tempArr[Integer.parseInt(split[0])][Integer.parseInt(split[1])] = 0; //мимо
            }

        }
        StringBuilder sb = new StringBuilder();
        for(int ver = 0; ver < GAME_FILED_LENGTH; ver++) {
            for(int hor = 0; hor < GAME_FILED_LENGTH; hor++) {
                if(tempArr[ver][hor] == 1) {
                    sb.append(emojiParser(ship));
                }
                else if(tempArr[ver][hor] == -1) {
                    sb.append(emojiParser(success));
                }
                else if(tempArr[ver][hor] == 0) {
                    sb.append(emojiParser(miss));
                }
                else {
                    sb.append(emojiParser(water));
                }
            }
            sb.append("\n");
        }
        return sb;
    }

    //возвращает текстовое представление результата хода
    public String getCurrentGameFiled(String notification, User user) {
        return  notification +
                "\nПоле противника\n" + getEmojiGameFiled(user.getAiGameFiled()) +
                "-----------------------\n" +
                "Ваше поле\n" + getEmojiGameFiled(user.getGameFiled());
    }
}
