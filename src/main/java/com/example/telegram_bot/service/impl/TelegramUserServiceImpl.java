package com.example.telegram_bot.service.impl;

import com.example.telegram_bot.repository.TelegramUserRepository;
import com.example.telegram_bot.repository.entity.TelegramUser;
import com.example.telegram_bot.service.TelegramUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TelegramUserServiceImpl implements TelegramUserService {

    private final TelegramUserRepository telegramUserRepository;

    @Autowired
    public TelegramUserServiceImpl(TelegramUserRepository telegramUserRepository) {
        this.telegramUserRepository = telegramUserRepository;
    }

    @Override
    public void save(TelegramUser telegramUser) {
        telegramUserRepository.save(telegramUser);
    }

    @Override
    public Optional<TelegramUser> findByChatId(Long chatId) {
        return telegramUserRepository.findByChatId(chatId);
    }

    @Override
    public Optional<TelegramUser> findByAccessToken(String token) {
        return telegramUserRepository.findTelegramUserByAccessToken(token);
    }

    @Override
    public List<TelegramUser> findAll() {
        return telegramUserRepository.findAll();
    }
}
