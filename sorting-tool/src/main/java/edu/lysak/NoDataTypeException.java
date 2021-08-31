package edu.lysak;

public class NoDataTypeException extends RuntimeException {
    @Override
    public String getMessage() {
        return "No data type defined!";
    }
}
