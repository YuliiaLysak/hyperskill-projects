package edu.lysak.converter.example_stage6.json;

public class JsonBoolean extends JsonPrimitive {
    protected final boolean value;

    public JsonBoolean(boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
