package com.example.telegram_bot.command;

import com.example.telegram_bot.Entity.User;
import com.example.telegram_bot.bot.JobConnect;
import com.example.telegram_bot.service.SendBotMessageService;
import com.example.telegram_bot.service.TelegramUserService;
import com.example.telegram_bot.state.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StatCommand implements State {
    private final TelegramUserService telegramUserService;
    private final SendBotMessageService sendBotMessageService;
    private final JobConnect jobConnect;

    public final static String STAT_MESSAGE = "Music bot использует %s человек.";

    @Autowired
    public StatCommand(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService, JobConnect jobConnect) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
        this.jobConnect = jobConnect;
    }

    @Override
    public void startState(Update update, User user) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId(), "Высылаю статистику...");
        execute(update, user);
    }

    @Override
    public void execute(Update update, User user) {
//        int activeUserCount = telegramUserService.retrieveAllActiveUsers().size();
//        sendBotMessageService.sendMessage(update.getMessage().getChatId(), String.format(STAT_MESSAGE, activeUserCount));
        user.setState(new NoCommand(sendBotMessageService));
    }

    @Override
    public void undo() {

    }

}
