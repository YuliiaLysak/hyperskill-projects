package edu.lysak.tracker;

import java.util.ArrayList;
import java.util.List;

public class StudentService {
    private static final String NAME_REGEX = "^[a-zA-Z]+((['-]?[a-zA-Z]+['-]?)?|['-]?)[a-zA-Z]+$";
    private static final String EMAIL_REGEX = "^[a-zA-z.\\d]+@[a-zA-z\\d]+\\.[a-zA-z\\d]+$";
    private final List<Student> students = new ArrayList<>();

    public int getStudentsCount() {
        return students.size();
    }

    public List<Student> getStudents() {
        return new ArrayList<>(students);
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
        if (isInvalidEmail(email)) {
            System.out.println("Incorrect email.");
            return false;
        }

        students.add(new Student(
                firstName,
                String.join(" ", lastNameParts),
                email)
        );
        return true;
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
