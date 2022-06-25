package edu.lysak.tracker.statistic;

import edu.lysak.tracker.Course;
import edu.lysak.tracker.Student;
import edu.lysak.tracker.StudentService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatisticService {
    private final StudentService studentService;
    private final Statistic statistic;

    public StatisticService(StudentService studentService, Statistic statistic) {
        this.studentService = studentService;
        this.statistic = statistic;
    }

    public String getMostPopular() {
        List<Course> mostPopular = statistic.getMostPopular();
        if (mostPopular.isEmpty()) {
            return "n/a";
        }
        return asJoinedString(mostPopular);
    }

    public String getLeastPopular() {
        List<Course> leastPopular = statistic.getLeastPopular();
        if (leastPopular.isEmpty()) {
            return "n/a";
        }
        return asJoinedString(leastPopular);
    }

    public String getHighestActivity() {
        List<Course> highestActivity = statistic.getHighestActivity();
        if (highestActivity.isEmpty()) {
            return "n/a";
        }
        return asJoinedString(highestActivity);
    }

    public String getLowestActivity() {
        List<Course> lowestActivity = statistic.getLowestActivity();
        if (lowestActivity.isEmpty()) {
            return "n/a";
        }
        return asJoinedString(lowestActivity);
    }

    public String getEasiestCourse() {
        List<Course> easiestCourses = statistic.getEasiestCourses();
        if (easiestCourses.isEmpty()) {
            return "n/a";
        }
        return asJoinedString(easiestCourses);
    }

    public String getHardestCourse() {
        List<Course> hardestCourses = statistic.getHardestCourses();
        if (hardestCourses.isEmpty()) {
            return "n/a";
        }
        return asJoinedString(hardestCourses);
    }

    public List<StudentStatistic> getStudentsByCourse(Course course) {
        Map<Integer, Student> students = studentService.getStudents();
        List<StudentStatistic> studentsByCourse = new ArrayList<>();
        for (var studentEntry : students.entrySet()) {
            CourseStatistic courseStatistic = studentEntry
                    .getValue()
                    .getCoursesStatistics()
                    .get(course);
            if (courseStatistic.getCoursePoints() == 0) {
                continue;
            }
            BigDecimal earnedPoints = BigDecimal.valueOf(courseStatistic.getCoursePoints());
            BigDecimal totalPoints = BigDecimal.valueOf(course.getTotalPoints());

            BigDecimal completed = earnedPoints
                    .multiply(BigDecimal.valueOf(100))
                    .divide(totalPoints, 1, RoundingMode.HALF_UP);

            studentsByCourse.add(new StudentStatistic(
                    studentEntry.getKey(),
                    courseStatistic.getCoursePoints(),
                    completed.doubleValue())
            );
        }

        studentsByCourse.sort(Comparator
                .comparingInt(StudentStatistic::getEarnedPoints)
                .reversed()
                .thenComparingInt(StudentStatistic::getStudentId)
        );
        return studentsByCourse;
    }

    public void initStatistics() {
        Map<Integer, Student> students = studentService.getStudents();

        var allStatistics = students.values().stream()
                .map(Student::getCoursesStatistics)
                .collect(Collectors.toList());

        int javaCompletedTasks = getCourseCompletedTasksCount(allStatistics, Course.JAVA);
        int dsaCompletedTasks = getCourseCompletedTasksCount(allStatistics, Course.DSA);
        int databasesCompletedTasks = getCourseCompletedTasksCount(allStatistics, Course.DATABASES);
        int springCompletedTasks = getCourseCompletedTasksCount(allStatistics, Course.SPRING);

        analyzePopularity(allStatistics);
        analyzeActivity(javaCompletedTasks, dsaCompletedTasks, databasesCompletedTasks, springCompletedTasks);
        analyzeDifficulty(allStatistics, javaCompletedTasks, dsaCompletedTasks, databasesCompletedTasks, springCompletedTasks);
    }

    private void analyzePopularity(List<Map<Course, CourseStatistic>> allStatistics) {
        long javaStudents = getStudentCount(allStatistics, Course.JAVA);
        long dsaStudents = getStudentCount(allStatistics, Course.DSA);
        long databasesStudents = getStudentCount(allStatistics, Course.DATABASES);
        long springStudents = getStudentCount(allStatistics, Course.SPRING);

        Map<Course, Long> allStudentsByCourse = Map.of(Course.JAVA, javaStudents, Course.DSA, dsaStudents, Course.DATABASES, databasesStudents, Course.SPRING, springStudents);
        setMostPopularCourse(allStudentsByCourse);
        setLeastPopularCourse(allStudentsByCourse);
    }

    private void analyzeActivity(int javaCompletedTasks, int dsaCompletedTasks, int databasesCompletedTasks, int springCompletedTasks) {
        Map<Course, Integer> allCoursesByCompletedTasks = Map.of(Course.JAVA, javaCompletedTasks, Course.DSA, dsaCompletedTasks, Course.DATABASES, databasesCompletedTasks, Course.SPRING, springCompletedTasks);
        setHighestActivity(allCoursesByCompletedTasks);
        setLowestActivity(allCoursesByCompletedTasks);
    }

    private void analyzeDifficulty(List<Map<Course, CourseStatistic>> allStatistics, int javaCompletedTasks, int dsaCompletedTasks, int databasesCompletedTasks, int springCompletedTasks) {
        int javaTotalPoints = getCourseTotalPoints(allStatistics, Course.JAVA);
        int dsaTotalPoints = getCourseTotalPoints(allStatistics, Course.DSA);
        int databasesTotalPoints = getCourseTotalPoints(allStatistics, Course.DATABASES);
        int springTotalPoints = getCourseTotalPoints(allStatistics, Course.SPRING);

        double javaAverageGrade = javaCompletedTasks > 0 ? 1.0 * javaTotalPoints / javaCompletedTasks : 0;
        double dsaAverageGrade = dsaCompletedTasks > 0 ? 1.0 * dsaTotalPoints / dsaCompletedTasks : 0;
        double databasesAverageGrade = databasesCompletedTasks > 0 ? 1.0 * databasesTotalPoints / databasesCompletedTasks : 0;
        double springAverageGrade = springCompletedTasks > 0 ? 1.0 * springTotalPoints / springCompletedTasks : 0;

        Map<Course, Double> allCoursesByAverageGrade = Map.of(Course.JAVA, javaAverageGrade, Course.DSA, dsaAverageGrade, Course.DATABASES, databasesAverageGrade, Course.SPRING, springAverageGrade);
        setEasiestCourses(allCoursesByAverageGrade);
        setHardestCourses(allCoursesByAverageGrade);
    }

    private void setMostPopularCourse(Map<Course, Long> allStudentsByCourse) {
        Long maxValue = allStudentsByCourse.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getValue)
                .orElse(0L);
        if (maxValue == 0) {
            statistic.setMostPopular(List.of());
        } else {
            List<Course> mostPopular = allStudentsByCourse.entrySet().stream()
                    .filter(it -> maxValue.equals(it.getValue()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            statistic.setMostPopular(mostPopular);
        }
    }

    private void setLeastPopularCourse(Map<Course, Long> allStudentsByCourse) {
        Long minValue = allStudentsByCourse.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getValue)
                .orElse(0L);
        if (minValue == 0) {
            statistic.setLeastPopular(List.of());
        } else {
            List<Course> leastPopular = allStudentsByCourse.entrySet().stream()
                    .filter(it -> minValue.equals(it.getValue()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            leastPopular.removeAll(statistic.getMostPopular());
            statistic.setLeastPopular(leastPopular);
        }
    }

    private void setHighestActivity(Map<Course, Integer> allCoursesByCompletedTasks) {
        Integer maxValue = allCoursesByCompletedTasks.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getValue)
                .orElse(0);
        if (maxValue == 0) {
            statistic.setHighestActivity(List.of());
        } else {
            List<Course> highestActivity = allCoursesByCompletedTasks.entrySet().stream()
                    .filter(it -> maxValue.equals(it.getValue()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            statistic.setHighestActivity(highestActivity);
        }
    }

    private void setLowestActivity(Map<Course, Integer> allCoursesByCompletedTasks) {
        Integer minValue = allCoursesByCompletedTasks.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getValue)
                .orElse(0);
        if (minValue == 0) {
            statistic.setLowestActivity(List.of());
        } else {
            List<Course> lowestActivity = allCoursesByCompletedTasks.entrySet().stream()
                    .filter(it -> minValue.equals(it.getValue()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            lowestActivity.removeAll(statistic.getHighestActivity());
            statistic.setLowestActivity(lowestActivity);
        }
    }

    private void setEasiestCourses(Map<Course, Double> allCoursesByAverageGrade) {
        Double maxValue = allCoursesByAverageGrade.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getValue)
                .orElse(0.0);
        if (maxValue == 0.0) {
            statistic.setEasiestCourses(List.of());
        } else {
            List<Course> easiestCourses = allCoursesByAverageGrade.entrySet().stream()
                    .filter(it -> maxValue.equals(it.getValue()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            statistic.setEasiestCourses(easiestCourses);
        }
    }

    private void setHardestCourses(Map<Course, Double> allCoursesByAverageGrade) {
        Double minValue = allCoursesByAverageGrade.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getValue)
                .orElse(0.0);
        if (minValue == 0.0) {
            statistic.setHardestCourses(List.of());
        } else {
            List<Course> hardestCourses = allCoursesByAverageGrade.entrySet().stream()
                    .filter(it -> minValue.equals(it.getValue()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            hardestCourses.removeAll(statistic.getEasiestCourses());
            statistic.setHardestCourses(hardestCourses);
        }
    }

    private long getStudentCount(List<Map<Course, CourseStatistic>> allStatistics, Course course) {
        return allStatistics
                .stream()
                .map(it -> it.get(course))
                .filter(it -> it.getCoursePoints() > 0)
                .count();
    }

    private int getCourseCompletedTasksCount(List<Map<Course, CourseStatistic>> allStatistics, Course course) {
        return allStatistics
                .stream()
                .map(it -> it.get(course).getCompletedTasks())
                .mapToInt(it -> it)
                .sum();
    }

    private int getCourseTotalPoints(List<Map<Course, CourseStatistic>> allStatistics, Course course) {
        return allStatistics
                .stream()
                .map(it -> it.get(course).getCoursePoints())
                .mapToInt(it -> it)
                .sum();
    }

    private String asJoinedString(List<Course> courses) {
        return courses
                .stream()
                .map(Course::getName)
                .collect(Collectors.joining(", "));
    }
}
