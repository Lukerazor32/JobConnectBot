package com.example.telegram_bot.service.impl;

import com.example.telegram_bot.dto.superjob.*;
import com.example.telegram_bot.repository.MyUtils;
import com.example.telegram_bot.service.SuperJobUserService;
import com.google.gson.JsonObject;
import kong.unirest.GenericType;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SuperJobUserServiceImpl implements SuperJobUserService {
    private final String superJobAPIPath;
    private final String secretKey;

    public SuperJobUserServiceImpl(@Value("${superjob.api.path}") String superJobAPIPath,
                            @Value("${superjob.api.secret-key}") String secretKey) {
        this.superJobAPIPath = superJobAPIPath;
        this.secretKey = secretKey;
    }

    @Override
    public boolean createSubscriptVacancy(String authToken, SubscriptionArgs args) {
        Map<String, String> headerProp = new HashMap<>();
        headerProp.put("Host", "api.superjob.ru");
        headerProp.put("X-Api-App-Id", secretKey);
        headerProp.put("Authorization", String.format("Bearer %s", authToken));
        headerProp.put("Content-Type", "application/json");
        HttpResponse<JsonObject> response = Unirest.post(String.format("%s/subscriptions/", superJobAPIPath))
                .headers(headerProp)
                .body(args.populateQueries())
                .asObject(JsonObject.class);
        if (response.getStatus() == 200 && response.getBody().has("id")) {
            return true;
        } else if (response.getStatus() == 410) {
            MyUtils.updateToken(authToken);
            createSubscriptVacancy(authToken, args);
        }
        return false;
    }

    @Override
    public TownObject getTowns() {
        return Unirest.post(String.format("%s/towns/", superJobAPIPath))
                .asObject(TownObject.class)
                .getBody();
    }

    @Override
    public TownObject getTowns(String townTitle) {
        return Unirest.post(String.format("%s/towns/", superJobAPIPath))
                .queryString("keyword", townTitle)
                .asObject(TownObject.class)
                .getBody();
    }

    @Override
    public List<Catalogues> getCategories() {
        return Unirest.post(String.format("%s/catalogues/", superJobAPIPath))
                .asObject(new GenericType<List<Catalogues>>() {
                })
                .getBody();
    }

    @Override
    public List<Positions> getPositions(int categoryId) {
        return Unirest.post(String.format("%s/catalogues/parent/%s/", superJobAPIPath, categoryId))
                .asObject(new GenericType<List<Positions>>() {
                })
                .getBody();
    }
}
