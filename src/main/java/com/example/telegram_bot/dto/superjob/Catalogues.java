package com.example.telegram_bot.dto.superjob;

import lombok.Getter;

import java.util.List;

@Getter
public class Catalogues {
    private String title_rus;
    private String url_rus;
    private String title;
    private String title_trimmed;
    private int key;
    private List<Positions> positions;
}
