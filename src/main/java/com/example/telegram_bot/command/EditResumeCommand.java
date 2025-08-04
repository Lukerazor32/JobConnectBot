package com.example.telegram_bot.command;

import com.example.telegram_bot.Entity.User;
import com.example.telegram_bot.dto.superjob.VacancyResponse;
import com.example.telegram_bot.dto.superjob.VacancyResponseObject;
import com.example.telegram_bot.dto.superjob.resume.ResumeData;
import com.example.telegram_bot.dto.superjob.resume.Resume;
import com.example.telegram_bot.service.ResumeService;
import com.example.telegram_bot.service.SendBotMessageService;
import com.example.telegram_bot.service.TelegramUserService;
import com.example.telegram_bot.state.State;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EditResumeCommand implements State {
    private final SendBotMessageService sendBotMessageService;
    private final TelegramUserService telegramUserService;
    private final ResumeService resumeService;

    private ResumeData resumeData;
    private Long chatId;
    private List<VacancyResponse> newResponses;
    private Message message;
    private int responseIndex;

    public EditResumeCommand(SendBotMessageService sendBotMessageService,
                             TelegramUserService telegramUserService,
                             ResumeService resumeService) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
        this.resumeService = resumeService;
        responseIndex = 0;
    }

    @Override
    public void startState(Update update, User user) {
        chatId = user.getChatId();

        resumeData = resumeService.getResumes(user.getToken());
        if (resumeData.getTotal() > 0) {
            InlineKeyboardMarkup markupResumes = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsResumes = new ArrayList<>();
            Resume[] resumes = resumeData.getObjects();
            for (int i = 0; i < resumes.length; i++) {
                Resume resume = resumes[i];
                String resumeTitle = resume.getProfession() + " " + String.format("(%s)", resume.getPublished().getTitle());
                rowsResumes.add(sendBotMessageService.createRow(resumeTitle, "selectResume " + i));
            }
            markupResumes.setKeyboard(rowsResumes);

            sendBotMessageService.setReplyMarkup(markupResumes);
            sendBotMessageService.sendMessage(chatId, "Список резюме:");

            telegramUserService.findByChatId(chatId).ifPresent(
                    telegramUser -> {
                        Long lastView = telegramUser.getLastDateViewResponse();

                        if (lastView != null) {
                            VacancyResponseObject vacancyResponseObject = resumeService.getVacancyResponses(user.getToken());
                            List<VacancyResponse> vacancyResponse = vacancyResponseObject.getObjects();

                            newResponses = vacancyResponse.stream()
                                    .filter(response -> response.getDate_updated() > lastView)
                                    .filter(response -> response.getStatus() != 0)
                                    .collect(Collectors.toList());

                            if (newResponses.size() > 0) {
                                InlineKeyboardMarkup markupResponses = new InlineKeyboardMarkup();
                                List<List<InlineKeyboardButton>> rowsResponses = new ArrayList<>();
                                rowsResponses.add(sendBotMessageService.createRow("Посмотреть ответы на отклики", "showVacancyResponses"));

                                markupResponses.setKeyboard(rowsResponses);
                                sendBotMessageService.setReplyMarkup(markupResponses);
                                sendBotMessageService.sendMessage(chatId, String.format("Найдено %s ответов на ваши отклики", newResponses.size()));
                            }
                        } else {
                            telegramUser.setLastDateViewResponse(Instant.now().getEpochSecond());
                            telegramUserService.save(telegramUser);
                        }
                    }
            );
        } else {
            sendBotMessageService.sendMessage(chatId, "Резюме не найдено");
        }
    }

    @Override
    public void execute(Update update, User user) {
        if (update.hasCallbackQuery()) {
            String callBack = update.getCallbackQuery().getData();

            if (callBack.contains("selectResume")) {
                int resumeId = Integer.parseInt(callBack.split(" ")[1]);
                Resume selectedResume = resumeData.getObjects()[resumeId];
                if (selectedResume != null) {
                    sendBotMessageService.sendMessage(chatId, selectedResume.toString());
                } else {
                    sendBotMessageService.sendMessage(chatId, "Резюме не найдено");
                }
            }
            if (newResponses != null && newResponses.size() > 0) {
                if (callBack.equals("showVacancyResponses")) {
                    telegramUserService.findByChatId(chatId).ifPresent(
                            telegramUser -> {
                                telegramUser.setLastDateViewResponse(Instant.now().getEpochSecond());
                                telegramUserService.save(telegramUser);
                            }
                    );
                    sendBotMessageService.setReplyMarkup(getResumeReplyMarkup());
                    message = sendBotMessageService.sendMessage(chatId, newResponses.get(responseIndex).toString());
                    return;
                } else if (callBack.equals("nextVacancyResponse") && responseIndex+1 < newResponses.size()) {
                    responseIndex++;
                } else if (callBack.equals("lastVacancyResponse") && responseIndex-1 >= 0) {
                    responseIndex--;
                }
                EditMessageText editMessageText = new EditMessageText();
                editMessageText.setMessageId(message.getMessageId());
                editMessageText.setReplyMarkup(getResumeReplyMarkup());
                editMessageText.setChatId(String.valueOf(user.getChatId()));
                editMessageText.setText(newResponses.get(responseIndex).toString());
                sendBotMessageService.updateMessage(editMessageText);
            }
        }
    }

    @Override
    public void undo() {

    }

    private InlineKeyboardMarkup getResumeReplyMarkup() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> nextBackButtons = new ArrayList<>();
        nextBackButtons.add(sendBotMessageService.createRow("⬅", "lastVacancyResponse").get(0));
        nextBackButtons.add(sendBotMessageService.createRow("➡", "nextVacancyResponse").get(0));
        rowsInline.add(nextBackButtons);

        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }
}
