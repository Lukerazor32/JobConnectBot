package com.example.telegram_bot.service;

import com.example.telegram_bot.command.StartCommand;
import com.example.telegram_bot.dto.superjob.AccessTokenData;

import java.util.List;
import java.util.Map;

public interface SuperJobAuth {
    String getAuthURL(String chatId);
    void setAuthCode(String code, String chatId);
    void setAccessToken(String code, String chatId);
    List<String> refreshToken(String refreshToken);
    Map<Long, String[]> getTokens();
}
