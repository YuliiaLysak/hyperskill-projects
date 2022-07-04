package edu.lysak.solver;

public class GaussElimination {

    public Matrix solve(LinearEquation equation) {
        System.out.println("Start solving the equation.");
        Matrix coefficients = equation.getCoefficients();
        Matrix constants = equation.getConstants();

        // Reduce Equation to Echelon Form
        for (int i = 0; i < coefficients.size(); i++) {
            Row leadingRow = coefficients.getAt(i);
            double leadingElement = 1 / leadingRow.getAt(i);
            if (leadingElement != 1) {
                System.out.printf("%s * R%d -> R%d%n", leadingElement, i + 1, i + 1);
                for (int j = 0; j < leadingRow.size(); j++) {
                    leadingRow.update(j, leadingElement * leadingRow.getAt(j));
                }
                constants.getAt(i).update(0, leadingElement * constants.getAt(i).getAt(0));
            }

            for (int j = i + 1; j < coefficients.size(); j++) {
                Row row = coefficients.getAt(j);
                double leadingElementFromRow = row.getAt(i);
                System.out.printf("%s * R%d + R%d-> R%d%n", -1 * leadingElementFromRow, i + 1, j + 1, j + 1);
                for (int k = 0; k < row.size(); k++) {
                    row.update(k, row.getAt(k) - leadingElementFromRow * leadingRow.getAt(k));
                }
                constants.getAt(j).update(0, constants.getAt(j).getAt(0) - leadingElementFromRow * constants.getAt(i).getAt(0));
            }
        }

        // Reduce Equation to Reduced Echelon Form
        for (int i = coefficients.size() - 1; i > 0; i--) {
            Row leadingRow = coefficients.getAt(i);
            for (int j = i - 1; j >= 0; j--) {
                Row row = coefficients.getAt(j);
                double leadingElement = row.getAt(i);
                System.out.printf("%s * R%d + R%d-> R%d%n", -1 * leadingElement, i + 1, j + 1, j + 1);
                for (int k = 0; k < row.size(); k++) {
                    row.update(k, -1 * leadingElement * leadingRow.getAt(k) + row.getAt(k));
                }
                constants.getAt(j).update(0, -1 * leadingElement * constants.getAt(i).getAt(0) + constants.getAt(j).getAt(0));
            }
        }

        return constants;
    }

}
