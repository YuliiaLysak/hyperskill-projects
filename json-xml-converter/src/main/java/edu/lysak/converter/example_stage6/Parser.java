package edu.lysak.converter.example_stage6;

public interface Parser<T extends Subject> {
    T parse(String input);
}
