package edu.lysak.tictactoe;

public class Main {
    public static void main(String[] args) {
        GameField gameField = new GameField();
        InputHandler inputHandler = new InputHandler(gameField);

        String command = inputHandler.getCommand();

        while (!inputHandler.isExitGame()) {
            Player[] players = inputHandler.getPlayers(gameField, command);
            gameField.drawMatrix();

            while (!gameField.isGameFinished()) {
                if (gameField.getMoveCount() % 2 == 0) {
                    players[0].move();
                } else {
                    players[1].move();
                }
                gameField.drawMatrix();
                gameField.checkResult();
            }
            command = inputHandler.getCommand();
            gameField.setGameFinished(false);
        }
    }
}
