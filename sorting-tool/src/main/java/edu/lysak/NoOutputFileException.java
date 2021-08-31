package edu.lysak;

public class NoOutputFileException extends RuntimeException {
    @Override
    public String getMessage() {
        return "No output file defined!";
    }
}
