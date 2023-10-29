package org.bot0ff.dto;

import lombok.Builder;
import lombok.Data;
import org.bot0ff.component.TelegramBot;
import org.bot0ff.entity.User;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

@Data
@Builder
public class ResponseDto {
    TelegramBot telegramBot;
    SendMessage sendMessage;
    EditMessageText editMessageText;
    AnswerCallbackQuery answerCallbackQuery;
    User user;
}
