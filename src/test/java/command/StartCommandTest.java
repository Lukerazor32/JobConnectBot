package command;

import com.example.telegram_bot.command.StartCommand;
import com.example.telegram_bot.state.State;
import org.junit.jupiter.api.DisplayName;

import static com.example.telegram_bot.command.CommandName.START;
import static com.example.telegram_bot.command.StartCommand.START_MESSAGE;

@DisplayName("Unit-level testing for StartCommand")
class StartCommandTest extends AbstractCommandTest {

    @Override
    String getCommandName() {
        return START.getCommandName();
    }

    @Override
    String getCommandMessage() {
        return START_MESSAGE;
    }

    @Override
    State getCommand() {
        return new StartCommand(sendBotMessageService, telegramUserService, superJobAuth);
    }
}