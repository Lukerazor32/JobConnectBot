package com.example.telegram_bot.repository.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "tg_user")
public class TelegramUser {
    @Id
    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "username")
    private String username;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "last_date_view_response")
    private Long lastDateViewResponse;

    @Column(name = "resume_subscription_id")
    private Integer resumeSubscriptionId;

    @Column(name = "last_date_vacancy")
    private Long lastDateVacancy;

    @Column(name = "is_subscript")
    private Boolean isSubscript;
}
