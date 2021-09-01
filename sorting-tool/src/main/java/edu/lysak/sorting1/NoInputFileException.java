package edu.lysak.sorting1;

public class NoInputFileException extends RuntimeException {
    @Override
    public String getMessage() {
        return "No input file defined!";
    }
}
