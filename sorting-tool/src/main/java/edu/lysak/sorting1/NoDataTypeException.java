package edu.lysak.sorting1;

public class NoDataTypeException extends RuntimeException {
    @Override
    public String getMessage() {
        return "No data type defined!";
    }
}
