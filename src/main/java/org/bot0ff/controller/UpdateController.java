package org.bot0ff.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bot0ff.component.TelegramBot;
import org.bot0ff.dto.ResponseDto;
import org.bot0ff.service.MainService;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@Controller
@RequiredArgsConstructor
public class UpdateController {
    private TelegramBot telegramBot;
    private final MainService mainService;

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void processUpdate(Update update) {
        if(update == null) {
            log.error("Received update is null");
            return;
        }

        if(update.getMessage() != null) {
            processTextMessage(telegramBot, update);
        }
        else if(update.getCallbackQuery() != null) {
            processCallbackQuery(telegramBot, update);
        }
        else {
            setUnsupportedMessageType(update);
            log.error("Received unsupported message type " + update);
        }
    }

    private void processTextMessage(TelegramBot telegramBot, Update update) {
        mainService.processTextMessage(telegramBot, update);
        //System.out.println(update.getMessage().getText());
    }

    private void processCallbackQuery(TelegramBot telegramBot, Update update) {
        mainService.processCallbackQuery(telegramBot, update);
        //System.out.println(update.getCallbackQuery().getData());
    }

    private void setUnsupportedMessageType(Update update) {
        var message = update.getMessage();
        var sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText("Неподдерживаемый тип сообщений");
        var response = ResponseDto.builder()
                        .telegramBot(telegramBot)
                        .sendMessage(sendMessage)
                        .build();
        telegramBot.sendAnswer(response);
    }
}
