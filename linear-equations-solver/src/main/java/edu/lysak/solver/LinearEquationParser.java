package edu.lysak.solver;

import java.util.Scanner;

public class LinearEquationParser {

    public LinearEquation parse(Scanner scanner) {
        int numberOfEquations = scanner.nextInt(); // Also, it number of variables
        Matrix coefficients = new Matrix(numberOfEquations);
        Matrix constants = new Matrix(numberOfEquations);
        for (int i = 0; i < numberOfEquations; i++) {
            Row coefficient = new Row(numberOfEquations);
            for (int j = 0; j < numberOfEquations; j++) {
                coefficient.add(scanner.nextDouble());
            }
            coefficients.add(coefficient);

            Row constant = new Row(1);
            constant.add(scanner.nextDouble());
            constants.add(constant);
        }
        return new LinearEquation(coefficients, constants);
    }

}