package command;

import com.example.telegram_bot.command.HelpCommand;
import com.example.telegram_bot.state.State;
import org.junit.jupiter.api.DisplayName;

import static com.example.telegram_bot.command.CommandName.HELP;

@DisplayName("Unit-level testing for HelpCommand")
public class HelpCommandTest extends AbstractCommandTest {
    @Override
    String getCommandName() {
        return HELP.getCommandName();
    }

    @Override
    String getCommandMessage() {
        return "";
    }

    @Override
    State getCommand() {
        return new HelpCommand(sendBotMessageService, jobConnect);
    }
}
