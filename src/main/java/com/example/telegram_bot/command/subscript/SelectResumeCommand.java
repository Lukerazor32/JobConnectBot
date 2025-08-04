package com.example.telegram_bot.command.subscript;

import com.example.telegram_bot.Entity.User;
import com.example.telegram_bot.dto.superjob.resume.Resume;
import com.example.telegram_bot.dto.superjob.resume.ResumeData;
import com.example.telegram_bot.service.ResumeService;
import com.example.telegram_bot.service.SendBotMessageService;
import com.example.telegram_bot.service.SuperJobUserService;
import com.example.telegram_bot.service.TelegramUserService;
import com.example.telegram_bot.state.State;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class SelectResumeCommand implements State {
    private final SendBotMessageService sendBotMessageService;
    private final TelegramUserService telegramUserService;
    private final SuperJobUserService superJobUserService;
    private final ResumeService resumeService;

    private ResumeData resumeData;

    public SelectResumeCommand(SendBotMessageService sendBotMessageService,
                               TelegramUserService telegramUserService,
                               SuperJobUserService superJobUserService,
                               ResumeService resumeService) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
        this.superJobUserService = superJobUserService;
        this.resumeService = resumeService;
    }

    @Override
    public void startState(Update update, User user) {
        resumeData = resumeService.getResumes(user.getToken());
        if (resumeData.getTotal() > 0) {
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
            Resume[] resumes = resumeData.getObjects();
            for (int i = 0; i < resumes.length; i++) {
                Resume resume = resumes[i];
                String resumeTitle = resume.getProfession() + " " + String.format("(%s)", resume.getPublished().getTitle());
                rowsInline.add(sendBotMessageService.createRow(resumeTitle, String.valueOf(i)));
            }
            markupInline.setKeyboard(rowsInline);

            sendBotMessageService.setReplyMarkup(markupInline);
            sendBotMessageService.sendMessage(user.getChatId(), "Выберите резюме, по критериям которого будут находиться вакансии:");
        } else {
            sendBotMessageService.sendMessage(user.getChatId(), "Резюме не найдено");
        }
    }

    @Override
    public void execute(Update update, User user) {
        if (update.hasCallbackQuery()) {
            CallbackQuery callBack = update.getCallbackQuery();
            Resume selectedResume = resumeData.getObjects()[Integer.parseInt(callBack.getData())];
            if (selectedResume != null) {
                sendBotMessageService.sendMessage(user.getChatId(), "Резюме выбрано!");
                user.setState(new SubscriptVacancyCommand(sendBotMessageService,
                        superJobUserService,
                        telegramUserService,
                        selectedResume.getId()));
            } else {
                sendBotMessageService.sendMessage(user.getChatId(), "Резюме не найдено");
            }
        }
    }

    @Override
    public void undo() {

    }
}
