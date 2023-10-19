package org.bot0ff.service;

import org.bot0ff.component.TelegramBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface MainService {
    void processTextMessage(TelegramBot telegramBot, Update update);
    void processCallbackQuery(TelegramBot telegramBot, Update update);
}
