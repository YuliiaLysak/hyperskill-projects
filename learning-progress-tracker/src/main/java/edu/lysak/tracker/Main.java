package edu.lysak.tracker;

import edu.lysak.tracker.statistic.Statistic;
import edu.lysak.tracker.statistic.StatisticService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StudentService studentService = new StudentService();
        Statistic statistic = new Statistic();
        StatisticService statisticService = new StatisticService(studentService, statistic);
        InputHandler inputHandler = new InputHandler(scanner, studentService, statisticService);
        inputHandler.process();
    }
}
