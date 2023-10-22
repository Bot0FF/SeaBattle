package org.bot0ff.service;

import com.vdurmont.emoji.EmojiParser;
import lombok.Data;
import org.springframework.stereotype.Service;

//игровое поле из смайлов
@Data
@Service
public class UtilService {
    private String emHp = EmojiParser.parseToUnicode(":heart:");

    private String emojiParser(String emojiName) {
        return EmojiParser.parseToUnicode(emojiName);
    }

//    //возвращает поле размером 10 на 10 смайлов
//    public StringBuilder getEmojiMap(User user) {
//        StringBuilder sb = new StringBuilder();
//        for(int y = user.getPosY() - 2; y <= user.getPosY() + 2; y++) {
//            for(int x = user.getPosX() - 2; x <= user.getPosX() + 2; x++) {
//                if(y == user.getPosY() && x == user.getPosX()) {
//                    sb.append(" ").append(emojiParser(":man_farmer:"));
//                    continue;
//                }
//                if((y < 0 || y > 49) || (x < 0 || x > 49)) {
//                    sb.append(" ").append(emojiParser(":spider_web:"));
//                    continue;
//                }
//                sb.append(" ").append(emojiParser(ConstructMap.allLocations[y][x].getSmileLoc()));
//            }
//            sb.append("\n");
//        }
//        return sb;
//    }
}
