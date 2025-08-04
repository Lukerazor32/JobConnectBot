package com.example.telegram_bot.command.subscript;

import com.example.telegram_bot.Entity.User;
import com.example.telegram_bot.command.NoCommand;
import com.example.telegram_bot.dto.superjob.SubscriptionArgs;
import com.example.telegram_bot.service.SendBotMessageService;
import com.example.telegram_bot.service.SuperJobUserService;
import com.example.telegram_bot.service.TelegramUserService;
import com.example.telegram_bot.state.State;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.Instant;
import java.util.Date;

public class SubscriptVacancyCommand implements State {
    private final SendBotMessageService sendBotMessageService;
    private final SuperJobUserService superJobUserService;
    private final TelegramUserService telegramUserService;
    private Integer resumeId;

    public SubscriptVacancyCommand(SendBotMessageService sendBotMessageService,
                                   SuperJobUserService superJobUserService,
                                   TelegramUserService telegramUserService,
                                   Integer resumeId) {
        this.sendBotMessageService = sendBotMessageService;
        this.superJobUserService = superJobUserService;
        this.telegramUserService = telegramUserService;
        this.resumeId = resumeId;
    }

    @Override
    public void startState(Update update, User user) {
        telegramUserService.findByChatId(user.getChatId()).ifPresent(
                telegramUser -> {
                    telegramUser.setResumeSubscriptionId(resumeId);
                    telegramUser.setIsSubscript(true);
                    telegramUser.setLastDateVacancy(Instant.now().getEpochSecond());
                    telegramUserService.save(telegramUser);
                }
        );

        sendBotMessageService.sendMessage(user.getChatId(), "Подписка на вакансии успешна!");
        user.setState(new NoCommand(sendBotMessageService));
    }

    @Override
    public void execute(Update update, User user) {

    }

    @Override
    public void undo() {

    }
}
