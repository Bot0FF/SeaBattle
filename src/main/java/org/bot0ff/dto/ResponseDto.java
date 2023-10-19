package org.bot0ff.dto;

import lombok.Builder;
import lombok.Data;
import org.bot0ff.component.TelegramBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Data
@Builder
public class ResponseDto {
    TelegramBot telegramBot;
    SendMessage sendMessage;
    AnswerCallbackQuery answerCallbackQuery;
}
