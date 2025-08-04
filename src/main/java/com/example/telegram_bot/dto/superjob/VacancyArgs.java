package com.example.telegram_bot.dto.superjob;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class VacancyArgs {
    private final int idResume;
    private final String order_field;
}
