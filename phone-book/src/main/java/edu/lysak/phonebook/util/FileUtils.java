package edu.lysak.phonebook.util;

import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public final class FileUtils {
    private FileUtils() {
    }

    public static List<String> getDataFromFile(String fileName) {
        try {
            URL url = FileUtils.class.getClassLoader().getResource(fileName);
            URI uri = Objects.requireNonNull(url).toURI();
            return Files.readAllLines(Path.of(uri));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }
}
