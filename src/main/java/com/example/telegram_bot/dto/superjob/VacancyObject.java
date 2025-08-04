package com.example.telegram_bot.dto.superjob;

import lombok.Getter;

import java.util.List;

@Getter
public class VacancyObject {
    private List<Vacancy> objects;
    private int total;
    private boolean more;
    private int subscription_id;
    private boolean subscription_active;
}
