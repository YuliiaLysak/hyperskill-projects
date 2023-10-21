package edu.lysak.tictactoe.example_stage5;

import java.util.Random;

public class Easy extends Opponent {
    char[][] playingField;
    char letter;

    public Easy(char[][] playingField, char letter) {
        this.playingField = playingField;
        this.letter = letter;
    }


    @Override
    void makeMove() {
        Random rndX = new Random();
        Random rndY = new Random();
        int x = rndX.nextInt(3);
        int y = rndY.nextInt(3);
        if (playingField[x][y] == '_') {
            System.out.println("Making move level \"easy\"");
            playingField[x][y] = letter;
        } else {
            try {
                Thread.sleep(10);
                makeMove();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
