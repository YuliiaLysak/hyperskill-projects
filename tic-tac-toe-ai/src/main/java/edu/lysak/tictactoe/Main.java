package edu.lysak.tictactoe;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GameField gameField = new GameField();
        Player[] players = gameField.getPlayers(scanner);

        gameField.drawMatrix();

        while (!gameField.isGameFinished()) {
            if (gameField.getMoveCount() % 2 == 0) {
                players[0].move(scanner);
            } else {
                players[1].move(scanner);
            }
            gameField.drawMatrix();
            gameField.checkResult();
        }
    }
}
