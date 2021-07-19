package edu.lysak.converter;

import java.math.BigInteger;
import java.util.Scanner;

public class InputHandler {
    private final Scanner scanner;
    private final Converter converter;

    public InputHandler(Scanner scanner, Converter converter) {
        this.scanner = scanner;
        this.converter = converter;
    }

    public void proceed() {
        String input;
        do {
            System.out.print("Enter two numbers in format: {source base} {target base} (To quit type /exit) ");
            input = scanner.nextLine();
            if ("/exit".equals(input)) {
                continue;
            }
            String[] numbers = input.split(" ");
            BigInteger sourceBase = new BigInteger(numbers[0]);
            BigInteger targetBase = new BigInteger(numbers[1]);
            proceedWithBases(sourceBase, targetBase);
        } while (!"/exit".equals(input));
    }

    private void proceedWithBases(BigInteger sourceBase, BigInteger targetBase) {
        String input;
        do {
            System.out.printf("Enter number in base %s to convert to base %s (To go back type /back) ", sourceBase, targetBase);
            input = scanner.nextLine();
            if (!"/back".equals(input)) {
                String result;
                if (input.contains(".")) {
                    result = converter.convertFractional(input, sourceBase, targetBase);
                } else {
                    BigInteger decimal = converter.toDecimal(input, sourceBase);
                    result = converter.fromDecimal(decimal, targetBase);
                }
                System.out.println("Conversion result: " + result);
            }
            System.out.println();
        } while (!"/back".equals(input));
    }
}
