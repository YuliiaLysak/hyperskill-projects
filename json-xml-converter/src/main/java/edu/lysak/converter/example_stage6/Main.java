package edu.lysak.converter.example_stage6;

import edu.lysak.converter.example_stage6.json.Json2XmlConverter;
import edu.lysak.converter.example_stage6.json.JsonObject;
import edu.lysak.converter.example_stage6.json.JsonParser;
import edu.lysak.converter.example_stage6.xml.Xml2JsonConverter;
import edu.lysak.converter.example_stage6.xml.XmlElement;
import edu.lysak.converter.example_stage6.xml.XmlParser;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        String input = getInputFromFile();
        if (isJson(input)) {
            Parser<JsonObject> parser = new JsonParser();
            JsonObject json = parser.parse(input);
            Converter<JsonObject, XmlElement> converter = new Json2XmlConverter();
            XmlElement xml = converter.convert(json);
            System.out.println(xml);
        } else if (isXml(input)) {
            Parser<XmlElement> parser = new XmlParser();
            XmlElement xml = parser.parse(input);
            Converter<XmlElement, JsonObject> converter = new Xml2JsonConverter();
            JsonObject json = converter.convert(xml);
            System.out.println(json.toPretty());
        } else {
            throw new IllegalArgumentException("Unknown input type: " + input);
        }
    }

    private static String getInputFromFile() throws IOException, URISyntaxException {
        String filePath = "stage6/test-stage6-json.txt";
//        String filePath = "stage6/test-stage6-json-2.txt";
//        String filePath = "stage6/test-stage6-json-3.txt";
//        String filePath = "stage6/test-stage6-xml.txt";
//        String filePath = "stage6/test-stage6-xml-2.txt";
//        String filePath = "stage6/test-stage6-xml-3.txt";
        URL url = Objects.requireNonNull(Main.class.getClassLoader().getResource(filePath));
        return Files.readString(Path.of(url.toURI()));
    }

    private static boolean isJson(String input) {
        return input.startsWith("{");
    }

    private static boolean isXml(String input) {
        return input.startsWith("<");
    }
}
