package edu.lysak.converter.example_stage4;

import edu.lysak.converter.example_stage4.js.JsConverter;
import edu.lysak.converter.example_stage4.js.JsObject;
import edu.lysak.converter.example_stage4.js.JsParser;
import edu.lysak.converter.example_stage4.x.XElement;
import edu.lysak.converter.example_stage4.x.XPrinter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        final JsObject json = new JsParser().parse(getInputFromFile());
        final List<XElement> xmlList = new JsConverter().convert(json);
        final XPrinter printer = new XPrinter();
        for (XElement element : xmlList) {
            printer.print(element);
        }
    }

    private static String getInputFromFile() throws IOException, URISyntaxException {
//        String filePath = "stage4/test-stage4-json.txt";
//        String filePath = "stage4/test-stage4-json-2.txt";
        String filePath = "stage4/test-stage4-json-3.txt";
        URL url = Objects.requireNonNull(Main.class.getClassLoader().getResource(filePath));
        return Files.readString(Path.of(url.toURI()));
    }
}
