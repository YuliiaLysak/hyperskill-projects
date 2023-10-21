package edu.lysak.tictactoe;

import java.util.Arrays;
import java.util.Scanner;

public class GameField {
    public static final char[][] matrix = new char[][]{
        {' ', ' ', ' '},
        {' ', ' ', ' '},
        {' ', ' ', ' '},
    };
    private int emptyCount = 0;
    private int xWinCount = 0;
    private int oWinCount = 0;
    private boolean gameFinished = false;
    public static int moveCount = 0;


    public boolean isGameFinished() {
        return gameFinished;
    }

    public int getMoveCount() {
        return moveCount;
    }


    public void drawMatrix() {
        System.out.println("---------");
        for (char[] matrix : matrix) {
            System.out.print("| ");
            for (char c : matrix) {
                System.out.print(c + " ");
            }
            System.out.println("|");
        }
        System.out.println("---------");
    }

    public Player[] getPlayers(Scanner scanner) {
        String command;
        do {
            System.out.print("Input command: ");
            command = scanner.nextLine();
        } while (!GameField.isCorrectCommand(command));
        String[] parts = command.split(" ");
        Player[] players = new Player[2];
        for (int i = 1; i < parts.length; i++) {
            if ("user".equals(parts[i])) {
                players[i - 1] = new User();
            }
            if ("easy".equals(parts[i])) {
                players[i - 1] = new ComputerAI();
            }
        }
        players[0].setElement('X');
        players[1].setElement('O');
        return players;
    }

    private static boolean isCorrectCommand(String command) {
        String[] parts = command.split(" ");
        if (parts.length != 3 || !"start".equals(parts[0])) {
            System.out.println("Bad parameters!");
            return false;
        }
        if (("easy".equals(parts[1]) || "user".equals(parts[1]))
            && ("easy".equals(parts[2]) || "user".equals(parts[2]))) {
            return true;
        } else {
            System.out.println("Bad parameters!");
            return false;
        }
    }

    public static boolean isCorrectInput(String coordinates) {
        try {
            int x = Integer.parseInt(coordinates.substring(0, 1));
            int y = Integer.parseInt(coordinates.substring(2));

            if (x > 3 || x < 1 || y > 3 || y < 1) {
                System.out.println("Coordinates should be from 1 to 3!");
                return true;
            }

            if (GameField.matrix[3 - y][x - 1] != ' ') {
                System.out.println("This cell is occupied! Choose another one!");
                return true;
            }

            return false;

        } catch (NumberFormatException e) {
            System.out.println("You should enter numbers!");
            return true;
        }
    }

    public void checkResult() {
        int rowSum = 0;
        int columnSum = 0;
        int principalDiagonal = 0;
        int secondaryDiagonal = 0;

        for (char[] matrix : matrix) {
            for (char c : matrix) {
                if (c != 'X' && c != 'O') {
                    emptyCount++;
                }
            }
        }

        for (int j = 0; j < matrix.length; j++) {
            for (int i = 0; i < matrix[j].length; i++) {
                rowSum += matrix[j][i];
                columnSum += matrix[i][j];

                if (i == j) {
                    principalDiagonal += matrix[j][i];
                }

                if (i + j == 3 - 1) {
                    secondaryDiagonal += matrix[j][i];
                }
            }

            if (rowSum == 264 || columnSum == 264 || principalDiagonal == 264 || secondaryDiagonal == 264) {        // 'X' + 'X' + 'X'   => 264
                xWinCount++;
            }

            if (rowSum == 237 || columnSum == 237 || principalDiagonal == 237 || secondaryDiagonal == 237) {        // 'O' + 'O' + 'O'  => 237
                oWinCount++;
            }
            rowSum = 0;
            columnSum = 0;
        }

        if (xWinCount > oWinCount) {
            gameFinished = true;
            System.out.println("X wins");
        }

        if (oWinCount > xWinCount) {
            gameFinished = true;
            System.out.println("O wins");
        }

        if (xWinCount == 0 && oWinCount == 0 && emptyCount == 0) {
            System.out.println("Draw");
            for (char[] matrix : matrix) {
                Arrays.fill(matrix, ' ');
            }
            drawMatrix();
            emptyCount = 0;
            xWinCount = 0;
            oWinCount = 0;
            moveCount = 0;
        }
    }
}
