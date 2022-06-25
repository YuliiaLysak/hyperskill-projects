package edu.lysak.tracker;

import edu.lysak.tracker.statistic.CourseStatistic;
import edu.lysak.tracker.statistic.StatisticService;
import edu.lysak.tracker.statistic.StudentStatistic;

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
    private final StatisticService statisticService;

    public InputHandler(Scanner scanner, StudentService studentService, StatisticService statisticService) {
        this.scanner = scanner;
        this.studentService = studentService;
        this.statisticService = statisticService;
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
                case STATISTICS -> showStatistics();
                case NOTIFY -> notifyStudents();
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
            Map<Course, CourseStatistic> coursesStatistics = student.getCoursesStatistics();
            System.out.printf("%s points: Java=%s; DSA=%s; Databases=%s; Spring=%s%n",
                    studentId,
                    coursesStatistics.get(Course.JAVA).getCoursePoints(),
                    coursesStatistics.get(Course.DSA).getCoursePoints(),
                    coursesStatistics.get(Course.DATABASES).getCoursePoints(),
                    coursesStatistics.get(Course.SPRING).getCoursePoints()
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

            studentService.updatePointsAndTasks(studentId, points);
            System.out.println("Points updated.");
        }
    }

    private void showStatistics() {
        statisticService.initStatistics();
        System.out.println("Type the name of a course to see details or 'back' to quit:");
        System.out.printf("Most popular: %s%n", statisticService.getMostPopular());
        System.out.printf("Least popular: %s%n", statisticService.getLeastPopular());
        System.out.printf("Highest activity: %s%n", statisticService.getHighestActivity());
        System.out.printf("Lowest activity: %s%n", statisticService.getLowestActivity());
        System.out.printf("Easiest course: %s%n", statisticService.getEasiestCourse());
        System.out.printf("Hardest course: %s%n", statisticService.getHardestCourse());

        while (true) {
            String input = scanner.nextLine();
            if (input.equals(Command.BACK.getCommandName())) {
                break;
            }
            switch (Course.valueFrom(input)) {
                case JAVA -> showCourseStatistic(Course.JAVA);
                case DSA -> showCourseStatistic(Course.DSA);
                case DATABASES -> showCourseStatistic(Course.DATABASES);
                case SPRING -> showCourseStatistic(Course.SPRING);
                default -> System.out.println("Unknown course.");
            }
        }
    }

    private void notifyStudents() {
        Map<Student, List<Course>> studentsWithCompletedCourses = studentService.getStudentsWithCompletedCourses();
        studentsWithCompletedCourses.forEach(this::notifyStudent);
        System.out.printf("Total %s students have been notified.%n", studentsWithCompletedCourses.size());
    }

    private void notifyStudent(Student student, List<Course> courses) {
        courses.forEach(course -> {
            System.out.printf("To: %s%n", student.getEmail());
            System.out.println("Re: Your Learning Progress");
            System.out.printf(
                    "Hello, %s %s! You have accomplished our %s course!%n",
                    student.getFirstName(),
                    student.getLastName(),
                    course.getName()
            );
        });
        student.setNotified(true);
    }

    private void showCourseStatistic(Course course) {
        System.out.println(course.getName());
        System.out.println("id\tpoints\tcompleted");
        List<StudentStatistic> studentsByCourse = statisticService.getStudentsByCourse(course);
        if (!studentsByCourse.isEmpty()) {
            studentsByCourse.forEach(it -> System.out.printf("%s\t%s\t%s%%%n",
                    it.getStudentId(),
                    it.getEarnedPoints(),
                    it.getCompleted()
            ));
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
