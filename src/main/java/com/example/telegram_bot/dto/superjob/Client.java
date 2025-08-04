package com.example.telegram_bot.dto.superjob;

import com.example.telegram_bot.dto.superjob.resume.Town;
import lombok.Getter;

import java.util.List;

@Getter
public class Client {
    private int id;
    private String title;
    private String link;
    private List<Object> industry;
    private String description;
    private int vacancy_count;
    private String staff_count;
    private String client_logo;
    private String address;
    private List<Object> addresses;
    private String url;
    private boolean short_reg;
    private boolean is_blocked;
    private int registered_date;
    private Town town;
}
