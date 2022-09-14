package edu.lysak.analyzer;

public class PatternInfo {
    private final int priority;
    private final String pattern;
    private final String resultString;

    public PatternInfo(int priority, String pattern, String resultString) {
        this.priority = priority;
        this.pattern = pattern;
        this.resultString = resultString;
    }

    public int getPriority() {
        return priority;
    }

    public String getPattern() {
        return pattern;
    }

    public String getResultString() {
        return resultString;
    }
}
