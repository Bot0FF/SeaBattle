package org.bot0ff.service.game;

import com.vdurmont.emoji.EmojiParser;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.bot0ff.util.Constants.HORIZONTAL_LENGTH;
import static org.bot0ff.util.Constants.VERTICAL_LENGTH;

@Log4j
@Service
public class GenerateEmojiGameFiled {
    private final String water = EmojiParser.parseToUnicode(":droplet:");
    private final String ship = EmojiParser.parseToUnicode(":sailboat:");
    private final String success = EmojiParser.parseToUnicode(":anchor:");
    private final String miss = EmojiParser.parseToUnicode(":mag:");

    private String emojiParser(String emojiName) {
        return EmojiParser.parseToUnicode(emojiName);
    }

    public StringBuilder getEmojiGameFiled(List<String> gameFiled) {
        int [][] tempArr = new int[VERTICAL_LENGTH][HORIZONTAL_LENGTH];
        for(String coord : gameFiled) {
            if(coord.contains(":")) {
                String[] split = coord.split(":");
                tempArr[Integer.parseInt(split[0])][Integer.parseInt(split[1])] = 1; //корабль есть
            }
            if(coord.contains("_")) {
                String[] split = coord.split("_");
                tempArr[Integer.parseInt(split[0])][Integer.parseInt(split[1])] = 2; //вода
            }
            else if(coord.contains("-")) {
                String[] split = coord.split("-");
                tempArr[Integer.parseInt(split[0])][Integer.parseInt(split[1])] = -1; //корабль подбит
            }
            else if(coord.contains("/")) {
                String[] split = coord.split("/");
                tempArr[Integer.parseInt(split[0])][Integer.parseInt(split[1])] = 0; //мимо
            }

        }
        StringBuilder sb = new StringBuilder();
        for(int ver = 0; ver < VERTICAL_LENGTH; ver++) {
            for(int hor = 0; hor < HORIZONTAL_LENGTH; hor++) {
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
}
