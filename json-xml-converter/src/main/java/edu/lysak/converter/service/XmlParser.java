package edu.lysak.converter.service;

import edu.lysak.converter.model.XmlAttribute;
import edu.lysak.converter.model.XmlElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class XmlParser {
    private static final Deque<XmlElement> ELEMENTS_DEQUE = new ArrayDeque<>();
    private static final Deque<String> PATH_DEQUE = new ArrayDeque<>();

    private final XMLEventReader xmlReader;

    public XmlParser(String filePath) {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        try {
            URL url = Objects.requireNonNull(XmlParser.class.getClassLoader().getResource(filePath));
            File file = new File(url.toURI());
            xmlReader = xmlInputFactory.createXMLEventReader(new FileInputStream(file));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<XmlElement> parseXml() throws XMLStreamException {
        List<XmlElement> resultList = new ArrayList<>();
        // used for checking self-closing tag, e.g. <tag/>
        int characterOffsetStart = 0;
        //used for checking taf with empty value, e.g. <tag></tag>
        int columnNumberStart = 0;

        while (xmlReader.hasNext()) {
            XMLEvent element = xmlReader.nextEvent();
            if (element.isStartElement()) {
                characterOffsetStart = element.getLocation().getCharacterOffset();
                columnNumberStart = element.getLocation().getColumnNumber();
                StartElement startElement = element.asStartElement();
                PATH_DEQUE.offerLast(startElement.getName().getLocalPart());

                XmlElement xmlElement = new XmlElement();
                xmlElement.setPath(String.join(", ", PATH_DEQUE));
                ELEMENTS_DEQUE.offerLast(xmlElement);

                parseAttributes(startElement, xmlElement);
            }

            if (element.isCharacters()) {
                XmlElement xmlElement = ELEMENTS_DEQUE.peekLast();
                checkForElementValue(xmlElement, element);
            }

            if (element.isEndElement()) {
                PATH_DEQUE.pollLast();
                XmlElement xmlElement = ELEMENTS_DEQUE.pollLast();
                checkForNullElementValue(characterOffsetStart, xmlElement, element);
                checkForEmptyElementValue(columnNumberStart, xmlElement, element);
                characterOffsetStart = 0;
                columnNumberStart = 0;
                resultList.add(xmlElement);
            }
        }
        return resultList;
    }

    private void parseAttributes(StartElement startElement, XmlElement xmlElement) {
        Iterator<Attribute> attributes = startElement.getAttributes();
        if (attributes.hasNext()) {
            while (attributes.hasNext()) {
                Attribute attribute = attributes.next();
                String attrKey = attribute.getName().getLocalPart();
                String attrValue = attribute.getValue();
                xmlElement.addAttribute(new XmlAttribute(attrKey, attrValue));
            }
        }
    }

    private void checkForElementValue(XmlElement xmlElement, XMLEvent nextEvent) {
        String value = nextEvent.asCharacters().getData();
        if (!value.matches("[\\n\\r\\s]+")) {
            xmlElement.setValue(value);
        }
    }

    private void checkForNullElementValue(int characterOffsetStart, XmlElement xmlElement, XMLEvent nextEvent) {
        int characterOffsetEnd = nextEvent.getLocation().getCharacterOffset();
        if (characterOffsetStart == characterOffsetEnd) {
            xmlElement.setValue(null);
        }
    }

    private void checkForEmptyElementValue(int columnNumberStart, XmlElement xmlElement, XMLEvent nextEvent) {
        int columnNumberEnd = nextEvent.getLocation().getColumnNumber();
        String endTag = nextEvent.asEndElement().getName().getLocalPart();
        // 3 is a length of special chars "</>" in "</closing-tag>"
        if ((columnNumberEnd - endTag.length() - 3) == columnNumberStart) {
            xmlElement.setValue("");
        }
    }

    public void printElements(List<XmlElement> xmlElements) {
        xmlElements.forEach(xmlElement -> {
            System.out.println("Element:");
            System.out.printf("path = %s%n", xmlElement.getPath());
            if (xmlElement.getValue() == null) {
                System.out.println("value = " + null);
            } else {
                System.out.printf("value = \"%s\"%n", xmlElement.getValue());
            }
            if (!xmlElement.getAttributes().isEmpty()) {
                System.out.println("attributes:");
                xmlElement.getAttributes().forEach(a -> System.out.printf("%s = \"%s\"%n", a.getAttrKey(), a.getAttrValue()));
            }
            System.out.println();
        });
    }


    // ****************************************
    //
    // This method is implemented only for testing DOM parsing
    // Did not use it because couldn't detect self-closing and empty tag
    //
    // ****************************************
    private static void parseXmlViaDOM() throws Exception {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.parse(new File("test-3.txt"));
        document.getDocumentElement().normalize();
        Element root = document.getDocumentElement();

        String path = "path = " + root.getNodeName();
        System.out.println("Element:");
        System.out.println(path);
        System.out.println();
        NodeList rootChildNodes = root.getChildNodes();
        visitChildNodes(rootChildNodes, path);
    }

    private static void visitChildNodes(NodeList nList, String path) {
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node node = nList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                System.out.println("Element:");
                String innerPath = path + ", " + node.getNodeName();
                System.out.println(innerPath);

                boolean printValue = true;
                NodeList childNodes = node.getChildNodes();
                for (int i = 0; i < childNodes.getLength(); i++) {
                    Node item = childNodes.item(i);
                    if (item.getNodeType() != Node.TEXT_NODE) {
                        printValue = false;
                    }
                }

                if (printValue) {
                    System.out.println("value = " + node.getTextContent());
                }

                if (node.hasAttributes()) {
                    System.out.println("attributes:");
                    NamedNodeMap nodeMap = node.getAttributes();
                    for (int i = 0; i < nodeMap.getLength(); i++) {
                        Node tempNode = nodeMap.item(i);
                        System.out.printf("%s : \"%s\"%n", tempNode.getNodeName(), tempNode.getNodeValue());
                    }
                }

                if (node.hasChildNodes()) {
                    visitChildNodes(node.getChildNodes(), innerPath);
                }

                System.out.println();
            }
        }
    }
}
