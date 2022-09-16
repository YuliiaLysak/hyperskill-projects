package edu.lysak.converter.model;

public class Attribute {
    private final String attrKey;
    private final String attrValue;

    public Attribute(String attrKey, String attrValue) {
        this.attrKey = attrKey;
        this.attrValue = attrValue;
    }

    public String getAttrKey() {
        return attrKey;
    }

    public String getAttrValue() {
        return attrValue;
    }
}
