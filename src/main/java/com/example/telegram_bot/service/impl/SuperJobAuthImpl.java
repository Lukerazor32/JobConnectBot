package com.example.telegram_bot.service.impl;

import com.example.telegram_bot.dto.superjob.AccessTokenData;
import com.example.telegram_bot.service.SuperJobAuth;
import kong.unirest.GenericType;
import kong.unirest.Unirest;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SuperJobAuthImpl implements SuperJobAuth {
    private final String superJobPath = "https://www.superjob.ru";
    private final String superJobAPIPath;
    private final String secretKey;

    private final String client_id;
    private final String callback;

    @Getter
    private Map<Long, String[]> tokensMap;

    public SuperJobAuthImpl(@Value("${superjob.api.path}") String superJobAPIPath,
                            @Value("${superjob.api.secret-key}") String secretKey,
                            @Value("${superjob.api.id}") String client_id,
                            @Value("${superjob.api.callback}") String callback) {
        this.superJobAPIPath = superJobAPIPath;
        this.secretKey = secretKey;
        this.client_id = client_id;
        this.callback = callback;
        tokensMap = new HashMap<>();
    }

    @Override
    public String getAuthURL(String chatId) {
        return String.format("<a href='%s'>Авторизоваться</a>", Unirest.get(String.format("%s/authorize/", superJobPath))
                .queryString("client_id", client_id)
                .queryString("redirect_uri", callback)
                .queryString("state", chatId)
                .getUrl());
    }

    @GetMapping("/auth")
    @Override
    public void setAuthCode(@RequestParam("code") String code,
                            @RequestParam("state") String chatId) {
        setAccessToken(code, chatId);
    }

    @Override
    public void setAccessToken(String code, String chatId) {
        String[] accessData = new String[2];
        AccessTokenData accessTokenData = Unirest.get(String.format("%s/oauth2/access_token/", superJobAPIPath))
                .queryString("code", code)
                .queryString("redirect_uri", callback)
                .queryString("client_id", client_id)
                .queryString("client_secret", secretKey)
                .asObject(new GenericType<AccessTokenData>() {
                })
                .getBody();
        accessData[0] = accessTokenData.getAccess_token();
        accessData[1] = accessTokenData.getRefresh_token();

        tokensMap.put(Long.parseLong(chatId), accessData);
    }

    @Override
    public List<String> refreshToken(String refreshToken) {
        return null;
    }

    @Override
    public Map<Long, String[]> getTokens() {
        return tokensMap;
    }
}
