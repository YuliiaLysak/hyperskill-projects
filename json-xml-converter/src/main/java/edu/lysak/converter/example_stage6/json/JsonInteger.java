package edu.lysak.converter.example_stage6.json;

public class JsonInteger extends JsonNumber {
    protected final int value;

    public JsonInteger(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
