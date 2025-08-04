package com.example.telegram_bot.command;

import com.example.telegram_bot.Entity.User;
import com.example.telegram_bot.dto.superjob.resume.ResumeData;
import com.example.telegram_bot.repository.entity.TelegramUser;
import com.example.telegram_bot.service.*;
import com.example.telegram_bot.state.State;
import kong.unirest.HttpResponse;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class StartCommand implements State {
    private final SendBotMessageService sendBotMessageService;
    private final TelegramUserService telegramUserService;
    private final SuperJobAuth superJobAuth;

    private boolean isAuth = false;

    public final static String START_MESSAGE = "Приветствую!\nЭто телеграм-бот для поиска работы! Для начала пройди авторизацию: \n%s";

    public StartCommand(SendBotMessageService sendBotMessageService,
                        TelegramUserService telegramUserService,
                        SuperJobAuth superJobAuth) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
        this.superJobAuth = superJobAuth;
    }

    @Override
    public void startState(Update update, User user) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            telegramUserService.findByChatId(user.getChatId()).ifPresent(
                    oldUser -> {
                        if(oldUser.getAccessToken() != null && oldUser.getRefreshToken() != null) {
                            isAuth = true;
                        }
                    }
            );
            if (!isAuth) {
                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                rowsInline.add(sendBotMessageService.createRow("Проверить авторизацию", "checkAuth"));
                markupInline.setKeyboard(rowsInline);

                sendBotMessageService.setReplyMarkup(markupInline);
                sendBotMessageService.sendMessage(user.getChatId(), String.format(START_MESSAGE, superJobAuth.getAuthURL(user.getChatId().toString())));
            }
        } else if (update.hasCallbackQuery() || isAuth) {
            execute(update, user);
        }
    }

    @Override
    public void execute(Update update, User user) {
        telegramUserService.findByChatId(user.getChatId()).ifPresentOrElse(
                oldUser -> {
                    if (update.hasCallbackQuery() && oldUser.getAccessToken() == null) {
                        setAuth(oldUser, user);
                    } else {
                        user.setState(new NoCommand(sendBotMessageService));
                    }
                },
                () -> {
                    TelegramUser newUser = new TelegramUser();
                    if (update.hasMessage()) {
                        newUser.setUsername(update.getMessage().getFrom().getUserName());
                        newUser.setFirstName(update.getMessage().getFrom().getFirstName());
                        newUser.setLastName(update.getMessage().getFrom().getLastName());
                    } else {
                        newUser.setUsername(update.getCallbackQuery().getFrom().getUserName());
                        newUser.setFirstName(update.getCallbackQuery().getFrom().getFirstName());
                        newUser.setLastName(update.getCallbackQuery().getFrom().getLastName());
                    }
                    newUser.setChatId(user.getChatId());
                    newUser.setLastDateViewResponse(Instant.now().getEpochSecond());
                    newUser.setIsSubscript(false);
                    setAuth(newUser, user);
                    telegramUserService.save(newUser);
                }
        );
    }

    @Override
    public void undo() {

    }

    private boolean setAuth(TelegramUser telegramUser, User user) {
        String accessToken;
        try {
            accessToken = superJobAuth.getTokens().get(telegramUser.getChatId())[0];
        } catch (NullPointerException e) {
            return false;
        }
        String refreshToken = superJobAuth.getTokens().get(telegramUser.getChatId())[1];

        user.setToken(accessToken);
        telegramUser.setAccessToken(accessToken);
        telegramUser.setRefreshToken(refreshToken);
        telegramUserService.save(telegramUser);
        sendBotMessageService.sendMessage(user.getChatId(), "Авторизация прошла успешно");
        user.setState(new NoCommand(sendBotMessageService));
        return true;
    }
}
