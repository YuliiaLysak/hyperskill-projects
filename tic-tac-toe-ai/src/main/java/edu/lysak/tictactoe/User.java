package edu.lysak.tictactoe;

public class User implements Player {
    private InputHandler inputHandler;
    private final GameField gameField;
    private char element;

    public User(GameField gameField, InputHandler inputHandler) {
        this.gameField = gameField;
        this.inputHandler = inputHandler;
    }

    @Override
    public void setElement(char element) {
        this.element = element;
    }

    @Override
    public void move() {
        Cell cell = inputHandler.getCell();
        gameField.putElementIntoCell(cell.getX(), cell.getY(), element);
    }
}
