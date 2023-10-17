package org.bot0ff.configuration;

import org.bot0ff.component.TelegramBot;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@RequiredArgsConstructor
public class TelegramBotInit {
    private final TelegramBot telegramBot;
    private static final Logger log = Logger.getLogger(TelegramBotInit.class);

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot((telegramBot));
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
