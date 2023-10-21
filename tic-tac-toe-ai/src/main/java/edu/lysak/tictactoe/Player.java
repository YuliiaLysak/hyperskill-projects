package edu.lysak.tictactoe;

public interface Player {
    void move();
    boolean isCorrectInput(String coordinates);
}
