package edu.lysak.hypermetro.service;

import edu.lysak.hypermetro.HyperMetro;
import edu.lysak.hypermetro.model.Command;
import edu.lysak.hypermetro.model.CommandType;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputHandler {
    private final Scanner scanner;
    private final HyperMetro hyperMetro;

    public InputHandler(Scanner scanner, HyperMetro hyperMetro) {
        this.scanner = scanner;
        this.hyperMetro = hyperMetro;
    }

    public void proceed() {
        while (true) {
            String input = scanner.nextLine();
            Command command = getCommand(input);
            switch (command.getCommandType()) {
                case APPEND -> hyperMetro.append(command.getLineName(), command.getStationName());
                case ADD_HEAD -> hyperMetro.addHead(command.getLineName(), command.getStationName());
                case REMOVE -> hyperMetro.remove(command.getLineName(), command.getStationName());
                case OUTPUT -> hyperMetro.output(command.getLineName());
                case EXIT -> {
                    return;
                }
                case INVALID -> System.out.println(CommandType.INVALID.getCommandName());
            }

        }
    }

    private Command getCommand(String input) {
//        String regex = "[^\s\"]+|\"([^\"]*)\"";
        String regex = "[^\s\"]+|\"[^\"]*\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher regexMatcher = pattern.matcher(input);
        List<String> inputList = new ArrayList<>();
        while (regexMatcher.find()) {
            inputList.add(regexMatcher.group().replace("\"", ""));
        }

        Command command = new Command(CommandType.valueFrom(inputList.get(0)));

        if (inputList.size() > 1) {
            command.setLineName(inputList.get(1));
        }

        if (inputList.size() > 2) {
            command.setStationName(inputList.get(2));
        }

        return command;
    }
}
