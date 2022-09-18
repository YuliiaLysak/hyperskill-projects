package edu.lysak.converter.example_stage5;

import edu.lysak.converter.example_stage5.js.JsConverter;
import edu.lysak.converter.example_stage5.js.JsObject;
import edu.lysak.converter.example_stage5.js.JsParser;
import edu.lysak.converter.example_stage5.x.XConverter;
import edu.lysak.converter.example_stage5.x.XElement;
import edu.lysak.converter.example_stage5.x.XParser;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        final String input = getInputFromFile();
        if (isJson(input)) {
            final JsParser parser = new JsParser();
            final JsObject json = parser.parse(input);
            final JsConverter converter = new JsConverter();
            final XElement xml = converter.convert(json);
            System.out.println(xml);
        } else if (isXml(input)) {
            final XParser parser = new XParser();
            final XElement xml = parser.parse(input);
            final XConverter converter = new XConverter();
            final JsObject json = converter.convert(xml);
            System.out.println(json.toPretty());
        } else {
            throw new IllegalArgumentException("Unknown input type: " + input);
        }
    }

    private static String getInputFromFile() throws IOException, URISyntaxException {
//        String filePath = "stage5/test-stage5-json.txt";
//        String filePath = "stage5/test-stage5-json-2.txt";
//        String filePath = "stage5/test-stage5-json-3.txt";
//        String filePath = "stage5/test-stage5-xml.txt";
//        String filePath = "stage5/test-stage5-xml-2.txt";
        String filePath = "stage5/test-stage5-xml-3.txt";
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
