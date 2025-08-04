package com.example.telegram_bot.service.impl;

import com.example.telegram_bot.bot.JobConnect;
import com.example.telegram_bot.service.SendBotMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link SendBotMessageService} interface.
 */
@Service
public class SendBotMessageServiceImpl implements SendBotMessageService {

    private final JobConnect jobConnect;

    private SendMessage sendMessage = new SendMessage();

    @Autowired
    public SendBotMessageServiceImpl(JobConnect jobConnect) {
        this.jobConnect = jobConnect;
    }

    @Override
    public Message sendMessage(Long chatId, String message) {
        sendMessage.setChatId(chatId.toString());
        sendMessage.enableHtml(true);
        sendMessage.setText(message);

        try {
            return jobConnect.execute(sendMessage);
        } catch (TelegramApiException e) {
            if (e.getMessage().contains("Too Many Requests")) {

            }
            e.printStackTrace();
        }
        finally {
            sendMessage = new SendMessage();
        }
        return null;
    }


    public void updateMessage(EditMessageText editMessageText) {
        editMessageText.enableHtml(true);
        try {
            jobConnect.execute(editMessageText);
        } catch (TelegramApiException e) {
            System.out.println("Отправлено то же сообщение");
        }
    }

    @Override
    public void deleteMessage(String chatId, int messageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId(messageId);
        try {
            jobConnect.execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setReplyMarkup(InlineKeyboardMarkup markup) {
        sendMessage.setReplyMarkup(markup);
    }

    @Override
    public void setReplyMarkup(ReplyKeyboardMarkup markup) {
        sendMessage.setReplyMarkup(markup);
    }

    @Override
    public List<InlineKeyboardButton> createRow(String text, String data) {
        List<InlineKeyboardButton> rowLocation = new ArrayList<>();
        InlineKeyboardButton inlineButtonAccept = new InlineKeyboardButton();
        inlineButtonAccept.setText(text);
        inlineButtonAccept.setCallbackData(data);
        rowLocation.add(inlineButtonAccept);
        return rowLocation;
    }
}
