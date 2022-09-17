package edu.lysak.converter.model;

import java.util.ArrayList;
import java.util.List;

public class XmlElement {
    private String path;
    private String value;
    private final List<XmlAttribute> attributes = new ArrayList<>();

    public void setPath(String path) {
        this.path = path;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void addAttribute(XmlAttribute attribute) {
        this.attributes.add(attribute);
    }

    public String getPath() {
        return path;
    }

    public String getValue() {
        return value;
    }

    public List<XmlAttribute> getAttributes() {
        return attributes;
    }
}
