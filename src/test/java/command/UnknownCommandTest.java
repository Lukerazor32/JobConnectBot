package command;

import com.example.telegram_bot.command.UnknownCommand;
import com.example.telegram_bot.state.State;
import org.junit.jupiter.api.DisplayName;

import static com.example.telegram_bot.command.UnknownCommand.UNKNOWN_MESSAGE;

@DisplayName("Unit-level testing for UnknownCommand")
public class UnknownCommandTest extends AbstractCommandTest {

    @Override
    String getCommandName() {
        return "/fdgdfgdfgdbd";
    }

    @Override
    String getCommandMessage() {
        return UNKNOWN_MESSAGE;
    }

    @Override
    State getCommand() {
        return new UnknownCommand(sendBotMessageService, jobConnect);
    }
}
