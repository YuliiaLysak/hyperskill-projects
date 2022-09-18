package edu.lysak.converter.example_stage5.js;

import edu.lysak.converter.example_stage5.Pretty;

public abstract class JsValue implements Pretty {
    @Override
    public String toPretty() {
        return toString();
    }

    public boolean isSimple() {
        return true;
    }
}
