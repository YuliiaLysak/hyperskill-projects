package edu.lysak.tictactoe;

import java.util.Arrays;

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
