package edu.lysak.converter.service;

import edu.lysak.converter.model.XmlAttribute;
import edu.lysak.converter.model.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Converter {
    String EMPTY = "";

    String convert(String input);

    default Element parseBaseElement(String input, String regex) {
        Pattern jsonPattern = Pattern.compile(regex);
        Matcher matcher = jsonPattern.matcher(input);
        String key = EMPTY;
        String value = EMPTY;
        if (matcher.find()) {
            key = matcher.group("key");
            value = matcher.group("value");
        }

        Element element = new Element();
        element.setKey(key);
        element.setValue(value);
        return element;
    }

    default List<XmlAttribute> parseAttributes(String allAttributes, String regex) {
        ArrayList<XmlAttribute> attributes = new ArrayList<>();
        Pattern attributesPattern = Pattern.compile(regex);
        Matcher matcher = attributesPattern.matcher(allAttributes);
        while (matcher.find()) {
            String attrKey = matcher.group("attrKey");
            String attrValue = matcher.group("attrValue");
            attributes.add(new XmlAttribute(attrKey, attrValue));
        }
        return attributes;
    }
}
