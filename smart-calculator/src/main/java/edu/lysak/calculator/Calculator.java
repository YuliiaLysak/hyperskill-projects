package edu.lysak.calculator;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
    private final Map<String, BigInteger> variables = new HashMap<>();

    public BigInteger processExpression(String line) {
        ExpressionParser expressionParser = new ExpressionParser(variables);
        String postfixExpression = expressionParser.getConvertedLineFromInfixToPostfix(line.trim());
        return getPostfixResult(postfixExpression);
    }

    public void processVariableExpression(String line) {
        Pattern variablePattern = Pattern.compile("(?<variable>[\\w\\p{IsCyrillic}]+)\\s*=\\s*(?<value>-?.*)");
        Matcher matcher = variablePattern.matcher(line);
        if (matcher.find()) {
            String variableName = matcher.group("variable");
            String variableValue = matcher.group("value");
            variableValue = variableValue.replaceAll("- ", "-");
            if (isInvalidVariableName(variableName)) {
                System.out.println("Invalid identifier");
                return;
            }

            if (isInvalidVariableValue(variableValue)) {
                System.out.println("Invalid assignment");
                return;
            }

            try {
                variables.put(variableName, new BigInteger(variableValue));
            } catch (NumberFormatException e) {
                if (variables.containsKey(variableValue)) {
                    variables.put(variableName, variables.get(variableValue));
                } else {
                    System.out.println("Unknown variable");
                }
            }
        }
    }

    private boolean isInvalidVariableName(String variableName) {
        Pattern identifierPattern = Pattern.compile("[\\d\\p{IsCyrillic}]+");
        Matcher identifierMatcher = identifierPattern.matcher(variableName);
        return identifierMatcher.find();
    }

    private boolean isInvalidVariableValue(String variableValue) {
        Pattern valuePattern = Pattern.compile("[A-Za-z]\\d|\\d[A-Za-z]|=");
        Matcher valueMatcher = valuePattern.matcher(variableValue);
        return valueMatcher.find();
    }

    public void showVariable(String varName) {
        if (variables.containsKey(varName)) {
            System.out.println(variables.get(varName));
        } else {
            System.out.println("Unknown variable");
        }
    }

    private BigInteger getPostfixResult(String line) {
        String[] tokens = line.split(" ");
        Deque<BigInteger> resultDeque = new ArrayDeque<>();
        for (String token : tokens) {
            if (isDigit(token)) {
                resultDeque.offerLast(new BigInteger(token));
                continue;
            }
            BigInteger secondOperand = resultDeque.pollLast();
            BigInteger firstOperand = resultDeque.pollLast();
            BigInteger result = getBinaryResult(firstOperand, secondOperand, token);
            resultDeque.offerLast(result);
        }
        if (!resultDeque.isEmpty()) {
            return resultDeque.pollLast();
        }
        throw new IllegalStateException("Wrong expression '" + line + "'");
    }

    private BigInteger getBinaryResult(BigInteger firstOperand, BigInteger secondOperand, String operator) {
        switch (operator) {
            case "+":
                return firstOperand.add(secondOperand);
            case "-":
                return firstOperand.subtract(secondOperand);
            case "*":
                return firstOperand.multiply(secondOperand);
            case "/":
                return firstOperand.divide(secondOperand);
            case "^":
                return firstOperand.pow(secondOperand.intValue());
//                return pow(firstOperand, secondOperand);
        }
        throw new IllegalStateException("Operation is not possible");
    }

    private boolean isDigit(String token) {
        char[] array = token.toCharArray();
        for (int i = 0; i < array.length; i++) {
            if (i == 0 && array[i] == '-' && array.length > 1) {
                continue;
            }
            if (!Character.isDigit(array[i])) {
                return false;
            }
        }
        return true;
    }

    private BigInteger pow(BigInteger base, BigInteger exponent) {
        BigInteger result = BigInteger.ONE;
        while (exponent.signum() > 0) {
            if (exponent.testBit(0)) result = result.multiply(base);
            base = base.multiply(base);
            exponent = exponent.shiftRight(1);
        }
        return result;
    }
}
