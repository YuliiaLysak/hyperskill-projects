package edu.lysak.tracker.statistic;

public class StudentStatistic {
    private final int studentId;
    private final int earnedPoints;
    private final double completed;

    public StudentStatistic(int studentId, int earnedPoints, double completed) {
        this.studentId = studentId;
        this.earnedPoints = earnedPoints;
        this.completed = completed;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getEarnedPoints() {
        return earnedPoints;
    }

    public double getCompleted() {
        return completed;
    }
}
