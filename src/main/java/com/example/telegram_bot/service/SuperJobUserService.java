package com.example.telegram_bot.service;

import com.example.telegram_bot.dto.superjob.*;

import java.util.List;

public interface SuperJobUserService {
    boolean createSubscriptVacancy(String authToken, SubscriptionArgs args);

    TownObject getTowns();

    TownObject getTowns(String townTitle);

    List<Catalogues> getCategories();

    List<Positions> getPositions(int categoryId);
}
