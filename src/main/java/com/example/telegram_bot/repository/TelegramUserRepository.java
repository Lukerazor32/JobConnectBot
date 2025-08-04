package com.example.telegram_bot.repository;

import com.example.telegram_bot.repository.entity.TelegramUser;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {
    Optional<TelegramUser> findTelegramUserByAccessToken(String token);
    Optional<TelegramUser> findByChatId(Long chatId);
    List<TelegramUser> findAll();
}
