package com.example.telegram_bot.service.impl;

import com.example.telegram_bot.dto.superjob.Vacancy;
import com.example.telegram_bot.dto.superjob.VacancyResponse;
import com.example.telegram_bot.dto.superjob.VacancyResponseObject;
import com.example.telegram_bot.service.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class FindNewVacanciesServiceImpl implements FindNewVacanciesService {
    private final SendBotMessageService sendBotMessageService;
    private final TelegramUserService telegramUserService;
    private final SuperJobUserService superJobUserService;
    private final VacancyService vacancyService;
    private final ResumeService resumeService;

    public FindNewVacanciesServiceImpl(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService, SuperJobUserService superJobUserService, VacancyService vacancyService, ResumeService resumeService) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
        this.superJobUserService = superJobUserService;
        this.vacancyService = vacancyService;
        this.resumeService = resumeService;
    }

    @Override
    @Scheduled(fixedRateString = "${bot.recountNewVacancyFixedRate}")
    public void findNewVacancies() {
        telegramUserService.findAll().forEach(
                telegramUser -> {
                    Integer resumeId = telegramUser.getResumeSubscriptionId();
                    String token = telegramUser.getAccessToken();
                    Long dateLastVacancy = telegramUser.getLastDateVacancy();
                    if (resumeId != null && token != null && telegramUser.getIsSubscript() != false) {
                        List<Vacancy> sortedVacancies = vacancyService.getVacanciesOrderByDate(token, resumeId);
                        if (dateLastVacancy == null) {
                            telegramUser.setLastDateVacancy((long) sortedVacancies.get(0).getDate_published());
                            telegramUserService.save(telegramUser);
                            return;
                        }

                        List<Vacancy> newVacancy = new ArrayList<>();
                        for (Vacancy vacancy : sortedVacancies) {
                            if (vacancy.getDate_published() > dateLastVacancy) {
                                newVacancy.add(vacancy);
                            } else {
                                break;
                            }
                            if (newVacancy.size() >= 15) {
                                break;
                            }
                        }

                        Collections.reverse(newVacancy);
                        if (newVacancy.size() > 0) {
                            for (Vacancy vacancy : newVacancy) {
                                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

                                VacancyResponseObject vacancyResponseObj = resumeService.getVacancyResponsesByResume(token, resumeId);

                                for (VacancyResponse vacancyResp : vacancyResponseObj.getObjects()) {
                                    if (vacancyResp.getId_vacancy() == vacancy.getId()) {
                                        rowsInline.add(sendBotMessageService.createRow("Отклик отправлен", "SubscriptResponseTrue " + vacancy.getId()));
                                        break;
                                    }
                                }
                                if (rowsInline.size() == 0) {
                                    rowsInline.add(sendBotMessageService.createRow("Отправить отклик на вакансию",
                                            "SubscriptResponseFalse "
                                            + resumeId + " "
                                            + vacancy.getId() + " "));
                                }

                                markupInline.setKeyboard(rowsInline);
                                sendBotMessageService.setReplyMarkup(markupInline);
                                sendBotMessageService.sendMessage(telegramUser.getChatId(), String.format("Найдена новая вакансия!\n\n%s", vacancy.toString()));
                            }
                            telegramUser.setLastDateVacancy((long) newVacancy.get(newVacancy.size()-1).getDate_published());
                            telegramUserService.save(telegramUser);
                        }
                    }
                }
        );
    }
}
