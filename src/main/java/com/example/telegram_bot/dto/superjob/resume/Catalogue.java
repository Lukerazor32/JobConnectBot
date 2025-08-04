package com.example.telegram_bot.dto.superjob.resume;

import lombok.Getter;

@Getter
public class Catalogue {
    private int id;
    private String title;
    private Position[] positions;
}
