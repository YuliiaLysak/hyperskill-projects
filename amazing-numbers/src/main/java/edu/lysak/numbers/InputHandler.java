package edu.lysak.numbers;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class InputHandler {
    private final Scanner scanner;
    private final NumberChecker checker;

    public InputHandler(Scanner scanner, NumberChecker checker) {
        this.scanner = scanner;
        this.checker = checker;
    }

    public void proceed() {
        printGreeting();
        printSupportedRequests();

        String input;
        while (true) {
            System.out.print("Enter a request: ");
            input = scanner.nextLine();

            System.out.println();
            if ("0".equals(input)) {
                break;
            }

            if (input.contains(" ")) {
                proceedAsList(input);
            } else {
                proceedAsNumber(input);
            }
        }

        printGoodbye();
    }

    private void proceedAsNumber(String input) {
        BigInteger number = getStartNumber(input);
        if (number == null) {
            return;
        }

        printResult(number);
    }

    private void proceedAsList(String input) {
        String[] array = input.split(" ");

        BigInteger startNumber = getStartNumber(array[0]);
        if (startNumber == null) {
            return;
        }

        Integer numbersCount = getNumbersCount(array[1]);
        if (numbersCount == null) {
            return;
        }

        if (array.length > 2) {
            proceedAsListWithProperties(array, startNumber, numbersCount);
            return;
        }

        for (int i = 0; i < numbersCount; i++) {
            printResultForListItem(startNumber.add(BigInteger.valueOf(i)));
        }
        System.out.println();
    }

    private void proceedAsListWithProperties(String[] array, BigInteger startNumber, Integer numbersCount) {
        List<String> inputProperties = Arrays.stream(Arrays.copyOfRange(array, 2, array.length))
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        List<String> propertiesValues = Arrays.stream(NumberProperty.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        List<String> excludedProperties = getExcludedProperties(inputProperties, propertiesValues);
        List<String> includedProperties = getIncludedProperties(inputProperties, propertiesValues);
        if (includedProperties == null || excludedProperties == null) {
            return;
        }


        if (excludedProperties.isEmpty()) {
            printResultForListItemWithProperties(includedProperties, startNumber, numbersCount);
            return;
        }

        if (includedProperties.isEmpty()) {
            printResultForListItemWithProperties(excludedProperties, startNumber, numbersCount);
            return;
        }

        if (isDirectOppositePropertiesPresent(excludedProperties, includedProperties)) {
            return;
        }

        printResultForListItemWithProperties(includedProperties, excludedProperties, startNumber, numbersCount);
        System.out.println();
    }

    private boolean isDirectOppositePropertiesPresent(List<String> excludedProperties, List<String> includedProperties) {
        List<String> oppositeProperties = excludedProperties.stream()
                .filter(property -> includedProperties.contains(property.substring(1)))
                .collect(Collectors.toList());
        if (!oppositeProperties.isEmpty()) {
            System.out.printf(
                    "The request contains mutually exclusive properties: [%s, %s]\n",
                    oppositeProperties.get(0),
                    oppositeProperties.get(0).substring(1)
            );
            System.out.println("There are no numbers with these properties.\n");
            return true;
        }
        return false;
    }

    private List<String> getExcludedProperties(List<String> inputProperties, List<String> propertiesValues) {
        List<String> wrongExcludedProperties = inputProperties.stream()
                .filter(property -> property.contains("-"))
                .filter(property -> !propertiesValues.contains(property.substring(1)))
                .collect(Collectors.toList());

        if (isWrongPropertiesPresent(wrongExcludedProperties)) {
            return null;
        }

        List<String> excludedProperties = inputProperties.stream()
                .filter(property -> property.contains("-"))
                .filter(property -> propertiesValues.contains(property.substring(1)))
                .collect(Collectors.toList());

        if (excludedProperties.contains("-ODD") && excludedProperties.contains("-EVEN")) {
            System.out.println("The request contains mutually exclusive properties: [-ODD, -EVEN]");
            System.out.println("There are no numbers with these properties.\n");
            return null;
        }

        if (excludedProperties.contains("-DUCK") && excludedProperties.contains("-SPY")) {
            System.out.println("The request contains mutually exclusive properties: [-DUCK, -SPY]");
            System.out.println("There are no numbers with these properties.\n");
            return null;
        }

        if (excludedProperties.contains("-SAD") && excludedProperties.contains("-HAPPY")) {
            System.out.println("The request contains mutually exclusive properties: [-SAD, -HAPPY]");
            System.out.println("There are no numbers with these properties.\n");
            return null;
        }

        return excludedProperties;
    }

    private List<String> getIncludedProperties(List<String> inputProperties, List<String> propertiesValues) {
        List<String> wrongIncludedProperties = inputProperties.stream()
                .filter(property -> !property.contains("-"))
                .filter(property -> !propertiesValues.contains(property))
                .collect(Collectors.toList());

        if (isWrongPropertiesPresent(wrongIncludedProperties)) {
            return null;
        }

        List<String> includedProperties = inputProperties.stream()
                .filter(propertiesValues::contains)
                .collect(Collectors.toList());

        if (includedProperties.contains("ODD") && includedProperties.contains("EVEN")) {
            System.out.println("The request contains mutually exclusive properties: [ODD, EVEN]");
            System.out.println("There are no numbers with these properties.\n");
            return null;
        }

        if (includedProperties.contains("DUCK") && includedProperties.contains("SPY")) {
            System.out.println("The request contains mutually exclusive properties: [DUCK, SPY]");
            System.out.println("There are no numbers with these properties.\n");
            return null;
        }

        if (includedProperties.contains("SUNNY") && includedProperties.contains("SQUARE")) {
            System.out.println("The request contains mutually exclusive properties: [SUNNY, SQUARE]");
            System.out.println("There are no numbers with these properties.\n");
            return null;
        }

        if (includedProperties.contains("SAD") && includedProperties.contains("HAPPY")) {
            System.out.println("The request contains mutually exclusive properties: [SAD, HAPPY]");
            System.out.println("There are no numbers with these properties.\n");
            return null;
        }

        return includedProperties;
    }

    private boolean isWrongPropertiesPresent(List<String> wrongProperties) {
        if (!wrongProperties.isEmpty()) {
            if (wrongProperties.size() == 1) {
                System.out.printf("The property [%s] is wrong.\n", wrongProperties.get(0));
                printAvailableProperties();
                return true;
            }

            StringBuilder errorMessage = new StringBuilder("The properties [");

            for (String wrongProperty : wrongProperties) {
                errorMessage.append(wrongProperty).append(", ");
            }
            errorMessage.delete(errorMessage.length() - 2, errorMessage.length());
            errorMessage.append("] are wrong.\n");
            System.out.println(errorMessage);
            printAvailableProperties();
            return true;
        }
        return false;
    }

    private BigInteger getStartNumber(String input) {
        BigInteger number;
        try {
            number = new BigInteger(input);
        } catch (NumberFormatException exception) {
            System.out.println("The first parameter should be a natural number or zero.\n");
            return null;
        }

        if (number.compareTo(BigInteger.ZERO) < 0) {
            System.out.println("The first parameter should be a natural number or zero.\n");
            return null;
        }
        return number;
    }

    private Integer getNumbersCount(String input) {
        int numbersCount;
        try {
            numbersCount = Integer.parseInt(input);
        } catch (NumberFormatException exception) {
            System.out.println("The second parameter should be a natural number.\n");
            return null;
        }

        if (numbersCount <= 0) {
            System.out.println("The second parameter should be a natural number.\n");
            return null;
        }
        return numbersCount;
    }

    private void printResult(BigInteger number) {
        System.out.printf("Properties of %,d\n" +
                        "        buzz: %s\n" +
                        "        duck: %s\n" +
                        " palindromic: %s\n" +
                        "      gapful: %s\n" +
                        "         spy: %s\n" +
                        "      square: %s\n" +
                        "       sunny: %s\n" +
                        "     jumping: %s\n" +
                        "         sad: %s\n" +
                        "       happy: %s\n" +
                        "        even: %s\n" +
                        "         odd: %s\n\n",
                number,
                checker.isBuzz(number),
                checker.isDuck(number),
                checker.isPalindromic(number),
                checker.isGapful(number),
                checker.isSpy(number),
                checker.isSquare(number),
                checker.isSunny(number),
                checker.isJumping(number),
                !checker.isHappy(number),
                checker.isHappy(number),
                checker.isEven(number),
                !checker.isEven(number)
        );
    }

    private void printResultForListItem(BigInteger number) {
        StringBuilder result = new StringBuilder(String.format("             %,d is ", number));
        if (checker.isBuzz(number)) {
            result.append("buzz, ");
        }
        if (checker.isDuck(number)) {
            result.append("duck, ");
        }
        if (checker.isPalindromic(number)) {
            result.append("palindromic, ");
        }
        if (checker.isGapful(number)) {
            result.append("gapful, ");
        }
        if (checker.isSpy(number)) {
            result.append("spy, ");
        }
        if (checker.isSquare(number)) {
            result.append("square, ");
        }
        if (checker.isSunny(number)) {
            result.append("sunny, ");
        }
        if (checker.isJumping(number)) {
            result.append("jumping, ");
        }
        if (!checker.isHappy(number)) {
            result.append("sad, ");
        }
        if (checker.isHappy(number)) {
            result.append("happy, ");
        }
        if (checker.isEven(number)) {
            result.append("even, ");
        }
        if (!checker.isEven(number)) {
            result.append("odd, ");
        }
        result.delete(result.length() - 2, result.length());

        System.out.println(result);
    }

    private void printResultForListItemWithProperties(List<String> includedProperties, List<String> excludedProperties, BigInteger startNumber, Integer numbersCount) {
        int count = 0;
        while (count < numbersCount) {
            int trueConditionCount = 0;
            for (String includedProperty : includedProperties) {
                for (String excludedProperty : excludedProperties) {
                    boolean includedCondition = getConditionForProperty(includedProperty, startNumber);
                    boolean excludedCondition = getConditionForProperty(excludedProperty, startNumber);
                    if (includedCondition && excludedCondition) {
                        trueConditionCount++;
                    }
                }
            }

            if (trueConditionCount == includedProperties.size() * excludedProperties.size()) {
                printResultForListItem(startNumber);
                count++;
            }
            startNumber = startNumber.add(BigInteger.ONE);
        }
    }

    private void printResultForListItemWithProperties(List<String> includedProperties, BigInteger startNumber, Integer numbersCount) {
        int count = 0;
        while (count < numbersCount) {
            int trueConditionCount = 0;
            for (String property : includedProperties) {
                boolean condition = getConditionForProperty(property, startNumber);
                if (condition) {
                    trueConditionCount++;
                }
            }
            if (trueConditionCount == includedProperties.size()) {
                printResultForListItem(startNumber);
                count++;
            }
            startNumber = startNumber.add(BigInteger.ONE);
        }
    }

    private boolean getConditionForProperty(String propertyName, BigInteger number) {
        switch (propertyName) {
            case "BUZZ":
                return checker.isBuzz(number);
            case "DUCK":
                return checker.isDuck(number);
            case "PALINDROMIC":
                return checker.isPalindromic(number);
            case "GAPFUL":
                return checker.isGapful(number);
            case "SPY":
                return checker.isSpy(number);
            case "SQUARE":
                return checker.isSquare(number);
            case "SUNNY":
                return checker.isSunny(number);
            case "JUMPING":
                return checker.isJumping(number);
            case "SAD":
                return !checker.isHappy(number);
            case "HAPPY":
                return checker.isHappy(number);
            case "EVEN":
                return checker.isEven(number);
            case "ODD":
                return !checker.isEven(number);
            case "-BUZZ":
                return !checker.isBuzz(number);
            case "-DUCK":
                return !checker.isDuck(number);
            case "-PALINDROMIC":
                return !checker.isPalindromic(number);
            case "-GAPFUL":
                return !checker.isGapful(number);
            case "-SPY":
                return !checker.isSpy(number);
            case "-SQUARE":
                return !checker.isSquare(number);
            case "-SUNNY":
                return !checker.isSunny(number);
            case "-JUMPING":
                return !checker.isJumping(number);
            case "-SAD":
                return checker.isHappy(number);
            case "-HAPPY":
                return !checker.isHappy(number);
            case "-EVEN":
                return !checker.isEven(number);
            case "-ODD":
                return checker.isEven(number);
        }
        throw new IllegalArgumentException();
    }

    private void printSupportedRequests() {
        System.out.println("Supported requests:\n" +
                "- enter a natural number to know its properties;\n" +
                "- enter two natural numbers to obtain the properties of the list:\n" +
                "  * the first parameter represents a starting number;\n" +
                "  * the second parameter shows how many consecutive numbers are to be printed;\n" +
                "- two natural numbers and properties to search for;\n" +
                "- a property preceded by minus must not be present in numbers;\n" +
                "- separate the parameters with one space;\n" +
                "- enter 0 to exit.\n");
    }

    private void printAvailableProperties() {
        System.out.printf("Available properties: %s\n\n", Arrays.toString(NumberProperty.values()));
    }

    private void printGreeting() {
        System.out.println("Welcome to Amazing Numbers!\n");
    }

    private void printGoodbye() {
        System.out.println("Goodbye!");
    }
}
