package edu.lysak.converter.converter;

import edu.lysak.converter.model.Attribute;
import edu.lysak.converter.model.Element;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlConverter implements Converter {
    private static final String ATTRIBUTES_REGEX = "(?<attrKey>[\\w-]+)\\s?=\\s?\"(?<attrValue>[\\w-]+)\"";
    private static final String SELF_CLOSING_ELEMENT_REGEX = "^<(?<key>[\\w-]+)(?<attributes>\\s?(?<attrKey>[\\w-]+)\\s?=\\s?\"(?<attrValue>[\\w-]+)\")*\\s?/>$";
    private static final String FULL_ELEMENT_REGEX = "^<(?<key>[\\w-]*)(?<attributes>\\s?(?<attrKey>[\\w-]+)\\s?=\\s?\"(?<attrValue>[\\w-]+)\")*>(?<value>[^<>]*)</[^<>]*>";

    @Override
    public String convert(String input) {
        Element xmlElement;
        if (input.contains("/>")) {
            xmlElement = parseSelfClosingXmlElement(input);
        } else {
            xmlElement = parseFullXmlElement(input);
        }
        return convertXmlToJson(xmlElement);
    }

    private Element parseSelfClosingXmlElement(String input) {
        Pattern selfClosingPattern = Pattern.compile(SELF_CLOSING_ELEMENT_REGEX);
        Matcher matcher = selfClosingPattern.matcher(input);
        String key = EMPTY;
        if (matcher.find()) {
            key = matcher.group("key");
        }

        Element element = new Element();
        element.setKey(key);
        element.setValue(EMPTY);
        element.setAttributes(getAttributes(input));
        return element;
    }

    private Element parseFullXmlElement(String input) {
        Element element = parseBaseElement(input, FULL_ELEMENT_REGEX);
        element.setAttributes(getAttributes(input));
        return element;
    }

    private List<Attribute> getAttributes(String input) {
        List<Attribute> attributes;
        if (input.contains("=")) {
            attributes = parseAttributes(
                    input.substring(input.indexOf(" "), input.indexOf(">")),
                    ATTRIBUTES_REGEX
            );
        } else {
            attributes = List.of();
        }
        return attributes;
    }

    private String convertXmlToJson(Element xmlElement) {
        List<Attribute> attributes = xmlElement.getAttributes();
        String key = xmlElement.getKey();
        String value = xmlElement.getValue();
        if (attributes.isEmpty()) {
            return getResultWithoutAttributes(key, value);
        }

        return getResultWithAttributes(key, value, attributes);
    }

    private String getResultWithoutAttributes(String key, String value) {
        if (value == null || value.isEmpty()) {
            return String.format("{\"%s\": %s}", key, null);
        } else {
            return String.format("{\"%s\": \"%s\"}", key, value);
        }
    }

    private String getResultWithAttributes(String key, String value, List<Attribute> attributes) {
        StringBuilder aggregatedAttributes = new StringBuilder();
        attributes.forEach(a -> aggregatedAttributes.append(String.format(
                "\"@%s\": \"%s\",",
                a.getAttrKey(),
                a.getAttrValue()
        )));

        if (value == null || value.isEmpty()) {
            return String.format("{\"%s\": {%s \"#%s\": %s } }", key, aggregatedAttributes, key, null);
        } else {
            return String.format("{\"%s\": {%s \"#%s\": \"%s\"} }", key, aggregatedAttributes, key, value);
        }
    }
}
