package edu.lysak.tracker;

public enum Course {
    JAVA("Java"),
    DSA("Data Structures and Algorithms"),
    DATABASES("Databases"),
    SPRING("Spring");

    private final String courseName;

    Course(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseName() {
        return courseName;
    }
}
