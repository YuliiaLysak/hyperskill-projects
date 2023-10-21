package edu.lysak.tictactoe;

import java.util.Random;

public class ComputerAI implements Player {
    private final char element;

    public ComputerAI(char element) {
        this.element = element;
    }

    @Override
    public void move() {
        System.out.println("Making move level \"easy\"");
        Random random = new Random();
        int x;
        int y;
        do {
            x = random.nextInt(3) + 1;
            y = random.nextInt(3) + 1;
        } while (!isCorrectInput(x + " " + y));

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
