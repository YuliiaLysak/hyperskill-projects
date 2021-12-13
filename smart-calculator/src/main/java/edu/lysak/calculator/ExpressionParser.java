package edu.lysak.calculator;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

public class ExpressionParser {
    private final Deque<String> deque = new ArrayDeque<>();
    private final StringBuilder postfixExpression = new StringBuilder();
    private final Map<String, BigInteger> variables;

    public ExpressionParser(Map<String, BigInteger> variables) {
        this.variables = variables;
    }

    public String getConvertedLineFromInfixToPostfix(String line) {
        boolean newBlock = true;
        boolean nextDigitIsNegative = false;
        String[] tokens = line.split(" ");
        for (String token : tokens) {
            if ("(".equals(token)) {
                newBlock = true;
                deque.offerLast(token);
                continue;
            }
            if (")".equals(token)) {
                discardParenthesis();
                continue;
            }
            if ("-".equals(token) && newBlock) {
                newBlock = false;
                nextDigitIsNegative = true;
                continue;
            }

            if (isDigit(token)) {
                newBlock = false;
                if (nextDigitIsNegative) {
                    postfixExpression.append("-");
                    nextDigitIsNegative = false;
                }
                postfixExpression.append(token).append(" ");
                continue;
            }
            if (isVariable(token)) {
                newBlock = false;
                BigInteger val = variables.get(token);
                if (nextDigitIsNegative) {
                    val = val.negate();
                    nextDigitIsNegative = false;
                }
                postfixExpression.append(val).append(" ");
                continue;
            }

            if (deque.isEmpty() || "(".equals(deque.peekLast())) {
                deque.offerLast(token);
            } else {
                String tokenFromDeque = deque.peekLast();
                if (isNotHigherPriority(token, tokenFromDeque)) {
                    addOperatorsFromDeque(token);
                }
                deque.offerLast(token);
            }
        }
        pollAllTokens();
        return postfixExpression.toString().trim();
    }

    private void discardParenthesis() {
        String currentToken = deque.pollLast();
        while (!"(".equals(currentToken)) {
            postfixExpression.append(currentToken).append(" ");
            currentToken = deque.pollLast();
        }
    }

    private void pollAllTokens() {
        while (!deque.isEmpty()) {
            postfixExpression.append(deque.pollLast()).append(" ");
        }
    }

    private boolean isNotHigherPriority(String token, String tokenFromDeque) {
        Operator operator = Operator.findByOperator(token);
        Operator operatorFromDeque = Operator.findByOperator(tokenFromDeque);
        if (operator != null && operatorFromDeque != null) {
            return operator.priority <= operatorFromDeque.priority;
        }
        throw new IllegalStateException("Operators are null");
    }

    private void addOperatorsFromDeque(String token) {
        String tokenFromDeque = deque.pollLast();
        while (tokenFromDeque != null && !"(".equals(tokenFromDeque) && isNotHigherPriority(token, tokenFromDeque)) {
            postfixExpression.append(tokenFromDeque).append(" ");
            tokenFromDeque = deque.pollLast();
        }
        if ("(".equals(tokenFromDeque)) {
            deque.offerLast("(");
        }
    }

    private boolean isDigit(String token) {
        for (char c : token.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    private boolean isVariable(String token) {
        return variables.containsKey(token);
    }
}
