package edu.lysak.converter.example_stage6.xml;

public class XmlSimpleValue extends XmlValue {
    protected final String value;

    public XmlSimpleValue() {
        this("");
    }

    public XmlSimpleValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
