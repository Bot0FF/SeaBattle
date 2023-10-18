package org.bot0ff.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bot0ff.component.TelegramBot;
import org.bot0ff.kafka.UpdateProducer;
import org.bot0ff.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@Controller
@RequiredArgsConstructor
public class UpdateController {
    private TelegramBot telegramBot;
    private final MessageUtils messageUtils;
    private final UpdateProducer updateProducer;
    @Value("${kafka.producer.message.incoming}")
    private String incomingMessageFromTelegram;

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void processUpdate(Update update) {
        if(update == null) {
            log.error("Received update is null");
            return;
        }

        if(update.getMessage() != null) {
            processTextMessage(update);
        }
        else if(update.getCallbackQuery() != null) {
            processCallbackQuery(update);
        }
        else {
            setUnsupportedMessageType(update);
            log.error("Received unsupported message type " + update);
        }
    }

    private void processTextMessage(Update update) {
        updateProducer.produce(incomingMessageFromTelegram, update);
    }

    private void processCallbackQuery(Update update) {
        updateProducer.produce(incomingMessageFromTelegram, update);
    }

    private void setUnsupportedMessageType(Update update) {
        var sendMessage = messageUtils.generateSendMessage(update,
                "Неподдерживаемый тип сообщений");
        setView(sendMessage);
    }

    private void setView(SendMessage sendMessage) {
        telegramBot.sendTextMessage(sendMessage);
    }

}
