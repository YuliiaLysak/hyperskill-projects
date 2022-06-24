package edu.lysak.tracker;

import java.util.HashMap;
import java.util.Map;

public class Student {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final Map<Course, Integer> coursesPoints;

    public Student(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        coursesPoints = new HashMap<>(Map.of(
                Course.JAVA, 0,
                Course.DSA, 0,
                Course.DATABASES, 0,
                Course.SPRING, 0
        ));
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Map<Course, Integer> getCoursesPoints() {
        return coursesPoints;
    }
}
