package edu.lysak.tracker;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class InputHandler {
    private final Scanner scanner;
    private final StudentService studentService;

    public InputHandler(Scanner scanner, StudentService studentService) {
        this.scanner = scanner;
        this.studentService = studentService;
    }

    public void process() {
        System.out.println("Learning Progress Tracker");
        boolean isAppRunning = true;
        while (isAppRunning) {
            String commandName = scanner.nextLine().toLowerCase(Locale.ROOT);
            if (commandName.isBlank() || commandName.isEmpty()) {
                System.out.println("No input.");
                continue;
            }

            switch (Command.valueFrom(commandName)) {
                case EXIT -> isAppRunning = false;
                case ADD_STUDENTS -> addStudents();
                case BACK -> System.out.println("Enter 'exit' to exit the program.");
                default -> System.out.println("Error: unknown command!");
            }
        }
        System.out.println("Bye!");
    }

    private void addStudents() {
        System.out.println("Enter student credentials or 'back' to return:");
        while (true) {
            String input = scanner.nextLine();
            Command command = Command.valueFrom(input);
            if (command == Command.BACK) {
                System.out.printf("Total %s students have been added.%n", studentService.getStudentsCount());
                break;
            }

            List<String> credentials = Arrays.stream(input.split(" ")).toList();
            if (credentials.size() < 3) {
                System.out.println("Incorrect credentials.");
                continue;
            }

            if (studentService.addStudent(credentials)) {
                System.out.println("The student has been added.");
            }
        }
    }
}
