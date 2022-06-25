package edu.lysak.tracker;

import edu.lysak.tracker.statistic.CourseStatistic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentService {
    private static final String NAME_REGEX = "^[a-zA-Z]+((['-]?[a-zA-Z]+['-]?)?|['-]?)[a-zA-Z]+$";
    private static final String EMAIL_REGEX = "^[a-zA-z.\\d]+@[a-zA-z\\d]+\\.[a-zA-z\\d]+$";
    private final Map<Integer, Student> students = new HashMap<>();
    private int studentCount = 1;

    public int getStudentsCount() {
        return students.size();
    }

    public Map<Integer, Student> getStudents() {
        return new HashMap<>(students);
    }

    public Student getStudent(int studentId) {
        return students.get(studentId);
    }

    public boolean isStudentExist(String studentId) {
        try {
            return students.containsKey(Integer.parseInt(studentId));
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    public boolean addStudent(List<String> credentials) {
        String firstName = credentials.get(0);
        if (isInvalidFirstName(firstName)) {
            System.out.println("Incorrect first name.");
            return false;
        }

        List<String> lastNameParts = credentials.subList(1, credentials.size() - 1);
        if (isInvalidLastName(lastNameParts)) {
            System.out.println("Incorrect last name.");
            return false;
        }

        String email = credentials.get(credentials.size() - 1);
        if (isEmailTaken(email)) {
            System.out.println("This email is already taken.");
            return false;
        }
        if (isInvalidEmail(email)) {
            System.out.println("Incorrect email.");
            return false;
        }

        Student student = new Student(
                firstName,
                String.join(" ", lastNameParts),
                email
        );
        students.put(studentCount++, student);
        return true;
    }

    public Map<Student, List<Course>> getStudentsWithCompletedCourses() {
        Map<Student, List<Course>> studentsWithCompletedCourses = new HashMap<>();
        students.entrySet().stream()
                .filter(it -> !it.getValue().isNotified())
                .forEach(it -> {
                    Student student = it.getValue();
                    Map<Course, CourseStatistic> coursesStatistics = student.getCoursesStatistics();
                    for (var courseEntry : coursesStatistics.entrySet()) {
                        int earnedPoints = courseEntry.getValue().getCoursePoints();
                        int totalPoints = courseEntry.getKey().getTotalPoints();
                        if (earnedPoints >= totalPoints) {
                            studentsWithCompletedCourses.merge(
                                    student,
                                    new ArrayList<>(List.of(courseEntry.getKey())),
                                    (oldValue, newValue) -> {
                                        oldValue.addAll(newValue);
                                        return oldValue;
                                    }
                            );
                        }
                    }
                });

        return studentsWithCompletedCourses;
    }

    public void updatePointsAndTasks(String studentId, List<Integer> points) {
        Student student = students.get(Integer.parseInt(studentId));
        Map<Course, CourseStatistic> coursesStatistics = student.getCoursesStatistics();
        coursesStatistics.get(Course.JAVA).updateCoursePointsAndTasks(points.get(0));
        coursesStatistics.get(Course.DSA).updateCoursePointsAndTasks(points.get(1));
        coursesStatistics.get(Course.DATABASES).updateCoursePointsAndTasks(points.get(2));
        coursesStatistics.get(Course.SPRING).updateCoursePointsAndTasks(points.get(3));
    }

    private boolean isEmailTaken(String email) {
        return students.values().stream()
                .map(Student::getEmail)
                .anyMatch(it -> it.equals(email));
    }

    private boolean isInvalidFirstName(String firstName) {
        return !firstName.matches(NAME_REGEX);
    }

    private boolean isInvalidLastName(List<String> lastNameParts) {
        for (String lastNamePart : lastNameParts) {
            if (!lastNamePart.matches(NAME_REGEX)) {
                return true;
            }
        }
        return false;
    }

    private boolean isInvalidEmail(String email) {
        return !email.matches(EMAIL_REGEX);
    }
}
