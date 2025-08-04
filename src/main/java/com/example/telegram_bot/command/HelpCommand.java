package com.example.telegram_bot.command;

import com.example.telegram_bot.Entity.User;
import com.example.telegram_bot.bot.JobConnect;
import com.example.telegram_bot.service.SendBotMessageService;
import com.example.telegram_bot.state.State;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.example.telegram_bot.command.CommandName.*;

public class HelpCommand implements State {
    private final SendBotMessageService sendBotMessageService;

    public HelpCommand(SendBotMessageService sendBotMessageService, JobConnect jobConnect) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void startState(Update update, User user) {
        user.setState(new NoCommand(sendBotMessageService));
    }

    @Override
    public void execute(Update update, User user) {

    }

    @Override
    public void undo() {

    }

}
