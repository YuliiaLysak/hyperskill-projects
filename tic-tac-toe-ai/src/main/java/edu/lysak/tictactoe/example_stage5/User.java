package edu.lysak.tictactoe.example_stage5;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Pattern;

public class User extends Opponent {
    char[][] playingField;
    char letter;
    BufferedReader reader;


    public User(char[][] playingField, char letter, BufferedReader reader) {
        this.playingField = playingField;
        this.letter = letter;
        this.reader = reader;
    }

    @Override
    public void makeMove() {
        try {
            String[] coordinatesBox;
            int x;
            int y;
            while (true) {
                System.out.print("Enter the coordinates: > ");
                String userInput = reader.readLine();
                if (userInput.equals("exit")) {
                    System.exit(0);
                }
                if (!Pattern.matches("\\d.\\d", userInput)) {
                    System.out.println("You should enter numbers!");
                    continue;
                }
                if (!Pattern.matches("[123].[123]", userInput)) {
                    System.out.println("Coordinates should be from 1 to 3!");
                    continue;
                }
                coordinatesBox = userInput.split(" ");
                x = Integer.parseInt(coordinatesBox[0]) - 1;
                y = Integer.parseInt(coordinatesBox[1]) - 1;
                if (playingField[x][y] != '_') {
                    System.out.println("This cell is occupied! Choose another one!");
                    continue;
                }
                break;
            }
            playingField[x][y] = letter;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
