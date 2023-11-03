package org.bot0ff.configuration;

import lombok.extern.log4j.Log4j;
import org.bot0ff.component.TelegramBot;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Log4j
@Component
@RequiredArgsConstructor
public class TelegramBotInit {
    private final TelegramBot telegramBot;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot((telegramBot));
        } catch (TelegramApiException e) {
            log.error("TelegramBotInit - Ошибка при инициализации TelegramBot: " + e.getMessage());
        }
    }
}
