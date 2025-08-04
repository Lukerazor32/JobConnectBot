package com.example.telegram_bot.dto.superjob;

import lombok.Getter;

import java.util.Date;

@Getter
public class VacancyResponse {
    private int id_vacancy;
    private Vacancy vacancy;
    private int id_resume;
    private String firm_name;
    private String contact_face;
    private long date_sent;
    private long date_updated;
    private long date;
    private String position_name;
    private boolean archive;
    private boolean storage;
    private String resume_additional_info;
    private String status_text;
    private int status;

    @Override
    public String toString() {
        StringBuilder strBuilder = new StringBuilder();

        strBuilder.append(String.format("<b>%s</b>", position_name) + "\n");
        strBuilder.append(String.format("<b>Компания:</b> %s", firm_name) + "\n\n");
        strBuilder.append(String.format("<b>Статус ответа:</b> %s", status_text));
        return strBuilder.toString();
    }
}
