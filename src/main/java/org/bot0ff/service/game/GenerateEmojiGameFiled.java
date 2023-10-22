package org.bot0ff.service.game;

import com.vdurmont.emoji.EmojiParser;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j
@Service
public class GenerateEmojiGameFiled {
    private final String water = EmojiParser.parseToUnicode(":droplet:");
    private final String ship = EmojiParser.parseToUnicode(":sailboat:");

    private String emojiParser(String emojiName) {
        return EmojiParser.parseToUnicode(emojiName);
    }

    public StringBuilder getEmojiGameFiled(List<String> shipCoordinates) {
        int [][] tempArr = new int[10][10];
        for(String coord : shipCoordinates) {
            String[] split = coord.split(":");
            tempArr[Integer.parseInt(split[0])][Integer.parseInt(split[1])] = 2;
        }
        StringBuilder sb = new StringBuilder();
        for(int ver = 0; ver < 10; ver++) {
            for(int hor = 0; hor < 10; hor++) {
                if(tempArr[ver][hor] == 2) {
                    sb.append(emojiParser(ship));
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
