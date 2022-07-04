package edu.lysak.solver;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.StringJoiner;

public class Main {
    public static void main(String[] args) throws IOException {
        String inputFilePath = Utils.getInputFile(args);
        String outputFilePath = Utils.getOutputFile(args);

        Scanner scanner = new Scanner(Path.of(inputFilePath));
        LinearEquationParser linearEquationParser = new LinearEquationParser();
        LinearEquation linearEquation = linearEquationParser.parse(scanner);
        GaussElimination gaussElimination = new GaussElimination();
        Matrix solution = gaussElimination.solve(linearEquation);

        try (BufferedWriter outputStream = Files.newBufferedWriter(Path.of(outputFilePath))) {
            StringJoiner stringJoiner = new StringJoiner(", ");
            for (int i = 0; i < solution.size(); i++) {
                double constant = solution.getAt(i).getAt(0);
                stringJoiner.add(String.valueOf(constant));
                outputStream.write(String.valueOf(constant));
                outputStream.write(System.lineSeparator());
            }

            System.out.printf("The solution is: (%s)%n", stringJoiner);
            System.out.printf("Saved to file %s%n", outputFilePath);
        }
    }
}
