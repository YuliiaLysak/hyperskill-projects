package edu.lysak.tracker;

public enum Course {
    JAVA("Java", 600),
    DSA("DSA", 400),
    DATABASES("Databases", 480),
    SPRING("Spring", 550),
    UNKNOWN_COURSE("Unknown course", 0);

    private final String name;
    private final int totalPoints;

    Course(String name, int totalPoints) {
        this.name = name;
        this.totalPoints = totalPoints;
    }

    public String getName() {
        return name;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public static Course valueFrom(String courseName) {
        for (Course course : values()) {
            if (courseName.equalsIgnoreCase(course.getName())) {
                return course;
            }
        }
        return UNKNOWN_COURSE;
    }
}
