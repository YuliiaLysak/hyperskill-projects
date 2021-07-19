package edu.lysak.converter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

public class Converter {
    private static final String DIGITS_AND_LETTERS = "0123456789abcdefghijklmnopqrstuvwxyz";

    public BigInteger toDecimal(String source, BigInteger sourceBase) {
        if (sourceBase.equals(BigInteger.TEN)) {
            return new BigInteger(source);
        }
        String[] sourceDigits = source.toLowerCase().split("");
        BigInteger result = BigInteger.ZERO;

        for (int i = sourceDigits.length - 1; i >= 0; i--) {
            BigInteger pow = sourceBase.pow(sourceDigits.length - 1 - i);
            BigInteger product = new BigInteger(String.valueOf(DIGITS_AND_LETTERS.indexOf(sourceDigits[i]))).multiply(pow);
            result = result.add(product);
        }
        return result;
    }

    public String fromDecimal(BigInteger decimal, BigInteger targetBase) {
        if (decimal.equals(BigInteger.ZERO)) {
            return "0";
        }

        String result = "";
        while (decimal.compareTo(BigInteger.ZERO) > 0) {
            BigInteger remainder = decimal.remainder(targetBase);
            result = DIGITS_AND_LETTERS.charAt(remainder.intValue()) + result;
            decimal = decimal.divide(targetBase);
        }
        return result;
    }

    public String convertFractional(String input, BigInteger sourceBase, BigInteger targetBase) {
        String[] parts = input.split("\\.");
        String integerPart = parts[0];
        String fractionalPart = parts[1];

        BigInteger decimalIntegerPart = toDecimal(integerPart, sourceBase);
        String integerPartResult = fromDecimal(decimalIntegerPart, targetBase);

        BigDecimal decimalFractionalPart = fractionalToDecimal(fractionalPart, sourceBase);
        String fractionalPartResult = fractionalFromDecimal(decimalFractionalPart, targetBase);

        return integerPartResult + "." + fractionalPartResult;
    }

    private String fractionalFromDecimal(BigDecimal decimalFractionalPart, BigInteger targetBase) {
        int precision = 5;
        double fractionalPart = decimalFractionalPart.doubleValue();

        StringBuilder s = new StringBuilder();
        double number;
        int integralPart;
        while (precision != 0) {
            integralPart = (int) (fractionalPart * targetBase.intValue());
            s.append(DIGITS_AND_LETTERS.charAt(integralPart));
            number = fractionalPart * targetBase.intValue();
            fractionalPart = number - integralPart;
            precision--;
        }
        return s.toString();
    }

    private BigDecimal fractionalToDecimal(String fractionalPart, BigInteger sourceBase) {
        if (sourceBase.equals(BigInteger.TEN)) {
            return new BigDecimal("0." + fractionalPart);
        }

        String[] sourceDigits = fractionalPart.toLowerCase().split("");
        BigDecimal result = BigDecimal.ZERO;

        for (int i = 0; i < sourceDigits.length; i++) {
            BigDecimal pow = new BigDecimal(sourceBase).pow(-(i + 1), MathContext.DECIMAL64);
            BigDecimal product = new BigDecimal(String.valueOf(DIGITS_AND_LETTERS.indexOf(sourceDigits[i]))).multiply(pow);
            result = result.add(product);
        }
        return result;
    }
}
