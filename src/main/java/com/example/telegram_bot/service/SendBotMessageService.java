package com.example.telegram_bot.service;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public interface SendBotMessageService {

    /**
     * Send message via telegram bot.
     *
     * @param chatId provided chatId in which messages would be sent.
     * @param message provided message to be sent.
     */
    Message sendMessage(Long chatId, String message);

    /**
     * Set reply markup via telegram bot.
     */
    void setReplyMarkup(InlineKeyboardMarkup markup);

    void setReplyMarkup(ReplyKeyboardMarkup markup);

    List<InlineKeyboardButton> createRow(String text, String data);

    void updateMessage(EditMessageText editMessageText);

    void deleteMessage(String chatId, int messageId);
}
