package edu.lysak.converter;

import edu.lysak.converter.model.XmlElement;
import edu.lysak.converter.service.Converter;
import edu.lysak.converter.service.JsonConverter;
import edu.lysak.converter.service.XmlConverter;
import edu.lysak.converter.service.XmlParser;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        String fileName = "test-3.txt";
//        executeConverting(fileName);
        executeParsing(fileName);
    }

    private static void executeConverting(String fileName) throws IOException {
        String input = Files.readString(Path.of(fileName))
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

    private static void executeParsing(String fileName) throws XMLStreamException {
        XmlParser xmlParser = new XmlParser(fileName);
        List<XmlElement> xmlElements = xmlParser.parseXml();
        xmlParser.printElements(xmlElements);
    }

}
