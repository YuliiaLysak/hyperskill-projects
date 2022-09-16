package edu.lysak.converter;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim();
        if (input.startsWith("<")) {
            parseXml(input);
        } else if (input.startsWith("{")) {
            parseJson(input);
        } else {
            System.out.println("Invalid input");
        }
    }

    private static void parseXml(String input) {
        String regex = "^<(?<key>[^<>]*)>(?<value>[^<>]*)</[^<>]*>|<(?<keyForEmptyValue>[^<>]*)/>$";
        Pattern xmlPattern = Pattern.compile(regex);
        Matcher matcher = xmlPattern.matcher(input);
        if (matcher.find()) {
            String key = matcher.group("key");
            String value = matcher.group("value");
            if (value == null) {
                key = matcher.group("keyForEmptyValue");
                value = "";
            }
            System.out.println(convertXmlToJson(key, value));
        }
    }

    private static String convertXmlToJson(String key, String value) {
        if (value.isEmpty()) {
            return String.format("{\"%s\":%s}", key, null);
        } else {
            return String.format("{\"%s\":\"%s\"}", key, value);
        }
    }

    private static void parseJson(String input) {
        String regex = "^\\{\"(?<key>.+)\"\\s?:\\s?\"?(?<value>[^\"]+)\"?}$";
        Pattern jsonPattern = Pattern.compile(regex);
        Matcher matcher = jsonPattern.matcher(input);
        if (matcher.find()) {
            String key = matcher.group("key");
            String value = matcher.group("value");
            System.out.println(convertJsonToXml(key, value));
        }
    }

    private static String convertJsonToXml(String key, String value) {
        if ("null".equals(value)) {
            return String.format("<%s/>", key);
        } else {
            return String.format("<%s>%s</%s>", key, value, key);
        }
    }
}
