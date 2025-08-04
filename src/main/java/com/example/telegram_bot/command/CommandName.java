package com.example.telegram_bot.command;

public enum CommandName {

    START("/start"),
    RESUME("/resume"),
    VACANCIES("/vacancies"),
    SUBSCRIPT("/subscript"),
    HELP("/help"),
    NO("");

    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }

}
