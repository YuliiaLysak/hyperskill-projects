package edu.lysak.tictactoe;

import java.util.Scanner;

public class User implements Player {
    private final Scanner scanner = new Scanner(System.in);
    private final char element;

    public User(char element) {
        this.element = element;
    }

    @Override
    public void move() {
        String coordinates;
        do {
            System.out.print("Enter the coordinates: ");
            coordinates = scanner.nextLine();
        } while (!isCorrectInput(coordinates));

        int x = Integer.parseInt(coordinates.substring(0, 1));
        int y = Integer.parseInt(coordinates.substring(2));
        GameField.matrix[3 - y][x - 1] = element;
        GameField.moveCount++;
    }

    @Override
    public boolean isCorrectInput(String coordinates) {
        try {
            int x = Integer.parseInt(coordinates.substring(0, 1));
            int y = Integer.parseInt(coordinates.substring(2));

            if (x > 3 || x < 1 || y > 3 || y < 1) {
                System.out.println("Coordinates should be from 1 to 3!");
                return false;
            }

            if (GameField.matrix[3 - y][x - 1] != ' ') {
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
