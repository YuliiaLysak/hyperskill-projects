package edu.lysak.tictactoe;

import java.util.Arrays;

public class GameField {
    private final char[][] matrix = new char[][]{
        {' ', ' ', ' '},
        {' ', ' ', ' '},
        {' ', ' ', ' '},
    };
    private int xWinCount = 0;
    private int oWinCount = 0;
    private boolean gameFinished = false;
    private int moveCount = 0;


    public boolean isGameFinished() {
        return gameFinished;
    }

    public void setGameFinished(boolean gameFinished) {
        this.gameFinished = gameFinished;
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

    public void putElementIntoCell(int x, int y, char element) {
        matrix[3 - y][x - 1] = element;
        moveCount++;
    }

    public void checkResult() {
        int emptyCount = 0;
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
            gameFinished = true;
            for (char[] matrix : matrix) {
                Arrays.fill(matrix, ' ');
            }
//            drawMatrix();
//            emptyCount = 0;
            xWinCount = 0;
            oWinCount = 0;
            moveCount = 0;
        }
    }

    public boolean isEmptyCellAt(int x, int y) {
        return matrix[3 - y][x - 1] != ' ';
    }

    public Cell getWinCell() {
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[y].length; x++) {
                if (matrix[y][x] == ' ') {
                    if (isWinCell(x, y, 'X') || isWinCell(x, y, 'O')) {
                        x = x + 1;
                        y = 3 - y;
                        return new Cell(x, y);
                    }
                }
            }
        }
        return new Cell(0, 0);
    }

    private boolean isWinCell(int x, int y, char element) {
        return isWinHorizontal(x, y, element)
            || isWinVertical(x, y, element)
            || isWinPrincipalDiagonal(x, y, element)
            || isWinSecondaryDiagonal(x, y, element);
    }

    private boolean isWinHorizontal(int x, int y, char element) {
        int count = 0;
        if (x == 0) {
            if (matrix[y][x + 1] == element) {
                count++;
            }
            if (count == 1 && matrix[y][x + 2] == element) {
                count++;
            }
        } else if (x == 1) {
            if (matrix[y][x - 1] == element) {
                count++;
            }
            if (count == 1 && matrix[y][x + 1] == element) {
                count++;
            }
        } else {
            if (matrix[y][x - 1] == element) {
                count++;
            }
            if (count == 1 && matrix[y][x - 2] == element) {
                count++;
            }
        }
        return count == 2;
    }

    private boolean isWinVertical(int x, int y, char element) {
        int count = 0;
        if (y == 0) {
            if (matrix[y + 1][x] == element) {
                count++;
            }
            if (count == 1 && matrix[y + 2][x] == element) {
                count++;
            }
        } else if (y == 1) {
            if (matrix[y - 1][x] == element) {
                count++;
            }
            if (count == 1 && matrix[y + 1][x] == element) {
                count++;
            }
        } else {
            if (matrix[y - 1][x] == element) {
                count++;
            }
            if (count == 1 && matrix[y - 2][x] == element) {
                count++;
            }
        }
        return count == 2;
    }

    private boolean isWinPrincipalDiagonal(int x, int y, char element) {
        int count = 0;
        if (x == 0 && y == 0) {
            if (matrix[y + 1][x + 1] == element) {
                count++;
            }
            if (count == 1 && matrix[y + 2][x + 2] == element) {
                count++;
            }
        } else if (x == 1 && y == 1) {
            if (matrix[y - 1][x - 1] == element) {
                count++;
            }
            if (count == 1 && matrix[y + 1][x + 1] == element) {
                count++;
            }
        } else if (x == 2 && y == 2) {
            if (matrix[y - 1][x - 1] == element) {
                count++;
            }
            if (count == 1 && matrix[y - 2][x - 2] == element) {
                count++;
            }
        }
        return count == 2;
    }

    private boolean isWinSecondaryDiagonal(int x, int y, char element) {
        int count = 0;
        if (y == 0 && x == 2) {
            if (matrix[y + 1][x - 1] == element) {
                count++;
            }
            if (count == 1 && matrix[y + 2][x - 2] == element) {
                count++;
            }
        } else if (y == 1 && x == 1) {
            if (matrix[y + 1][x - 1] == element) {
                count++;
            }
            if (count == 1 && matrix[y - 1][x + 1] == element) {
                count++;
            }
        } else if (y == 2 && x == 0) {
            if (matrix[y - 1][x + 1] == element) {
                count++;
            }
            if (count == 1 && matrix[y - 2][x + 2] == element) {
                count++;
            }
        }
        return count == 2;
    }
}
