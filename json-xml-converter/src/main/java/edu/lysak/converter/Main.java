package edu.lysak.converter;

import edu.lysak.converter.converter.Converter;
import edu.lysak.converter.converter.JsonConverter;
import edu.lysak.converter.converter.XmlConverter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        URL resource = Main.class.getClassLoader().getResource("test.txt");
        String input = Files.readString(Path.of(resource.toURI()))
                .replace(System.lineSeparator(), "");
        Converter converter;
        if (input.startsWith("<")) {
            converter = new XmlConverter();
        } else if (input.startsWith("{")) {
            converter = new JsonConverter();
        } else {
            System.out.println("Invalid input");
            return;
        }

        System.out.println(converter.convert(input));
    }
}
