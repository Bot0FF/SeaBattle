package org.bot0ff.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bot0ff.component.TelegramBot;
import org.bot0ff.dto.ResponseDto;
import org.bot0ff.service.*;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.bot0ff.entity.UserState.*;

//перенаправляет запросы в соответствующие сервисы
@Log4j
@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {
    private final UserService userService;
    private final RegistrationService registrationService;
    private final ActivityService activityService;
    private final PrepareGameService prepareGameService;
    private final SearchGameService searchGameService;
    private final InGameService inGameService;

    //перенаправляет текстовые запросы
    @Override
    public void processTextMessage(TelegramBot telegramBot, Update update) {
        var sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());

        var user = userService.findOrSaveUser(update);
        var userState = user.getState();
        var inputMessage = update.getMessage().getText();

        if(WAIT_REGISTRATION.equals(userState)) {
            var updateUser = registrationService.processRegistrationText(user, sendMessage, inputMessage);
            var response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .sendMessage(updateUser.getSendMessage())
                    .build();
            telegramBot.sendAnswer(response);
        }
        else if(ONLINE.equals(userState)) {
            var updateUser = activityService.changeOptionsFromMenu(user, sendMessage, inputMessage);
            var response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .sendMessage(updateUser.getSendMessage())
                    .build();
            telegramBot.sendAnswer(response);
        }
        else if(PREPARE_GAME.equals(userState)) {
            var updateUser = prepareGameService.optionsPrepareGameText(user, sendMessage, inputMessage);
            var response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .sendMessage(updateUser.getSendMessage())
                    .build();
            telegramBot.sendAnswer(response);
        }
        else if(SEARCH_GAME.equals(userState)) {
            var updateUser = searchGameService.searchGameText(user, sendMessage, inputMessage);
            var response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .sendMessage(updateUser.getSendMessage())
                    .build();
            telegramBot.sendAnswer(response);
        }
        else if(IN_GAME.equals(userState)) {
            var updateUser = inGameService.processTextMessage(update, user, sendMessage, inputMessage);
            var response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .sendMessage(updateUser.getSendMessage())
                    .build();
            telegramBot.sendAnswer(response);
        }
        else {
            log.error("Unknown user state: " + userState);
            var answer = getInfo("Неизвестная ошибка! Введите /cancel и попробуйте снова!", sendMessage);
            var response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .sendMessage(answer)
                    .build();
            telegramBot.sendAnswer(response);
        }
        user.setMessageId(update.getMessage().getMessageId() + 1);
        userService.saveUser(user);
    }

    //перенаправляет inline запросы
    @Override
    public void processCallbackQuery(TelegramBot telegramBot, Update update) {
        var user = userService.findOrSaveUser(update);
        var userState = user.getState();
        var inputMessage = update.getCallbackQuery().getData();
        var editMessageText = new EditMessageText();
        var answerCallbackQuery = new AnswerCallbackQuery();
        editMessageText.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
        editMessageText.setMessageId(user.getMessageId());
        answerCallbackQuery.setCallbackQueryId(update.getCallbackQuery().getId());

        if(WAIT_REGISTRATION.equals(userState)) {
            var updateUser = registrationService.processRegistrationInline(user, editMessageText, answerCallbackQuery, inputMessage);
            var response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .editMessageText(updateUser.getEditMessageText())
                    .answerCallbackQuery(answerCallbackQuery)
                    .build();
            telegramBot.sendAnswer(response);
        }
        else if(ONLINE.equals(userState)) {
            var updateUser = activityService.changeOptions(user, editMessageText, answerCallbackQuery, inputMessage);
            var response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .editMessageText(updateUser.getEditMessageText())
                    .answerCallbackQuery(answerCallbackQuery)
                    .build();
            telegramBot.sendAnswer(response);
        }
        else if(PREPARE_GAME.equals(userState)) {
            var updateUser = prepareGameService.optionsPrepareGameInline(user, editMessageText, answerCallbackQuery, inputMessage);
            var response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .editMessageText(updateUser.getEditMessageText())
                    .answerCallbackQuery(updateUser.getAnswerCallbackQuery())
                    .build();
            telegramBot.sendAnswer(response);
        }
        else if(SEARCH_GAME.equals(userState)) {
            var updateUser = searchGameService.searchGameInline(user, editMessageText, answerCallbackQuery, inputMessage);
            var response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .editMessageText(updateUser.getEditMessageText())
                    .answerCallbackQuery(updateUser.getAnswerCallbackQuery())
                    .build();
            telegramBot.sendAnswer(response);
        }
        else if(IN_GAME.equals(userState)) {
            var updateUser = inGameService.processCallbackQuery(update, user, editMessageText, answerCallbackQuery, inputMessage);
            var response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .editMessageText(updateUser.getEditMessageText())
                    .answerCallbackQuery(updateUser.getAnswerCallbackQuery())
                    .build();
            telegramBot.sendAnswer(response);
        }
        user.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        userService.saveUser(user);
    }

    private SendMessage getInfo(String msg, SendMessage sendMessage) {
        sendMessage.setText(msg);
        return sendMessage;
    }
}
