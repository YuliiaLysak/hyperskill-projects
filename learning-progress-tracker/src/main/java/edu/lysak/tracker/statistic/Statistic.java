package edu.lysak.tracker.statistic;

import edu.lysak.tracker.Course;

import java.util.List;

public class Statistic {
    private List<Course> mostPopular;
    private List<Course> leastPopular;
    private List<Course> highestActivity;
    private List<Course> lowestActivity;
    private List<Course> easiestCourses;
    private List<Course> hardestCourses;

    public List<Course> getMostPopular() {
        return mostPopular;
    }

    public void setMostPopular(List<Course> mostPopular) {
        this.mostPopular = mostPopular;
    }

    public List<Course> getLeastPopular() {
        return leastPopular;
    }

    public void setLeastPopular(List<Course> leastPopular) {
        this.leastPopular = leastPopular;
    }

    public List<Course> getHighestActivity() {
        return highestActivity;
    }

    public void setHighestActivity(List<Course> highestActivity) {
        this.highestActivity = highestActivity;
    }

    public List<Course> getLowestActivity() {
        return lowestActivity;
    }

    public void setLowestActivity(List<Course> lowestActivity) {
        this.lowestActivity = lowestActivity;
    }

    public List<Course> getEasiestCourses() {
        return easiestCourses;
    }

    public void setEasiestCourses(List<Course> easiestCourses) {
        this.easiestCourses = easiestCourses;
    }

    public List<Course> getHardestCourses() {
        return hardestCourses;
    }

    public void setHardestCourses(List<Course> hardestCourses) {
        this.hardestCourses = hardestCourses;
    }
}
