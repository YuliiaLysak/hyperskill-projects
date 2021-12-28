package edu.lysak.fileserver.client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ClientFileStorage {
    private final String baseClientStorageFolder;

    public ClientFileStorage(String baseClientStorageFolder) {
        this.baseClientStorageFolder = baseClientStorageFolder;
    }

    public byte[] getFileByName(String fileName) throws IOException {
        return Files.readAllBytes(Path.of(baseClientStorageFolder + fileName));
    }

    public void addFile(String localFileName, byte[] fileBytes) throws IOException {
        Files.write(Path.of(baseClientStorageFolder + localFileName), fileBytes);
    }
}
