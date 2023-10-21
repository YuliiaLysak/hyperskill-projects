package edu.lysak.tictactoe.example_stage5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Game {
    static boolean isGameEnd = false;

    public static void main(String[] args) {
        char[][] playingField = new char[3][3];
        char letterX = 'X';
        char letterO = 'O';

        System.out.print("Input command: > ");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                for (char[] chars : playingField) {
                    Arrays.fill(chars, '_');
                }
                isGameEnd = false;
                String userInput = reader.readLine();
                if (userInput.equals("exit")) {
                    break;
                }
                if (!userInput.trim().contains(" ")) {
                    System.out.println("Bad parameters!");
                    System.out.print("Input command: > ");
                    continue;
                }

                String[] parameters = userInput.trim().split(" ");
                String[] difficult = {"easy", "medium", "hard", "user"};

                if (parameters.length != 3 || (!(Arrays.asList(difficult).contains(parameters[1]) && Arrays.asList(difficult).contains(parameters[2])))) {
                    System.out.println("Bad parameters!");
                    System.out.print("Input command: > ");
                } else {
                    drawGameField(playingField);

                    Opponent playerOne = null;
                    Opponent playerTwo = null;

                    switch (parameters[1]) {
                        case ("easy") -> playerOne = new Easy(playingField, letterX);
                        case ("medium") -> playerOne = new Medium(playingField, letterX);
                        case ("hard") -> playerOne = new Hard(playingField, letterX);
                        case ("user") -> playerOne = new User(playingField, letterX, reader);
                    }
                    switch (parameters[2]) {
                        case ("easy") -> playerTwo = new Easy(playingField, letterO);
                        case ("medium") -> playerTwo = new Medium(playingField, letterO);
                        case ("hard") -> playerTwo = new Hard(playingField, letterO);
                        case ("user") -> playerTwo = new User(playingField, letterO, reader);
                    }
                    while (true) {
                        playerOne.makeMove();
                        drawGameField(playingField);
                        whoIsWin(playingField);
                        if (!isGameEnd) {
                            playerTwo.makeMove();
                            drawGameField(playingField);
                            whoIsWin(playingField);
                            if (isGameEnd) {
                                System.out.print("Input command: > ");
                                break;
                            }
                        } else {
                            System.out.print("Input command: > ");
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static void whoIsWin(char[][] playingField) {
        for (int i = 0; i < playingField.length; i++) {
            for (int j = 0; j < playingField[i].length; j++) {
                if (playingField[i][j] == '_') {
                    continue;
                }
                if (playingField[i][0] == playingField[i][1] && playingField[i][0] == playingField[i][2]) {
                    System.out.println(playingField[i][0] + " wins");
                    isGameEnd = true;
                    return;
                }
                if (playingField[0][j] == playingField[1][j] && playingField[0][j] == playingField[2][j]) {
                    System.out.println(playingField[0][j] + " wins");
                    isGameEnd = true;
                    return;
                }
            }
        }
        if (((playingField[0][0] == playingField[1][1] && playingField[0][0] == playingField[2][2]) ||
            (playingField[0][2] == playingField[1][1] && playingField[0][2] == playingField[2][0])) && playingField[1][1] != '_') {
            System.out.println(playingField[1][1] + " wins");
            isGameEnd = true;
            return;
        }
        for (char[] chars : playingField) {
            for (char aChar : chars) {
                if (aChar == '_') {
                    return;
                }
            }
        }
        System.out.println("Draw");
        isGameEnd = true;
    }

    protected static void drawGameField(char[][] playingField) {
        System.out.println("---------");
        for (int i = 0; i < playingField.length; i++) {
            System.out.print("| ");
            for (int j = 0; j < playingField[i].length; j++) {
                System.out.print(playingField[i][j] + " ");
            }
            System.out.println("|");
        }
        System.out.println("---------");
    }
}
