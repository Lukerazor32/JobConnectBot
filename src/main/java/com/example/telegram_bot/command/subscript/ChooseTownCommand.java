package com.example.telegram_bot.command.subscript;

import com.example.telegram_bot.Entity.User;
import com.example.telegram_bot.dto.superjob.SubscriptionArgs;
import com.example.telegram_bot.dto.superjob.Town;
import com.example.telegram_bot.service.SendBotMessageService;
import com.example.telegram_bot.service.SuperJobUserService;
import com.example.telegram_bot.service.TelegramUserService;
import com.example.telegram_bot.state.State;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class ChooseTownCommand implements State {
    private final SendBotMessageService sendBotMessageService;
    private final SuperJobUserService superJobUserService;
    private final TelegramUserService telegramUserService;
    private SubscriptionArgs.SubscriptionArgsBuilder subscriptionArgs;
    private Message message;

    private List<Town> towns;

    private final static String CHOOSETOWN = "Для начала нужно выбрать город. Напиши название города или выбери из предложенных";
    public ChooseTownCommand(SendBotMessageService sendBotMessageService, SuperJobUserService superJobUserService, TelegramUserService telegramUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.superJobUserService = superJobUserService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public void startState(Update update, User user) {
        towns = superJobUserService.getTowns().getObjects();
        setTownsMarkup();
        message = sendBotMessageService.sendMessage(user.getChatId(), CHOOSETOWN);
    }

    @Override
    public void execute(Update update, User user) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String town = update.getMessage().getText().strip();
            towns = superJobUserService.getTowns(town).getObjects();
            sendBotMessageService.deleteMessage(String.valueOf(user.getChatId()), message.getMessageId());
            setTownsMarkup();
            if (towns != null && towns.size() > 0) {
                message = sendBotMessageService.sendMessage(user.getChatId(), CHOOSETOWN);
            } else {
                message = sendBotMessageService.sendMessage(user.getChatId(), "Город не найден");
            }
        } else if (update.hasCallbackQuery()) {
            try {
                int townId = Integer.parseInt(update.getCallbackQuery().getData());
                int[] towns = {townId};
                subscriptionArgs = SubscriptionArgs.builder().town(towns);
                sendBotMessageService.sendMessage(user.getChatId(), "Город выбран!");
                user.setState(new ChooseCategoryCommand(sendBotMessageService, superJobUserService, telegramUserService, subscriptionArgs));
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }

    @Override
    public void undo() {

    }

    private void setTownsMarkup() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        int maxIndex = 5;
        if (towns.size() < maxIndex) {
            maxIndex = towns.size();
        }
        for (int i = 0; i < maxIndex; i++) {
            Town town = towns.get(i);
            rowsInline.add(sendBotMessageService.createRow(town.getTitle(), String.valueOf(town.getId())));
        }
        markupInline.setKeyboard(rowsInline);
        sendBotMessageService.setReplyMarkup(markupInline);
    }
}
