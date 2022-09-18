package edu.lysak.converter.example_stage5.js;

public class JsDouble extends JsNumber {
    protected final double value;

    public JsDouble(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }
}
