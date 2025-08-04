package com.example.telegram_bot.repository;

import com.example.telegram_bot.dto.superjob.AccessTokenData;
import com.example.telegram_bot.repository.entity.TelegramUser;
import com.example.telegram_bot.service.TelegramUserService;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;

import java.util.Optional;

public class MyUtils {
    private static final String updateUrl = "https://api.superjob.ru/2.0/oauth2/refresh_token/";
    @Value("${superjob.api.secret-key}")
    private static String secretKey;
    @Value("${superjob.api.id}")
    private static String client_id;

    private static TelegramUserService telegramUserService;

    public static void updateToken(String authToken) {
        telegramUserService.findByAccessToken(authToken).ifPresent(
                telegramUser -> {
                    HttpResponse<AccessTokenData> response = Unirest.get(updateUrl)
                            .queryString("refresh_token", telegramUser.getRefreshToken())
                            .queryString("client_id", client_id)
                            .queryString("client_secret", secretKey)
                            .asObject(AccessTokenData.class);
                    if (response.getStatus() == 200) {
                        AccessTokenData accessTokenData = response.getBody();
                        telegramUser.setAccessToken(accessTokenData.getAccess_token());
                        telegramUser.setRefreshToken(accessTokenData.getRefresh_token());
                        telegramUserService.save(telegramUser);
                    }
                }
        );

    }
}
