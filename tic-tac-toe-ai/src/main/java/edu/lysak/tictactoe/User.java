package edu.lysak.tictactoe;

import java.util.Scanner;

public class User implements Player {
    private char element;

    @Override
    public void setElement(char element) {
        this.element = element;
    }


    @Override
    public void move(Scanner scanner) {
        String coordinates;
        do {
            System.out.print("Enter the coordinates: ");
            coordinates = scanner.nextLine();
        } while (GameField.isCorrectInput(coordinates));

        int x = Integer.parseInt(coordinates.substring(0, 1));
        int y = Integer.parseInt(coordinates.substring(2));
        GameField.matrix[3 - y][x - 1] = element;
        GameField.moveCount++;
    }
}
