package edu.lysak.hypermetro;

import edu.lysak.hypermetro.exception.MetroException;
import edu.lysak.hypermetro.service.InputHandler;
import edu.lysak.hypermetro.service.MetroJsonParser;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            MetroJsonParser parser = new MetroJsonParser();
            HyperMetro hyperMetro = new HyperMetro(parser, args[0]);
            Scanner scanner = new Scanner(System.in);
            InputHandler inputHandler = new InputHandler(scanner, hyperMetro);
            inputHandler.proceed();
        } catch (MetroException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
