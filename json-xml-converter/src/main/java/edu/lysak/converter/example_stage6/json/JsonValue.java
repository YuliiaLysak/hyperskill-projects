package edu.lysak.converter.example_stage6.json;

import edu.lysak.converter.example_stage6.Pretty;

public abstract class JsonValue implements Pretty {
    @Override
    public String toPretty() {
        return toString();
    }

}
