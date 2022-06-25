package edu.lysak.tracker.statistic;

public class CourseStatistic {
    private int completedTasks;
    private int coursePoints;

    public CourseStatistic(int completedTasks, int coursePoints) {
        this.completedTasks = completedTasks;
        this.coursePoints = coursePoints;
    }

    public int getCompletedTasks() {
        return completedTasks;
    }

    public int getCoursePoints() {
        return coursePoints;
    }

    public void updateCoursePointsAndTasks(Integer morePoints) {
        if (morePoints > 0) {
            this.coursePoints += morePoints;
            completedTasks++;
        }
    }
}
