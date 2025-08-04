package com.example.telegram_bot.dto.superjob.resume;

import lombok.Getter;

@Getter
public class WorkHistory {
    private String town;
    private String name;
    private String profession;
    private String work;
    private Type type;
    private int monthbeg;
    private int monthend;
    private int yearbeg;
    private int yearend;


}
