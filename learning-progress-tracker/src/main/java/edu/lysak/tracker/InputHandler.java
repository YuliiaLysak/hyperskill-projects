package edu.lysak.tracker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class InputHandler {
    private static final String SPACE = " ";
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
                case LIST -> listStudents();
                case FIND -> findStudent();
                case ADD_STUDENTS -> addStudents();
                case ADD_POINTS -> addPoints();
                case EXIT -> isAppRunning = false;
                case BACK -> System.out.println("Enter 'exit' to exit the program.");
                default -> System.out.println("Error: unknown command!");
            }
        }
        System.out.println("Bye!");
    }

    private void listStudents() {
        Map<Integer, Student> students = studentService.getStudents();
        if (students.isEmpty()) {
            System.out.println("No students found.");
        } else {
            System.out.println("Students:");
            students.keySet()
                    .forEach(System.out::println);
        }
    }

    private void findStudent() {
        System.out.println("Enter an id or 'back' to return:");
        while (true) {
            String input = scanner.nextLine();
            Command command = Command.valueFrom(input);
            if (command == Command.BACK) {
                break;
            }
            int studentId = Integer.parseInt(input);
            Student student = studentService.getStudent(studentId);
            if (student == null) {
                System.out.printf("No student is found for id=%s.%n", studentId);
                continue;
            }
            Map<Course, Integer> coursesPoints = student.getCoursesPoints();
            System.out.printf("%s points: Java=%s; DSA=%s; Databases=%s; Spring=%s%n",
                    studentId,
                    coursesPoints.get(Course.JAVA),
                    coursesPoints.get(Course.DSA),
                    coursesPoints.get(Course.DATABASES),
                    coursesPoints.get(Course.SPRING)
            );
        }
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

            List<String> credentials = Arrays.stream(input.split(SPACE)).toList();
            if (credentials.size() < 3) {
                System.out.println("Incorrect credentials.");
                continue;
            }

            if (studentService.addStudent(credentials)) {
                System.out.println("The student has been added.");
            }
        }
    }

    private void addPoints() {
        System.out.println("Enter an id and points or 'back' to return:");
        while (true) {
            String input = scanner.nextLine();
            Command command = Command.valueFrom(input);
            if (command == Command.BACK) {
                break;
            }

            List<String> elements = Arrays.stream(input.split(SPACE)).toList();
            List<Integer> points = parsePoints(elements.subList(1, elements.size()));
            if (points.size() != 4) {
                System.out.println("Incorrect points format.");
                continue;
            }

            String studentId = elements.get(0);
            if (!studentService.isStudentExist(studentId)) {
                System.out.printf("No student is found for id=%s.%n", studentId);
                continue;
            }

            studentService.updatePoints(studentId, points);
            System.out.println("Points updated.");
        }
    }

    private List<Integer> parsePoints(List<String> points) {
        List<Integer> pointsAsInt = new ArrayList<>();
        for (String point : points) {
            try {
                pointsAsInt.add(Integer.parseInt(point));
            } catch (NumberFormatException exception) {
                return List.of();
            }
        }
        boolean isAnyNegative = pointsAsInt.stream()
                .anyMatch(it -> it < 0);

        if (isAnyNegative || pointsAsInt.size() < points.size()) {
            return List.of();
        }

        return pointsAsInt;
    }

}
