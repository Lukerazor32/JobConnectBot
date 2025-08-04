package com.example.telegram_bot.command;

import com.example.telegram_bot.Entity.User;
import com.example.telegram_bot.bot.JobConnect;
import com.example.telegram_bot.service.SendBotMessageService;
import com.example.telegram_bot.state.State;
import org.telegram.telegrambots.meta.api.objects.Update;

public class UnknownCommand implements State {
    public static final String UNKNOWN_MESSAGE = "Команда не распознана :(";

    private final SendBotMessageService sendBotMessageService;
    private final JobConnect jobConnect;

    public UnknownCommand(SendBotMessageService sendBotMessageService, JobConnect jobConnect) {
        this.sendBotMessageService = sendBotMessageService;
        this.jobConnect = jobConnect;
    }

    @Override
    public void startState(Update update, User user) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId(), UNKNOWN_MESSAGE);
        user.setState(new NoCommand(sendBotMessageService));
    }

    @Override
    public void execute(Update update, User user) {

    }

    @Override
    public void undo() {

    }

}
