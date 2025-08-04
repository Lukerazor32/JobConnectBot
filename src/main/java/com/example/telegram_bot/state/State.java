package com.example.telegram_bot.state;

import com.example.telegram_bot.Entity.User;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface State {
    void startState(Update update, User user);

    void execute(Update update, User user);

    void undo();
}
