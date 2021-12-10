package edu.lysak.calculator;

import java.util.Scanner;

public class InputHandler {
    private final Scanner scanner = new Scanner(System.in);
    private final Calculator calculator;
    private static boolean isExit = false;

    public InputHandler(Calculator calculator) {
        this.calculator = calculator;
    }

    public void execute() {
        do {
            String line = scanner.nextLine().trim();
            processInput(line);
        } while (!isExit);
    }

    private void processInput(String line) {
        if (line.isEmpty()) {
            return;
        }

        if (line.startsWith("/")) {
            String command = line.substring(1);
            switch (command) {
                case "help" -> {
                    System.out.println("'^' - power");
                    System.out.println("'*' - multiplication");
                    System.out.println("'/' - division");
                    System.out.println("'()' - brackets");
                    System.out.println("'+' - addition");
                    System.out.println("'-' - subtraction / unary minus");
                    System.out.println("'a = 5' - variable assignment");
                    return;
                }
                case "exit" -> {
                    System.out.println("Bye!");
                    isExit = true;
                    return;
                }
                default -> {
                    System.out.println("Unknown command");
                    return;
                }
            }
        }

        if (line.matches(".*[*/]{2,}.*")) {
            System.out.println("Invalid expression");
            return;
        }

        if (!isEvenBrackets(line)) {
            System.out.println("Invalid expression");
            return;
        }

//        if (line.startsWith("-")) {
//            line = 0 + " " + line;
//        }
        line = normalizeToken(line);


        if (line.contains("=")) {
            calculator.processVariableExpression(line);
            return;
        }

        if (isNotExpression(line)) {
            calculator.showVariable(line);
            return;
        }
        System.out.println(calculator.processExpression(line));
    }

    private boolean isNotExpression(String line) {
        return !line.contains("=") && !line.contains("+") && !line.contains("-")
                && !line.contains("*") && !line.contains("/") && !line.contains("^");
    }

    private boolean isEvenBrackets(String line) {
        int bracketCount = 0;
        for (char c : line.toCharArray()) {
            if (c == '(' || c == ')') {
                bracketCount++;
            }
        }
        return bracketCount % 2 == 0;
    }

    private String normalizeToken(String line) {
        line = line.replaceAll("^\\s*-", "-1*");
        line = line.replaceAll("\\(\\s*-", "(-1*");
        line = line.replaceAll("(--)+|\\+{2,}", "+");
        line = line.replaceAll("\\+-", "-");
        line = line.replaceAll("\\^", " ^ ");
        line = line.replaceAll("\\*", " * ");
        line = line.replaceAll("/", " / ");
        line = line.replaceAll("\\+", " + ");
        line = line.replaceAll("-", " - ");
        line = line.replaceAll("\\(", "( ").replaceAll("\\)", " )");
        line = line.replaceAll("\\s+", " ");
        return line;
    }
}
