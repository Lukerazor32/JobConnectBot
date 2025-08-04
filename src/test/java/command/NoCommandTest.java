package command;

import com.example.telegram_bot.command.NoCommand;
import com.example.telegram_bot.state.State;
import org.junit.jupiter.api.DisplayName;

import static com.example.telegram_bot.command.CommandName.NO;
import static com.example.telegram_bot.command.NoCommand.NO_MESSAGE;

@DisplayName("Unit-level testing for NoCommand")
public class NoCommandTest extends AbstractCommandTest {

    @Override
    String getCommandName() {
        return NO.getCommandName();
    }

    @Override
    String getCommandMessage() {
        return NO_MESSAGE;
    }

    @Override
    State getCommand() {
        return new NoCommand(sendBotMessageService);
    }
}