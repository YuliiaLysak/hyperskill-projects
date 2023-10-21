package edu.lysak.tictactoe;

import java.util.Scanner;

public class Main {
    private static final char[][] MATRIX = new char[3][3];

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter cells: ");
        char[] symbols = scanner.nextLine().toCharArray();
        int n = 0;
        int xCount = 0;
        int oCount = 0;
        int emptyCount = 0;
        int xWinCount = 0;
        int oWinCount = 0;


        System.out.println("---------");
        for (int y = 0; y < 3; y++) {
            System.out.print("| ");
            for (int x = 0; x < 3; x++) {
                MATRIX[y][x] = symbols[n];
                System.out.print(MATRIX[y][x] + " ");
                n++;
            }
            System.out.println("|");
        }
        System.out.println("---------");

        System.out.print("Enter the coordinates: ");
        String coordinates = scanner.nextLine();

        //coordinates (x, y)
        while (!isCorrectInput(coordinates)) {
            System.out.print("Enter the coordinates: ");
            coordinates = scanner.nextLine();
        }

        int x = Integer.parseInt(coordinates.substring(0, 1));
        int y = Integer.parseInt(coordinates.substring(2));

        for (char c : symbols) {
            if (c == 'X') {
                xCount++;
            } else if (c == 'O') {
                oCount++;
            } else {
                emptyCount++;
            }
        }

        if (xCount > oCount) {
            MATRIX[3-y][x-1] = 'O';
        } else {
            MATRIX[3-y][x-1] = 'X';
        }
        xCount = 0;
        oCount = 0;
        emptyCount = 0;

        System.out.println("---------");
        for (int j = 0; j < 3; j++) {
            System.out.print("| ");
            for (int i = 0; i < 3; i++) {
                System.out.print(MATRIX[j][i] + " ");
            }
            System.out.println("|");
        }
        System.out.println("---------");


        for (char[] matrix : MATRIX) {
            for (char c : matrix) {
                if (c == 'X') {
                    xCount++;
                } else if (c == 'O') {
                    oCount++;
                } else {
                    emptyCount++;
                }
            }
        }

        int rowSum = 0;
        int columnSum = 0;
        int principalDiagonal = 0;
        int secondaryDiagonal = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                rowSum += MATRIX[i][j];
                columnSum += MATRIX[j][i];

                if (j == i) {
                    principalDiagonal += MATRIX[i][j];
                }

                if (j + i == 3 - 1) {
                    secondaryDiagonal += MATRIX[i][j];
                }
            }

            if (rowSum == 264 || columnSum == 264 || principalDiagonal == 264 || secondaryDiagonal == 264) {
                xWinCount++;
            }

            if (rowSum == 237 || columnSum == 237 || principalDiagonal == 237 || secondaryDiagonal == 237) {
                oWinCount++;
            }
            rowSum = 0;
            columnSum = 0;
        }

        // System.out.println("xWinCount " + xWinCount);
        // System.out.println("oWinCount " + oWinCount);
        // System.out.println("emptyCount " + emptyCount);

//        if (Math.abs(xCount - oCount) > 1 || (xWinCount == oWinCount && xWinCount != 0 && oWinCount != 0)) {
//            System.out.println("Impossible");
//        } else
        if (xWinCount == 0 && oWinCount == 0 && emptyCount > 0) {
            System.out.println("Game not finished");
        } else if (xWinCount == 0 && oWinCount == 0 && emptyCount == 0) {
            System.out.println("Draw");
        } else if (xWinCount > oWinCount) {
            System.out.println("X wins");
        } else if (oWinCount > xWinCount) {
            System.out.println("O wins");
        }
    }

    public static boolean isCorrectInput(String coordinates) {
        try {
            int x = Integer.parseInt(coordinates.substring(0, 1));
            int y = Integer.parseInt(coordinates.substring(2));

            if (x > 3 || x < 1 || y > 3 || y < 1) {
                System.out.println("Coordinates should be from 1 to 3!");
                return false;
            } else if (MATRIX[3 - y][x - 1] != '_') {
                System.out.println("This cell is occupied! Choose another one!");
                return false;
            } else {
                return true;
            }
        } catch (NumberFormatException e) {
            System.out.println("You should enter numbers!");
            return false;
        }
    }
}
