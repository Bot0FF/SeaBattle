package org.bot0ff.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bot0ff.component.TelegramBot;
import org.bot0ff.component.button.InlineButton;
import org.bot0ff.dto.ResponseDto;
import org.bot0ff.service.*;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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
    private final PrepareManuallyService prepareManuallyService;
    private final PrepareAutomaticService prepareAutomaticService;
    private final SearchGameService searchGameService;
    private final InGameService inGameService;
    private final InGameService gameService;

    //перенаправляет текстовые запросы
    @Override
    public void processTextMessage(TelegramBot telegramBot, Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        var answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(String.valueOf(update.getMessage().getChatId()));

        var user = userService.findOrSaveUser(update);
        var userState = user.getState();
        var inputMessage = update.getMessage().getText();

        if(WAIT_REGISTRATION.equals(userState)) {
            var answer = registrationService.processRegistrationText(user, sendMessage, inputMessage);
            var response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .sendMessage(answer)
                    .build();
            telegramBot.sendAnswer(response);
        }
        else if(ONLINE.equals(userState)) {
            var answer = activityService.changeOptionsFromMenu(user, sendMessage, inputMessage);
            var response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .sendMessage(answer)
                    .build();
            telegramBot.sendAnswer(response);
        }
        else if(PREPARE_GAME.equals(userState)) {
            var answer = prepareGameService.optionsPrepareGameText(user, sendMessage, inputMessage);
            var response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .sendMessage(answer)
                    .build();
            telegramBot.sendAnswer(response);
        }
        else if(PREPARE_MANUALLY.equals(userState)) {
            var answer = prepareManuallyService.optionsPrepareManuallyText(user, sendMessage, inputMessage);
            var response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .sendMessage(answer)
                    .build();
            telegramBot.sendAnswer(response);
        }
        else if(PREPARE_AUTOMATIC.equals(userState)) {
            var answer = prepareAutomaticService.optionsPrepareAutomaticText(user, sendMessage, inputMessage);
            var response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .sendMessage(answer)
                    .build();
            telegramBot.sendAnswer(response);
        }
        else if(SEARCH_GAME.equals(userState)) {
            var answer = getInfo("Идет подготовка сражения...", sendMessage);
            answer.setReplyMarkup(InlineButton.stopSearchGameButton());
            var response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .sendMessage(answer)
                    .build();
            telegramBot.sendAnswer(response);
        }
        else if(IN_GAME.equals(userState)) {
            var answer = inGameService.processTextMessage(user, sendMessage, inputMessage);
            var response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .sendMessage(answer)
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
    }

    //перенаправляет inline запросы
    @Override
    public void processCallbackQuery(TelegramBot telegramBot, Update update) {
        var user = userService.findOrSaveUser(update);
        var userState = user.getState();
        var inputMessage = update.getCallbackQuery().getData();
        var sendMessage = new SendMessage();
        sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
        var answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(update.getCallbackQuery().getId());

        if(WAIT_REGISTRATION.equals(userState)) {
            var answer = registrationService.processRegistrationInline(user, sendMessage, inputMessage);
            var response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .sendMessage(answer)
                    .answerCallbackQuery(answerCallbackQuery)
                    .build();
            telegramBot.sendAnswer(response);
        }
        else if(ONLINE.equals(userState)) {
            var answer = activityService.changeOptions(user, sendMessage, inputMessage);
            var response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .sendMessage(answer)
                    .answerCallbackQuery(answerCallbackQuery)
                    .build();
            telegramBot.sendAnswer(response);
        }
        else if(PREPARE_GAME.equals(userState)) {
            var answer = prepareGameService.optionsPrepareGameInline(user, sendMessage, inputMessage);
            var response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .sendMessage(answer)
                    .answerCallbackQuery(answerCallbackQuery)
                    .build();
            telegramBot.sendAnswer(response);
        }
        else if(PREPARE_MANUALLY.equals(userState)) {
            var answer = prepareManuallyService.prepareGameManuallyInline(user, sendMessage, inputMessage);
            var response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .sendMessage(answer)
                    .answerCallbackQuery(answerCallbackQuery)
                    .build();
            telegramBot.sendAnswer(response);
        }
        else if(PREPARE_AUTOMATIC.equals(userState)) {
            var answer = prepareAutomaticService.prepareGameAutomaticInline(user, sendMessage, inputMessage);
                    var response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .sendMessage(answer)
                    .answerCallbackQuery(answerCallbackQuery)
                    .build();
            telegramBot.sendAnswer(response);
        }
        else if(SEARCH_GAME.equals(userState)) {
            var answer = searchGameService.stopSearchGame(user, sendMessage, inputMessage);
            var response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .sendMessage(answer)
                    .answerCallbackQuery(answerCallbackQuery)
                    .build();
            telegramBot.sendAnswer(response);
        }
        else if(IN_GAME.equals(userState)) {
            var answer = inGameService.processCallbackQuery(update, user, sendMessage, inputMessage);

            var response = ResponseDto.builder()
                    .telegramBot(telegramBot)
                    .sendMessage(answer)
                    .answerCallbackQuery(answerCallbackQuery)
                    .build();
            telegramBot.sendAnswer(response);
        }
    }

    private SendMessage getInfo(String msg, SendMessage sendMessage) {
        sendMessage.setText(msg);
        return sendMessage;
    }
}
