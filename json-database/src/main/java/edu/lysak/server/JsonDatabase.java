package edu.lysak.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class JsonDatabase {
    private static final String DB_PATH = "json-database/src/main/resources/server/data/db.json";
    private static final Path PATH = Path.of(DB_PATH);

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    private final Gson gson;

    public JsonDatabase(Gson gson) {
        this.gson = gson;
    }

    public void set(String key, String value) throws IOException {
        writeLock.lock();

        Map<String, String> database = readDbContent();
        database.put(key, value);
        Files.writeString(PATH, gson.toJson(database));

        writeLock.unlock();
    }

    public String get(String key) throws IOException {
        readLock.lock();

        Map<String, String> database = readDbContent();
        String value = database.get(key);

        readLock.unlock();
        return value;
    }

    public boolean delete(String key) throws IOException {
        writeLock.lock();

        Map<String, String> database = readDbContent();
        String deletedValue = database.remove(key);
        Files.writeString(PATH, gson.toJson(database));

        writeLock.unlock();
        return deletedValue != null;
    }

    private Map<String, String> readDbContent() throws IOException {
        String dbString = new String(Files.readAllBytes(PATH));
        Type typeOfMap = new TypeToken<Map<String, String>>() {
        }.getType();
        return gson.fromJson(dbString, typeOfMap);
    }
}
