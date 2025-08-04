package com.example.telegram_bot.service.impl;

import com.example.telegram_bot.dto.superjob.Vacancy;
import com.example.telegram_bot.dto.superjob.VacancyObject;
import com.example.telegram_bot.repository.MyUtils;
import com.example.telegram_bot.service.TelegramUserService;
import com.example.telegram_bot.service.VacancyService;
import kong.unirest.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VacancyServiceImpl implements VacancyService {
    private final String superJobAPIPath;
    private final String secretKey;
    private final TelegramUserService telegramUserService;

    public VacancyServiceImpl(@Value("${superjob.api.path}") String superJobAPIPath,
                              @Value("${superjob.api.secret-key}") String secretKey, TelegramUserService telegramUserService) {
        this.superJobAPIPath = superJobAPIPath;
        this.secretKey = secretKey;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public List<Vacancy> getVacancies(String authToken, int idResume, int page) {
        Map<String, String> headerProp = new HashMap<>();
        headerProp.put("Host", "api.superjob.ru");
        headerProp.put("X-Api-App-Id", secretKey);
        headerProp.put("Authorization", String.format("Bearer %s", authToken));


        HttpResponse<VacancyObject> response = Unirest.get(String.format("%s/vacancies/", superJobAPIPath))
                .headers(headerProp)
                .queryString("id_resume", idResume)
                .queryString("page", page)
                .queryString("count", 100)
                .queryString("order_field", "date")
                .queryString("order_direction", "desc")
                .asObject(VacancyObject.class);
        List<Vacancy> vacancies = null;
        if (response.getStatus() == 200) {
            vacancies = response.getBody().getObjects();
        }
        return vacancies;
    }

    @Override
    public List<Vacancy> getVacanciesOrderByDate(String authToken, int idResume) {
        Map<String, String> headerProp = new HashMap<>();
        headerProp.put("Host", "api.superjob.ru");
        headerProp.put("X-Api-App-Id", secretKey);
        headerProp.put("Authorization", String.format("Bearer %s", authToken));

        boolean more = true;
        int page = 1;
        List<Vacancy> vacancies = new ArrayList<>();
        while (more) {
            HttpResponse<VacancyObject> response = null;
            try {
                 response = Unirest.get(String.format("%s/vacancies/", superJobAPIPath))
                        .headers(headerProp)
                        .queryString("id_resume", idResume)
                        .queryString("page", page)
                        .queryString("order_by[updated_at]", "desc")
                        .asObject(VacancyObject.class);
            } catch (UnirestException e) {
                e.getMessage();
            }

            if (response != null && response.getStatus() == 200) {
                vacancies.addAll(response.getBody().getObjects());
                more = response.getBody().isMore();
                page++;
            } else if (response.getStatus() == 410) {
                MyUtils.updateToken(authToken);
                getVacanciesOrderByDate(authToken, idResume);
            }
        }
        Collections.sort(vacancies);

        return vacancies;
    }

    @Override
    public Vacancy getVacancyById(String authToken, int idVacancy) {
        Map<String, String> headerProp = new HashMap<>();
        headerProp.put("Host", "api.superjob.ru");
        headerProp.put("X-Api-App-Id", secretKey);
        headerProp.put("Authorization", String.format("Bearer %s", authToken));


        HttpResponse<Vacancy> response = Unirest.get(String.format("%s/vacancies/%s", superJobAPIPath, idVacancy))
                .headers(headerProp)
                .asObject(Vacancy.class);
        Vacancy vacancy = null;
        if (response.getStatus() == 200) {
            vacancy = response.getBody();
        } else if (response.getStatus() == 410) {
            MyUtils.updateToken(authToken);
            getVacancyById(authToken, idVacancy);
        }
        return vacancy;
    }


    @Override
    public boolean sendResponseToVacancy(String authToken, int idResume, int idVacancy) {
        Map<String, String> headerProp = new HashMap<>();
        headerProp.put("Host", "api.superjob.ru");
        headerProp.put("X-Api-App-Id", secretKey);
        headerProp.put("Authorization", String.format("Bearer %s", authToken));
        HttpResponse<JsonResponse> response =
                Unirest.post(String.format("%s/send_cv_on_vacancy/", superJobAPIPath))
                        .headers(headerProp)
                        .queryString("id_cv", idResume)
                        .queryString("id_vacancy", idVacancy)
                        .asObject(JsonResponse.class);
        if (response.getStatus() == 200) {
            return true;
        } else if (response.getStatus() == 410) {
            MyUtils.updateToken(authToken);
            sendResponseToVacancy(authToken, idResume, idVacancy);
        }
        return false;
    }



}
