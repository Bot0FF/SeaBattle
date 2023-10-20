package org.bot0ff.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bot0ff.component.button.InlineButton;
import org.bot0ff.entity.User;
import org.bot0ff.service.RegistrationService;
import org.bot0ff.service.UserService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.bot0ff.entity.UserState.ONLINE;
import static org.bot0ff.service.ServiceCommands.*;

@Log4j
@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    private final UserService userService;

    //TODO проверка на существующее имя в бд
    //регистрация с выбором имени
    @Override
    public SendMessage processRegistration(User user, SendMessage sendMessage, String cmd) {
        if(START.equals(cmd)) {
            sendMessage.setText("""
                    Добро пожаловать в игру "Морской Бой"!
                    Это классическая игра, где можно играть с реальными противниками или с ИИ.
                    Для начала введите желаемое имя и отправьте его или нажмите кнопку "Оставить как есть\"""");
            sendMessage.setReplyMarkup(InlineButton.registrationButton());
        }
        else if(HELP.equals(cmd)) {
            sendMessage.setText("Внимательно прочтите правила игры");
        }
        else if(CANCEL.equals(cmd)) {
            sendMessage.setText("Для начала введите желаемое имя и отправьте его или нажмите кнопку \"Оставить как есть\"");
            sendMessage.setReplyMarkup(InlineButton.registrationButton());
        }
        else if(cmd.length() > 10 | cmd.length() < 3){
            sendMessage.setText("Имя должно быть не больше 10 символов и не меньше 3. " +
                    "Для продолжения введите желаемое имя и отправьте его или нажмите кнопку \"Оставить как есть\"");
            sendMessage.setReplyMarkup(InlineButton.registrationButton());
        }
        else {
            user.setName(cmd);
            user.setState(ONLINE);
            userService.saveUser(user);
            sendMessage.setText("Выберите что хотите сделать");
            sendMessage.setReplyMarkup(InlineButton.changeOptions());
        }
        return sendMessage;
    }

    //TODO проверка на существующее имя в бд
    //регистрация с выбором имени автоматически
    @Override
    public SendMessage processRegistrationAuto(User user, SendMessage sendMessage, String cmd) {
        if(cmd.equals("/newUserWithCurrentName")) {
            user.setName(user.getName());
            user.setState(ONLINE);
            userService.saveUser(user);
            sendMessage.setText("Выберите что хотите сделать");
            sendMessage.setReplyMarkup(InlineButton.changeOptions());
        }
        else {
            sendMessage.setText("Для начала введите желаемое имя и отправьте его или нажмите кнопку \"Оставить как есть\"");
            sendMessage.setReplyMarkup(InlineButton.registrationButton());
        }
        return sendMessage;
    }
}
