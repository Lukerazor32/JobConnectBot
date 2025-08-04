package com.example.telegram_bot.service;

import com.example.telegram_bot.repository.entity.TelegramUser;
import org.checkerframework.checker.nullness.Opt;

import java.util.List;
import java.util.Optional;

public interface TelegramUserService {
    /**
     * Save provided {@link TelegramUser} entity.
     *
     * @param  telegramUser provided telegram user.
     */
    void save(TelegramUser telegramUser);

//    /**
//     * Retrieve all active {@link TelegramUser}.
//     *
//     * @return the collection of the active {@link TelegramUser} objects.
//     */
//    List<TelegramUser> retrieveAllActiveUsers();

    /**
     * Find {@link TelegramUser} by chatId.
     *
     * @param chatId provided Chat ID
     * @return {@link TelegramUser} with provided chat ID or null otherwise.
     */
    Optional<TelegramUser> findByChatId(Long chatId);

    Optional<TelegramUser> findByAccessToken(String token);

    List<TelegramUser> findAll();
}
