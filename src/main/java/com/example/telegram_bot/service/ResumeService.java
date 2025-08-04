package com.example.telegram_bot.service;

import com.example.telegram_bot.dto.superjob.SubscriptionArgs;
import com.example.telegram_bot.dto.superjob.VacancyResponseObject;
import com.example.telegram_bot.dto.superjob.resume.ResumeData;

public interface ResumeService {
    ResumeData getResumes(String authToken);

    void createResume(String authToken);

    VacancyResponseObject getVacancyResponses(String authToken);

    VacancyResponseObject getVacancyResponsesByResume(String authToken, int idResume);
}
