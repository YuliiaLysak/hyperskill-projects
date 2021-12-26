package edu.lysak.fileserver.client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static edu.lysak.fileserver.util.Constants.CLIENT_STORAGE_FOLDER;

public class ClientFileStorage {

    public byte[] getFileByName(String fileName) throws IOException {
        return Files.readAllBytes(Path.of(CLIENT_STORAGE_FOLDER + fileName));
    }

    public void addFile(String localFileName, byte[] fileBytes) throws IOException {
        Files.write(Path.of(CLIENT_STORAGE_FOLDER + localFileName), fileBytes);
    }
}
