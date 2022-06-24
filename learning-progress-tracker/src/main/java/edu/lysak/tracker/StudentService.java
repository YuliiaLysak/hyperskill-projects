package edu.lysak.tracker;

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

    public void updatePoints(String studentId, List<Integer> points) {
        Student student = students.get(Integer.parseInt(studentId));
        Map<Course, Integer> coursesPoints = student.getCoursesPoints();
        coursesPoints.merge(Course.JAVA, points.get(0), Integer::sum);
        coursesPoints.merge(Course.DSA, points.get(1), Integer::sum);
        coursesPoints.merge(Course.DATABASES, points.get(2), Integer::sum);
        coursesPoints.merge(Course.SPRING, points.get(3), Integer::sum);
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
