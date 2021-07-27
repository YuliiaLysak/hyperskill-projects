package edu.lysak.numbers;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NumberChecker {

    public boolean isEven(BigInteger number) {
        return number.mod(BigInteger.TWO).equals(BigInteger.ZERO);
    }

    public boolean isBuzz(BigInteger number) {
        return number.mod(BigInteger.valueOf(7)).equals(BigInteger.ZERO) ||
                number.mod(BigInteger.TEN).equals(BigInteger.valueOf(7));
    }

    public boolean isDuck(BigInteger number) {
        String num = String.valueOf(number);
        return num.substring(1).contains("0");
    }

    public boolean isPalindromic(BigInteger number) {
        String num = String.valueOf(number);
        String reversedNum = new StringBuilder(num).reverse().toString();
        return num.equals(reversedNum);
    }

    public boolean isGapful(BigInteger number) {
        String num = String.valueOf(number);
        String div = "" + num.charAt(0) + num.charAt(num.length() - 1);
        BigInteger divider = new BigInteger(div);
        return num.length() >= 3 && number.mod(divider).equals(BigInteger.ZERO);
    }

    public boolean isSpy(BigInteger number) {
        String num = String.valueOf(number);
        String[] numbers = num.split("");
        int sum = Arrays.stream(numbers)
                .mapToInt(Integer::parseInt)
                .sum();
        int product = Arrays.stream(numbers)
                .mapToInt(Integer::parseInt)
                .reduce(1, (x, y) -> x * y);
        return sum == product;
    }

    public boolean isSquare(BigInteger number) {
        BigInteger remainder = number.sqrtAndRemainder()[1];
        return remainder.equals(BigInteger.ZERO);
    }

    public boolean isSunny(BigInteger number) {
        BigInteger remainder = number.add(BigInteger.ONE).sqrtAndRemainder()[1];
        return remainder.equals(BigInteger.ZERO);
    }

    public boolean isJumping(BigInteger number) {
        String num = String.valueOf(number);
        String[] numbers = num.split("");
        List<Integer> digits = Arrays.stream(numbers)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        for (int i = 0; i < digits.size() - 1; i++) {
            if (Math.abs(digits.get(i) - digits.get(i + 1)) != 1) {
                return false;
            }
        }
        return true;
    }

    public boolean isHappy(BigInteger number) {
        int digitSum;
        while (true) {
            String num = String.valueOf(number);
            String[] digits = num.split("");
            digitSum = Arrays.stream(digits)
                    .mapToInt(Integer::parseInt)
                    .map(i -> i * i)
                    .sum();
            if (String.valueOf(digitSum).length() == 1) {
                break;
            }
            number = BigInteger.valueOf(digitSum);
        }

        return digitSum == 1;
    }
}
