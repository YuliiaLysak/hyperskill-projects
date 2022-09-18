package edu.lysak.converter.example_stage6;

public interface Converter<T extends Subject, R extends Subject> {
    R convert(T subject);
}
