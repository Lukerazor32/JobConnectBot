package com.example.telegram_bot.service.impl;

import com.example.telegram_bot.dto.superjob.VacancyResponseObject;
import com.example.telegram_bot.dto.superjob.resume.ResumeData;
import com.example.telegram_bot.repository.MyUtils;
import com.example.telegram_bot.service.ResumeService;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ResumeServiceImpl implements ResumeService {
    private final String superJobAPIPath;
    private final String secretKey;

    public ResumeServiceImpl(@Value("${superjob.api.path}") String superJobAPIPath,
                                   @Value("${superjob.api.secret-key}") String secretKey) {
        this.superJobAPIPath = superJobAPIPath;
        this.secretKey = secretKey;
    }
    @Override
    public ResumeData getResumes(String authToken) {
        Map<String, String> headerProp = new HashMap<>();
        headerProp.put("Host", "api.superjob.ru");
        headerProp.put("X-Api-App-Id", secretKey);
        headerProp.put("Authorization", String.format("Bearer %s", authToken));
        HttpResponse<ResumeData> response = Unirest.get(String.format("%s/user_cvs/", superJobAPIPath))
                .headers(headerProp)
                .asObject(ResumeData.class);
        ResumeData resumeData = null;
        if (response.getStatus() == 200) {
            resumeData = response.getBody();
        } else if (response.getStatus() == 410) {
            MyUtils.updateToken(authToken);
            getResumes(authToken);
        }

        return resumeData;
    }

    @Override
    public void createResume(String authToken) {

    }

    @Override
    public VacancyResponseObject getVacancyResponses(String authToken) {
        Map<String, String> headerProp = new HashMap<>();
        headerProp.put("Host", "api.superjob.ru");
        headerProp.put("X-Api-App-Id", secretKey);
        headerProp.put("Authorization", String.format("Bearer %s", authToken));
        HttpResponse<VacancyResponseObject> response = Unirest.get(String.format("%s/messages/history/all/", superJobAPIPath))
                .headers(headerProp)
                .asObject(VacancyResponseObject.class);
        VacancyResponseObject resumeData = null;
        if (response.getStatus() == 200) {
            resumeData = response.getBody();
        } else if (response.getStatus() == 410) {
            MyUtils.updateToken(authToken);
            getVacancyResponses(authToken);
        }

        return resumeData;
    }

    @Override
    public VacancyResponseObject getVacancyResponsesByResume(String authToken, int idResume) {
        Map<String, String> headerProp = new HashMap<>();
        headerProp.put("Host", "api.superjob.ru");
        headerProp.put("X-Api-App-Id", secretKey);
        headerProp.put("Authorization", String.format("Bearer %s", authToken));
        HttpResponse<VacancyResponseObject> response = Unirest.get(String.format("%s/messages/history/all/%s/", superJobAPIPath, idResume))
                .headers(headerProp)
                .asObject(VacancyResponseObject.class);
        VacancyResponseObject resumeData = null;
        if (response.getStatus() == 200) {
            resumeData = response.getBody();
        } else if (response.getStatus() == 410) {
            MyUtils.updateToken(authToken);
            getVacancyResponses(authToken);
        }

        return resumeData;
    }


}
