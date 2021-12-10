package edu.lysak.calculator;

public class Main {

    public static void main(String[] args) {
        InputHandler inputHandler = new InputHandler(new Calculator());
        inputHandler.execute();
    }

}
