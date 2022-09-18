package edu.lysak.hypermetro.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Command {
    private CommandType commandType;
    private String lineName1;
    private String stationName1;
    private String lineName2;
    private String stationName2;

    public Command(CommandType commandType) {
        this.commandType = commandType;
    }
}
