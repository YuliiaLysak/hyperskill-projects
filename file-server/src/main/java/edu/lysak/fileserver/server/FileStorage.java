package edu.lysak.fileserver.server;

import edu.lysak.fileserver.util.SerializationUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class FileStorage {
    private final String identifiersFileName;
    private final String baseServerStorageFolder;
    private Map<Integer, String> identifiers;

    public FileStorage(String identifiersFileName, String baseServerStorageFolder) {
        this.identifiersFileName = identifiersFileName;
        this.baseServerStorageFolder = baseServerStorageFolder;
    }

    public int addFile(String fileName, byte[] fileBytes) throws IOException {
        if (identifiersFileName.equals(fileName)) {
            throw new IllegalArgumentException("This file name is reserved for internal use.");
        }
        int id = getId();
        identifiers.put(id, fileName);
        updateIdentifiersFile();
        Files.write(Path.of(baseServerStorageFolder + fileName), fileBytes);
        return id;
    }

    public byte[] getFileById(int id) throws IOException {
        String fileName = identifiers.get(id);
        Path filePath = Path.of(baseServerStorageFolder + fileName);
        if (fileName != null && Files.exists(filePath)) {
            return Files.readAllBytes(filePath);
        }
        return null;
    }

    public byte[] getFileByName(String fileName) throws IOException {
        System.out.println("FILE_NAME = " + fileName);
        Path filePath = Path.of(baseServerStorageFolder + fileName);
        if (Files.exists(filePath) && !identifiersFileName.equals(fileName)) {
            return Files.readAllBytes(filePath);
        }
        return null;
    }

    public boolean deleteFileById(int id) throws IOException {
        String fileName = identifiers.remove(id);
        boolean isDeleted = Files.deleteIfExists(Path.of(baseServerStorageFolder + fileName));
        updateIdentifiersFile();
        return isDeleted;
    }

    public boolean deleteFileByName(String fileName) throws IOException {
        boolean isDeleted = Files.deleteIfExists(Path.of(baseServerStorageFolder + fileName));
        updateIdentifiersFile();
        return isDeleted;
    }

    public boolean contains(String fileName) {
        return Files.exists(Path.of(baseServerStorageFolder + fileName));
    }

    private int getId() {
        int id;
        do {
            id = new SecureRandom().nextInt(1000);
        } while (identifiers.containsKey(id));
        return id;
    }

    private void updateIdentifiersFile() throws IOException {
        String filePath = baseServerStorageFolder + identifiersFileName;
        SerializationUtils.serialize(identifiers, filePath);
    }

    public void init() throws IOException, ClassNotFoundException {
        String filePath = baseServerStorageFolder + identifiersFileName;
        if (Files.exists(Path.of(filePath))) {
            identifiers = (Map<Integer, String>) SerializationUtils.deserialize(filePath);
        } else {
            identifiers = new HashMap<>();
        }
    }
}
