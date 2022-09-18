package edu.lysak.converter.model;

import java.util.List;

public class Element {
    private String key;
    private String value;
    private List<XmlAttribute> attributes;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<XmlAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<XmlAttribute> attributes) {
        this.attributes = attributes;
    }
}
