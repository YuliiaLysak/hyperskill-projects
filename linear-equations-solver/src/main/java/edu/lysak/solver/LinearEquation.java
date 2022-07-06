package edu.lysak.solver;

public class LinearEquation {

    private final Matrix coefficients;
    private final Matrix constants;

    public LinearEquation(Matrix coefficients, Matrix constants) {
        this.coefficients = coefficients;
        this.constants = constants;
    }

    public Matrix getCoefficients() {
        return coefficients;
    }

    public Matrix getConstants() {
        return constants;
    }
}