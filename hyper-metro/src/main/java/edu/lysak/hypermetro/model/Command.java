package edu.lysak.hypermetro.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Command {
    private CommandType commandType;
    private String lineName;
    private String stationName;

    public Command(CommandType commandType) {
        this.commandType = commandType;
    }
}
