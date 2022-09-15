package edu.lysak.hypermetro.model;

import lombok.Getter;

@Getter
public enum CommandType {
    APPEND("/append"),
    ADD_HEAD("/add-head"),
    REMOVE("/remove"),
    OUTPUT("/output"),
    EXIT("/exit"),
    INVALID("Invalid command");

    private final String commandName;

    CommandType(String commandName) {
        this.commandName = commandName;
    }

    public static CommandType valueFrom(String commandName) {
        for (CommandType commandType : values()) {
            if (commandName.equalsIgnoreCase(commandType.getCommandName())) {
                return commandType;
            }
        }
        return INVALID;
    }
}
