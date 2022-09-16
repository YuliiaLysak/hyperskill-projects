package edu.lysak.converter.converter;

import edu.lysak.converter.model.Attribute;
import edu.lysak.converter.model.Element;

import java.util.List;

public class JsonConverter implements Converter {
    private static final String ATTRIBUTES_REGEX = "\"@(?<attrKey>[\\w-]+)\"\s?:\s?\"?(?<attrValue>[\\w-]+)\"?,?\s?";
    private static final String JSON_WITHOUT_ATTR_REGEX = "^\\{\"(?<key>.+)\"\\s?:\\s?\"?(?<value>[^\"]+)\"?}$";
    private static final String JSON_REGEX = "^\\{\s*\"(?<key>\\w+)\"\s*:\s*\\{\s*(?<attributes>\"@(?<attrKey>[\\w-]+)\"\s*:\s*\"?(?<attrValue>[\\w-]+)\"?,?\s*)*\s*\"#(?<key2>\\w+)\"\s*:\s*\"?(?<value>[^<>\"]+)\"?\s*}\s*}$";

    @Override
    public String convert(String input) {
        Element jsonElement;
        if (!input.contains("@")) {
            jsonElement = parseJsonElementWithoutAttributes(input);
        } else {
            jsonElement = parseJsonElement(input);
        }

        return convertJsonToXml(jsonElement);
    }

    private Element parseJsonElementWithoutAttributes(String input) {
        Element jsonElement = parseBaseElement(input, JSON_WITHOUT_ATTR_REGEX);
        jsonElement.setAttributes(List.of());
        return jsonElement;
    }

    private Element parseJsonElement(String input) {
        Element element = parseBaseElement(input, JSON_REGEX);
        element.setAttributes(getAttributes(input));
        return element;
    }

    private List<Attribute> getAttributes(String input) {
        List<Attribute> attributes;
        if (input.contains("@")) {
            attributes = parseAttributes(
                    input.substring(input.indexOf("@") - 1, input.indexOf("#") - 1),
                    ATTRIBUTES_REGEX
            );
        } else {
            attributes = List.of();
        }
        return attributes;

    }

    private String convertJsonToXml(Element jsonElement) {
        String key = jsonElement.getKey();
        String value = jsonElement.getValue();
        List<Attribute> attributes = jsonElement.getAttributes();
        if (attributes.isEmpty()) {
            return getResultWithoutAttributes(key, value);
        }

        return getResultWithAttributes(key, value, attributes);
    }

    private String getResultWithoutAttributes(String key, String value) {
        if (value.isEmpty() || value.contains("null")) {
            return String.format("<%s/>", key);
        } else {
            return String.format("<%s>%s</%s>", key, value, key);
        }
    }

    private String getResultWithAttributes(String key, String value, List<Attribute> attributes) {
        StringBuilder aggregatedAttributes = new StringBuilder();
        attributes.forEach(a -> aggregatedAttributes.append(String.format(
                "%s = \"%s\" ",
                a.getAttrKey(),
                a.getAttrValue()
        )));

        if (value.isEmpty() || value.contains("null")) {
            return String.format("<%s %s/>", key, aggregatedAttributes);
        } else {
            return String.format("<%s %s>%s</%s>", key, aggregatedAttributes, value, key);
        }
    }
}
