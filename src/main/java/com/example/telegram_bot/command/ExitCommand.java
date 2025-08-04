package com.example.telegram_bot.command;

import com.example.telegram_bot.Entity.User;
import com.example.telegram_bot.service.SendBotMessageService;
import com.example.telegram_bot.state.State;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ExitCommand implements State {
    private final SendBotMessageService sendBotMessageService;

    public ExitCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void startState(Update update, User user) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId(), "Вы вышли из команды");
    }

    @Override
    public void execute(Update update, User user) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId(), "Введите команду");
    }

    @Override
    public void undo() {

    }

}
