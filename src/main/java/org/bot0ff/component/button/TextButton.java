package org.bot0ff.component.button;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
public class TextButton {

    /*-------------КНОПКИ МЕНЮ-------------*/
    //команды меню
    public static List<BotCommand> commandMarkup() {
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Start"));
        listOfCommands.add(new BotCommand("/cancel", "Завершить игру"));

        return listOfCommands;
    }
}
