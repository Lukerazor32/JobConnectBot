package com.example.telegram_bot.Entity;

import com.example.telegram_bot.state.State;

import lombok.Data;

@Data
public class User {
    private Long chatId;
    private State state;
    private State oldState;
    private String token;
}
