package edu.lysak.converter.model;

public class XmlAttribute {
    private final String attrKey;
    private final String attrValue;

    public XmlAttribute(String attrKey, String attrValue) {
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
