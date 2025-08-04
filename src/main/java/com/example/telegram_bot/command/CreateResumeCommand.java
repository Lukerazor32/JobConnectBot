package com.example.telegram_bot.command;

import com.example.telegram_bot.Entity.User;
import com.example.telegram_bot.service.ResumeService;
import com.example.telegram_bot.service.SendBotMessageService;
import com.example.telegram_bot.service.TelegramUserService;
import com.example.telegram_bot.state.State;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CreateResumeCommand implements State {
    private final SendBotMessageService sendBotMessageService;
    private final TelegramUserService telegramUserService;
    private final ResumeService resumeServiceImpl;

    public CreateResumeCommand(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService, ResumeService resumeServiceImpl) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
        this.resumeServiceImpl = resumeServiceImpl;
    }

    @Override
    public void startState(Update update, User user) {

    }

    @Override
    public void execute(Update update, User user) {

    }

    @Override
    public void undo() {

    }
}
