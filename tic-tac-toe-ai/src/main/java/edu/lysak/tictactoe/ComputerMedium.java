package edu.lysak.tictactoe;

import java.util.Random;

public class ComputerMedium implements Player {
    private final GameField gameField;
    private char element;

    public ComputerMedium(GameField gameField) {
        this.gameField = gameField;
    }

    @Override
    public void setElement(char element) {
        this.element = element;
    }

    @Override
    public void move() {
        System.out.println("Making move level \"medium\"");
        Cell winCell = gameField.getWinCell();
        if (winCell.getX() > 0 && winCell.getY() > 0
            && isCorrectRandomNumbers(winCell.getX(), winCell.getY())) {
            gameField.putElementIntoCell(winCell.getX(), winCell.getY(), element);
        } else {
            Random random = new Random();
            int x;
            int y;
            do {
                x = random.nextInt(3) + 1;
                y = random.nextInt(3) + 1;
            } while (!isCorrectRandomNumbers(x, y));

            gameField.putElementIntoCell(x, y, element);
        }
    }

    public boolean isCorrectRandomNumbers(int x, int y) {
        if (x > 3 || x < 1 || y > 3 || y < 1) {
            System.out.println("Coordinates should be from 1 to 3!");
            return false;
        }

        if (gameField.isEmptyCellAt(x, y)) {
            System.out.println("This cell is occupied! Choose another one!");
            return false;
        }
        return true;
    }
}
