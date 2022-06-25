package edu.lysak.tracker;

import edu.lysak.tracker.statistic.CourseStatistic;

import java.util.HashMap;
import java.util.Map;

public class Student {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final Map<Course, CourseStatistic> coursesStatistics;
    private boolean notified = false;

    public Student(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        coursesStatistics = new HashMap<>(Map.of(
                Course.JAVA, new CourseStatistic(0, 0),
                Course.DSA, new CourseStatistic(0, 0),
                Course.DATABASES, new CourseStatistic(0, 0),
                Course.SPRING, new CourseStatistic(0, 0)
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

    public Map<Course, CourseStatistic> getCoursesStatistics() {
        return coursesStatistics;
    }

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }
}
