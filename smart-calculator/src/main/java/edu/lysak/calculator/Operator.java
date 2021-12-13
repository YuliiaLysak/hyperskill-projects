package edu.lysak.calculator;

public enum Operator {
    POWER("^", 3),
    MULTIPLICATION("*", 2),
    DIVISION("/", 2),
    ADDITION("+", 1),
    SUBTRACTION("-", 1);

    String operator;
    int priority;

    Operator(String operator, int priority) {
        this.operator = operator;
        this.priority = priority;
    }

    public static Operator findByOperator(String operator) {
        for (Operator value : values()) {
            if (value.operator.equals(operator)) {
                return value;
            }
        }
        return null;
    }
}
