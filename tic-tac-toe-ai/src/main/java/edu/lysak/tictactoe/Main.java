package edu.lysak.tictactoe;

public class Main {
    public static void main(String[] args) {
        GameField gameField = new GameField();
        User user = new User('X');
        ComputerAI computer = new ComputerAI('O');

        gameField.drawMatrix();

        while (!gameField.isGameFinished()) {
            if (gameField.getMoveCount() % 2 == 0) {
                user.move();
            } else {
                computer.move();
            }
            gameField.drawMatrix();
            gameField.checkResult();
        }
    }
}
