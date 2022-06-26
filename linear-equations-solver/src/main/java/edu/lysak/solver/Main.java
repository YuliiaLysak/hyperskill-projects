package edu.lysak.solver;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Double> numbers = Arrays.stream(scanner.nextLine().split(" "))
                .map(Double::parseDouble)
                .toList();
        System.out.printf("%f", numbers.get(1) / numbers.get(0));
    }
}
