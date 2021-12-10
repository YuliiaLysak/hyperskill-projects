package edu.lysak.calculator;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
    private final Map<String, Integer> variables = new HashMap<>();

    public int processExpression(String line) {
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
                variables.put(variableName, Integer.parseInt(variableValue));
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

    private int getPostfixResult(String line) {
        String[] tokens = line.split(" ");
        Deque<Integer> resultDeque = new ArrayDeque<>();
        for (String token : tokens) {
            if (isDigit(token)) {
                resultDeque.offerLast(Integer.parseInt(token));
                continue;
            }
            Integer secondOperand = resultDeque.pollLast();
            Integer firstOperand = resultDeque.pollLast();
            int result = getBinaryResult(firstOperand, secondOperand, token);
            resultDeque.offerLast(result);
        }
        if (!resultDeque.isEmpty()) {
            return resultDeque.pollLast();
        }
        throw new IllegalStateException("Wrong expression '" + line + "'");
    }

    private int getBinaryResult(Integer firstOperand, Integer secondOperand, String operator) {
        switch (operator) {
            case "+":
                return firstOperand + secondOperand;
            case "-":
                return firstOperand - secondOperand;
            case "*":
                return firstOperand * secondOperand;
            case "/":
                return firstOperand / secondOperand;
            case "^":
                return (int) Math.pow(firstOperand, secondOperand);
//                int result = 1;
//                for (int i = 0; i < secondOperand; i++) {
//                    result *= firstOperand;
//                }
//                return result;
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
}
