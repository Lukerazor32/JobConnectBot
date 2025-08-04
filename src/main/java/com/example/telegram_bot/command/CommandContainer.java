package com.example.telegram_bot.command;

import com.example.telegram_bot.Entity.User;
import com.example.telegram_bot.bot.JobConnect;
import com.example.telegram_bot.command.subscript.ChooseTownCommand;
import com.example.telegram_bot.command.subscript.SelectResumeCommand;
import com.example.telegram_bot.service.*;
import com.example.telegram_bot.state.State;
import com.google.common.collect.ImmutableMap;

import static com.example.telegram_bot.command.CommandName.*;

public class CommandContainer {

    private final ImmutableMap<String, State> commandMap;
    private final ImmutableMap<String, State> commandAdminMap;
    private final State unknownCommand;
    private final Long adminID = Long.valueOf(1395425257);

    public CommandContainer(SendBotMessageService sendBotMessageService,
                            TelegramUserService telegramUserService,
                            JobConnect jobConnect,
                            SuperJobAuth superJobAuth,
                            SuperJobUserService spUserService,
                            ResumeService resumeService,
                            VacancyService vacancyService) {
        commandMap = ImmutableMap.<String, State>builder()
                .put(START.getCommandName(), new StartCommand(sendBotMessageService, telegramUserService, superJobAuth))
                .put(HELP.getCommandName(), new HelpCommand(sendBotMessageService, jobConnect))
                .put(NO.getCommandName(), new NoCommand(sendBotMessageService))
                .put(RESUME.getCommandName(), new EditResumeCommand(sendBotMessageService, telegramUserService, resumeService))
                .put(VACANCIES.getCommandName(), new VacancyCommand(sendBotMessageService, telegramUserService, resumeService, vacancyService))
                .put(SUBSCRIPT.getCommandName(), new SelectResumeCommand(sendBotMessageService, telegramUserService, spUserService, resumeService))
                .build();

        unknownCommand = new UnknownCommand(sendBotMessageService, jobConnect);

        commandAdminMap = ImmutableMap.<String, State>builder()
                .build();
    }

    public State retrieveCommand(String commandIdentifier, User user) {
        if (commandAdminMap.containsKey(commandIdentifier) && (user.getChatId().equals(adminID))) return commandAdminMap.get(commandIdentifier);
        else return commandMap.getOrDefault(commandIdentifier, unknownCommand);
    }
}
