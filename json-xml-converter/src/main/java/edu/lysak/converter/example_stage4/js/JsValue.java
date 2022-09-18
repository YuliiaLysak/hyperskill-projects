package edu.lysak.converter.example_stage4.js;

public abstract class JsValue {
    public String toPretty() {
        return toString();
    }

    public boolean isSimple() {
        return true;
    }
}
