package org.bot0ff.service.game;

import com.vdurmont.emoji.EmojiParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import static org.bot0ff.util.Constants.GAME_FILED_LENGTH;

@Log4j
@Service
@RequiredArgsConstructor
public class GameMessageService {
    private final String oneDeckShip = EmojiParser.parseToUnicode(":sailboat:");
    private final String twoDeckShip = EmojiParser.parseToUnicode(":speedboat:");
    private final String threeDeckShip = EmojiParser.parseToUnicode(":ferry:");
    private final String fourDeckShip = EmojiParser.parseToUnicode(":ship:");
    private final String hit = EmojiParser.parseToUnicode(":x:");
    private final String miss = EmojiParser.parseToUnicode(":o:");
    private final String water = EmojiParser.parseToUnicode(":droplet:");

    public static String emojiPars(String emojiName) {
        return EmojiParser.parseToUnicode(emojiName);
    }

    public String emojiParser(String emojiName) {
        return EmojiParser.parseToUnicode(emojiName);
    }

    //возвращает поле в виде смайлового представления
    public StringBuilder getEmojiGameFiled(int[][] userGameFiled) {
        StringBuilder sb = new StringBuilder();
        for(int ver = 0; ver < GAME_FILED_LENGTH; ver++) {
            for(int hor = 0; hor < GAME_FILED_LENGTH; hor++) {
                if(userGameFiled[ver][hor] == 1) {
                    sb.append(emojiParser(oneDeckShip)).append(" ");
                }
                else if(userGameFiled[ver][hor] == 2) {
                    sb.append(emojiParser(twoDeckShip)).append(" ");
                }
                else if(userGameFiled[ver][hor] == 3) {
                    sb.append(emojiParser(threeDeckShip)).append(" ");
                }
                else if(userGameFiled[ver][hor] == 4) {
                    sb.append(emojiParser(fourDeckShip)).append(" ");
                }
                else if(userGameFiled[ver][hor] == -1) {
                    sb.append(emojiParser(hit)).append(" ");
                }
                else if(userGameFiled[ver][hor] == -2) {
                    sb.append(emojiParser(miss)).append(" ");
                }
                else {
                    sb.append(emojiParser(water)).append(" ");
                }
            }
            sb.append("\n");
        }
        return sb;
    }

    //возвращает текстовое представление результата хода
    public String getCurrentGameFiled(String notification, int[][] userGameFiled) {
        return  notification +
                "\nВаше поле\n" + getEmojiGameFiled(userGameFiled) +
                "\nПоле противника";
    }
}
