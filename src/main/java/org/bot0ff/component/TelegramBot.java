package org.bot0ff.component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bot0ff.component.button.TextButton;
import org.bot0ff.controller.EndGameController;
import org.bot0ff.controller.JoinUserController;
import org.bot0ff.controller.UpdateController;
import org.bot0ff.dto.ResponseDto;
import org.bot0ff.controller.UserAiController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Log4j
@Component
@PropertySource("application.properties")
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {
    @Value("${bot.name}")
    private String botName;
    @Value("${bot.token}")
    private String botToken;
    private final UpdateController updateController;
    private final UserAiController userAiController;
    private final EndGameController endGameController;
    private final JoinUserController joinUserController;

    @PostConstruct
    private void init() {
        updateController.registerBot(this);
        userAiController.registerBot(this);
        endGameController.registerBot(this);
        joinUserController.registerBot(this);
        try {
            this.execute(new SetMyCommands(TextButton.commandMarkup(), new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        updateController.processUpdate(update);
    }

    public void sendAnswer(ResponseDto responseDto) {
        try {
            if(responseDto.getSendMessage() != null) {
                execute(responseDto.getSendMessage());
            }
            if(responseDto.getEditMessageText() != null) {
                execute(responseDto.getEditMessageText());
            }
            if(responseDto.getAnswerCallbackQuery() != null) {
                execute(responseDto.getAnswerCallbackQuery());
            }
        } catch (TelegramApiException e) {
             e.printStackTrace();
        }

    }
//
//    public void sendAnswerCallbackQuery(AnswerCallbackQuery answerCallbackQuery) {
//        try {
//            execute(answerCallbackQuery);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//    }
}
