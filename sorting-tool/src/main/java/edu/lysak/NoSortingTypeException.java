package edu.lysak;

public class NoSortingTypeException extends RuntimeException {
    @Override
    public String getMessage() {
        return "No sorting type defined!";
    }
}
