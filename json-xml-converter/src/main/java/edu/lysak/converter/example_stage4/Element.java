package edu.lysak.converter.example_stage4;

public abstract class Element extends Value {
    public final String name;
    public final Value value;

    public Element(String name) {
        this(name, null);
    }

    public Element(String name, Value value) {
        this.name = name;
        this.value = value;
    }
}
