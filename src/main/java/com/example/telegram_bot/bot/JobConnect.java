package com.example.telegram_bot.bot;

import com.example.telegram_bot.Entity.User;
import com.example.telegram_bot.command.CommandContainer;
import com.example.telegram_bot.command.CommandName;
import com.example.telegram_bot.command.NoCommand;
import com.example.telegram_bot.command.StartCommand;
import com.example.telegram_bot.command.subscript.CheckVacancyRequestCommand;
import com.example.telegram_bot.dto.superjob.Vacancy;
import com.example.telegram_bot.repository.entity.TelegramUser;
import com.example.telegram_bot.service.*;
import com.example.telegram_bot.service.impl.SendBotMessageServiceImpl;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class JobConnect extends TelegramLongPollingBot {

    public static String COMMAND_PREFIX = "/";
    private final CommandContainer commandContainer;
    private final SendBotMessageService sendBotMessageService;
    private final TelegramUserService telegramUserService;
    private final VacancyService vacancyService;
    private final SuperJobAuth superJobAuth;
    private ExecutorService executor;
    private List<User> activeUsers;

    @Value("${bot.username}")
    private String username;

    @Value("${bot.token}")
    private String token;

    private Long chatId;

    public JobConnect(TelegramUserService telegramUserService,
                      SuperJobAuth superJobAuth,
                      SuperJobUserService spUserService,
                      ResumeService resumeService,
                      VacancyService vacancyService) {
        sendBotMessageService = new SendBotMessageServiceImpl(this);
        this.telegramUserService = telegramUserService;
        this.vacancyService = vacancyService;
        this.superJobAuth = superJobAuth;
        this.commandContainer = new CommandContainer(sendBotMessageService,
                telegramUserService,
                this,
                superJobAuth,
                spUserService,
                resumeService,
                vacancyService);
        executor = Executors.newFixedThreadPool(100);
        activeUsers = new ArrayList<>();
        Unirest.config()
                .socketTimeout(5000)
                .connectTimeout(1000)
                .setDefaultHeader("Accept", "application/json");
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        executor.execute(() -> {
            User user = null;
            if (update.hasMessage()) {
                chatId = update.getMessage().getChatId();
            }
            if (update.hasCallbackQuery()) {
                chatId = update.getCallbackQuery().getMessage().getChatId();
            }

            for (User activeUser : activeUsers) {
                if (activeUser.getChatId().equals(chatId)) {
                    user = activeUser;
                    break;
                }
            }

            if (user == null) {
                user = new User();
                user.setState(new StartCommand(sendBotMessageService, telegramUserService, superJobAuth));
                user.setOldState(user.getState());
                user.setChatId(chatId);

                User finalUser = user;
                telegramUserService.findByChatId(chatId).ifPresent(
                        telegramUser -> {
                            finalUser.setToken(telegramUser.getAccessToken());
                        }
                );

                user = finalUser;
                activeUsers.add(user);
            }

            if (checkToken(user)) {
                if (update.hasMessage() && update.getMessage().hasText()) {
                    String message = update.getMessage().getText().trim();

                    for (CommandName value : CommandName.values()) {
                        if (value.getCommandName().equals(message)) {
                            user.getState().undo();
                            user.setState(commandContainer.retrieveCommand(value.getCommandName(), user));
                            user.getState().startState(update, user);
                            return;
                        }
                    }
                }

                if (update.hasCallbackQuery()) {
                    if (update.getCallbackQuery().getData().contains("SubscriptResponseFalse")) {
                        user.setState(new CheckVacancyRequestCommand(sendBotMessageService, vacancyService));
                        user.getState().startState(update, user);
                        return;
                    }
                }

                user.setOldState(user.getState());
                user.getState().execute(update, user);
                if (user.getState() != user.getOldState()) {
                    user.getState().startState(update, user);
                }
            } else {
                user.getState().startState(update, user);
            }
        });
    }

    private boolean checkToken(User user) {
        boolean isAuth = false;
        if (user.getToken() != null) {
            isAuth = true;
        }
        return isAuth;
    }
}
