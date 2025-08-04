package com.example.telegram_bot.dto.superjob;

import lombok.Data;

import java.util.List;

@Data
public class VacancyResponseObject {
    private List<VacancyResponse> objects;
    private int total;
    private boolean more;
}
