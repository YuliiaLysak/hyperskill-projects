package edu.lysak.tictactoe;

import java.util.Random;
import java.util.Scanner;

public class ComputerAI implements Player {
    private char element;

    @Override
    public void setElement(char element) {
        this.element = element;
    }


    @Override
    public void move(Scanner scanner) {
        System.out.println("Making move level \"easy\"");
        Random random = new Random();
        int x;
        int y;
        do {
            x = random.nextInt(3) + 1;
            y = random.nextInt(3) + 1;
        } while (GameField.isCorrectInput(x + " " + y));

        GameField.matrix[3 - y][x - 1] = element;
        GameField.moveCount++;
    }
}
