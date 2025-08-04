package command;

import com.example.telegram_bot.Entity.User;
import com.example.telegram_bot.bot.JobConnect;
import com.example.telegram_bot.command.CommandContainer;
import com.example.telegram_bot.command.UnknownCommand;
import com.example.telegram_bot.service.*;
import com.example.telegram_bot.state.State;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CommandContainerTest {
    private CommandContainer commandContainer;
    private User user;

    @BeforeEach
    public void init() {
        SendBotMessageService sendBotMessageService = Mockito.mock(SendBotMessageService.class);
        TelegramUserService telegramUserService = Mockito.mock(TelegramUserService.class);
        SuperJobAuth superJobAuth = Mockito.mock(SuperJobAuth.class);
        SuperJobUserService superJobUserService = Mockito.mock(SuperJobUserService.class);
        ResumeService resumeService = Mockito.mock(ResumeService.class);
        VacancyService vacancyService = Mockito.mock(VacancyService.class);
        JobConnect jobConnect = Mockito.mock(JobConnect.class);
        user = Mockito.mock(User.class);
        commandContainer = new CommandContainer(sendBotMessageService,
                telegramUserService,
                jobConnect,
                superJobAuth,
                superJobUserService,
                resumeService,
                vacancyService);
    }

    @Test
    public void shouldReturnUnknownCommand() {
        //given
        String unknownCommand = "/fgjhdfgdfg";

        //when
        State command = commandContainer.retrieveCommand(unknownCommand, user);

        //then
        Assertions.assertEquals(UnknownCommand.class, command.getClass());
    }
}
