package edu.lysak.tracker;

public enum Command {
    EXIT("exit"),
    BACK("back"),
    ADD_STUDENTS("add students"),
    NO_SUCH_COMMAND(null);

    private final String commandName;

    Command(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }

    public static Command valueFrom(String commandName) {
        for (Command command : values()) {
            if (commandName.equals(command.getCommandName())) {
                return command;
            }
        }
        return NO_SUCH_COMMAND;
    }
}
