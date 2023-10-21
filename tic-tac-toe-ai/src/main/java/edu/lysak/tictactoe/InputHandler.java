package edu.lysak.tictactoe;

import java.util.Scanner;

public class InputHandler {
    private final Scanner scanner = new Scanner(System.in);
    private final GameField gameField;
    private boolean exitGame = false;


    public InputHandler(GameField gameField) {
        this.gameField = gameField;
    }

    public boolean isExitGame() {
        return exitGame;
    }

    public String getCommand() {
        String command;
        do {
            System.out.print("Input command: ");
            command = scanner.nextLine();
        } while (!correctCommand(command));

        if (command.equals("exit")) {
            exitGame = true;
        }
        return command;
    }

    private boolean correctCommand(String command) {
        if ("exit".equals(command)) {
            return true;
        }

        String[] parts = command.split(" ");
        if (parts.length != 3 || !"start".equals(parts[0])) {
            System.out.println("Bad parameters!");
            return false;
        }

        if (("easy".equals(parts[1]) || "user".equals(parts[1]) || "medium".equals(parts[1]))
            && ("easy".equals(parts[2]) || "user".equals(parts[2]) || "medium".equals(parts[2]))) {
            return true;
        } else {
            System.out.println("Bad parameters!");
            return false;
        }
    }

    public Player[] getPlayers(GameField gameField, String command) {
        String[] parts = command.split(" ");
        Player[] players = new Player[2];
        for (int i = 1; i < parts.length; i++) {
            if ("user".equals(parts[i])) {
                players[i - 1] = new User(gameField, this);
            }
            if ("easy".equals(parts[i])) {
                players[i - 1] = new ComputerEasy(gameField);
            }
            if ("medium".equals(parts[i])) {
                players[i - 1] = new ComputerMedium(gameField);
            }
        }
        players[0].setElement('X');
        players[1].setElement('O');
        return players;
    }

    public Cell getCell() {
        String coordinates;
        do {
            System.out.print("Enter the coordinates: ");
            coordinates = scanner.nextLine();
        } while (!correctInput(coordinates));
        int x = Integer.parseInt(coordinates.substring(0, 1));
        int y = Integer.parseInt(coordinates.substring(2));

        return new Cell(x, y);
    }

    public boolean correctInput(String coordinates) {
        try {
            int x = Integer.parseInt(coordinates.substring(0, 1));
            int y = Integer.parseInt(coordinates.substring(2));

            if (x > 3 || x < 1 || y > 3 || y < 1) {
                System.out.println("Coordinates should be from 1 to 3!");
                return false;
            }

            if (gameField.isEmptyCellAt(x, y)) {
                System.out.println("This cell is occupied! Choose another one!");
                return false;
            }

            return true;

        } catch (NumberFormatException e) {
            System.out.println("You should enter numbers!");
            return false;
        }
    }
}
