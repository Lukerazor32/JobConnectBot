package command;

import com.example.telegram_bot.Entity.User;
import com.example.telegram_bot.bot.JobConnect;
import com.example.telegram_bot.service.*;
import com.example.telegram_bot.service.impl.SendBotMessageServiceImpl;
import com.example.telegram_bot.service.impl.SuperJobAuthImpl;
import com.example.telegram_bot.service.impl.SuperJobUserServiceImpl;
import com.example.telegram_bot.state.State;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

abstract class AbstractCommandTest {
    protected JobConnect jobConnect = Mockito.mock(JobConnect.class);
    protected TelegramUserService telegramUserService = Mockito.mock(TelegramUserService.class);
    protected SendBotMessageService sendBotMessageService = new SendBotMessageServiceImpl(jobConnect);
    protected SuperJobAuth superJobAuth = new SuperJobAuthImpl("", "", "", "");
    protected SuperJobUserService superJobUserService = new SuperJobUserServiceImpl("", "");

    abstract String getCommandName();

    abstract String getCommandMessage();

    abstract State getCommand();

    @Test
    public void shouldProperlyExecuteCommand() throws TelegramApiException {
        //given
        User user = new User();
        user.setState(getCommand());
        user.setChatId(1234567890L);

        Update update = new Update();
        Message message = Mockito.mock(Message.class);
        Mockito.when(message.getChatId()).thenReturn(user.getChatId());
        Mockito.when(message.getText()).thenReturn(getCommandName());
        update.setMessage(message);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(user.getChatId().toString());
        sendMessage.setText(getCommandMessage());
        sendMessage.enableHtml(true);

        //when
        getCommand().execute(update, user);

        //then
        Mockito.verify(jobConnect).execute(sendMessage);
    }
}
