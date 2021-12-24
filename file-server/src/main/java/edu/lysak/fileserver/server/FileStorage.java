package edu.lysak.fileserver.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileStorage {
//    private static final String STORAGE_FOLDER = System.getProperty("user.dir") + "/File Server/task/src/server/data/";
    private static final String STORAGE_FOLDER = "file-server/src/main/resources/server/data/";

    public void addFile(String fileName, String fileContent) throws IOException {
        Files.writeString(Path.of(STORAGE_FOLDER + fileName), fileContent);
    }

    public String getFileContent(String fileName) throws IOException {
        return new String(Files.readAllBytes(Path.of(STORAGE_FOLDER + fileName)));
    }

    public boolean deleteFile(String fileName) throws IOException {
        return Files.deleteIfExists(Path.of(STORAGE_FOLDER + fileName));
    }

    public boolean contains(String fileName) {
        return Files.exists(Path.of(STORAGE_FOLDER + fileName));
    }
}
