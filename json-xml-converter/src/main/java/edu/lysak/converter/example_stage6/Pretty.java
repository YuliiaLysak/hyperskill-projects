package edu.lysak.converter.example_stage6;

public interface Pretty {
    String INDENT = "  ";

    String toPretty();

    default String toPretty(int level) {
        return toPretty();
    }
}
