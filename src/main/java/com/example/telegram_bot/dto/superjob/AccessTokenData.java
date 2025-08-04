package com.example.telegram_bot.dto.superjob;

import lombok.Getter;

@Getter
public class AccessTokenData {
    private String access_token;
    private String refresh_token;
    private int ttl;
    private int expires_in;
    private String bearer;
}
