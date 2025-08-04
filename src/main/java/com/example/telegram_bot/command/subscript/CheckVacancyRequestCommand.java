package com.example.telegram_bot.command.subscript;

import com.example.telegram_bot.Entity.User;
import com.example.telegram_bot.command.NoCommand;
import com.example.telegram_bot.dto.superjob.Vacancy;
import com.example.telegram_bot.service.SendBotMessageService;
import com.example.telegram_bot.service.VacancyService;
import com.example.telegram_bot.state.State;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CheckVacancyRequestCommand implements State {
    private final SendBotMessageService sendBotMessageService;
    private final VacancyService vacancyService;

    public CheckVacancyRequestCommand(SendBotMessageService sendBotMessageService, VacancyService vacancyService) {
        this.sendBotMessageService = sendBotMessageService;
        this.vacancyService = vacancyService;
    }

    @Override
    public void startState(Update update, User user) {
        String data = update.getCallbackQuery().getData();
        int idResume= Integer.parseInt(data.split(" ")[1]);
        int idVacancy = Integer.parseInt(data.split(" ")[2]);

        if (vacancyService.sendResponseToVacancy(user.getToken(), idResume, idVacancy)) {
            Vacancy vacancy = vacancyService.getVacancyById(user.getToken(), idVacancy);
            sendBotMessageService.sendMessage(user.getChatId(), String.format("Отклик на вакансию <b>%s</b> успешно отправлен!", vacancy.getProfession()));
        } else {
            sendBotMessageService.sendMessage(user.getChatId(), "При отправке отклика произошла ошибка");
        }
        user.setState(new NoCommand(sendBotMessageService));
    }

    @Override
    public void execute(Update update, User user) {

    }

    @Override
    public void undo() {

    }
}
