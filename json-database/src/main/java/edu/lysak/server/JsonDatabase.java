package edu.lysak.server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

    public void set(JsonArray keys, JsonElement value) throws IOException {
        writeLock.lock();

        JsonObject database = readDbContent();
        String firstKey = keys.get(0).getAsString();
        String lastKey = keys.get(keys.size() - 1).getAsString();

        if (keys.size() == 1) {
            database.add(firstKey, value);
        } else {
            JsonElement parentElement = database.get(firstKey);
            for (int i = 1; i < keys.size() - 1; i++) {
                if (parentElement.isJsonObject()) {
                    parentElement = parentElement.getAsJsonObject().get(keys.get(i).getAsString());
                } else {
                    parentElement = parentElement.getAsJsonPrimitive();
                }
            }
            parentElement.getAsJsonObject().add(lastKey, value);
        }
        Files.writeString(PATH, gson.toJson(database));

        writeLock.unlock();
    }

    public JsonElement get(JsonArray keys) throws IOException {
        readLock.lock();

        JsonObject database = readDbContent();
        JsonElement value = database.get(keys.get(0).getAsString());
        for (int i = 1; i < keys.size(); i++) {
            if (value.isJsonObject()) {
                value = value.getAsJsonObject().get(keys.get(i).getAsString());
            } else {
                value = value.getAsJsonPrimitive();
            }
        }

        readLock.unlock();
        return value;
    }

    public boolean delete(JsonArray keys) throws IOException {
        writeLock.lock();

        JsonObject database = readDbContent();
        String firstKey = keys.get(0).getAsString();
        String lastKey = keys.get(keys.size() - 1).getAsString();
        JsonElement deletedValue = null;

        if (keys.size() == 1) {
            deletedValue = database.remove(firstKey);
        } else {
            JsonElement parentValue = database.get(firstKey);
            for (int i = 1; i < keys.size() - 1; i++) {
                parentValue = parentValue.getAsJsonObject().get(keys.get(i).getAsString());
            }

            if (parentValue.getAsJsonObject().has(lastKey)) {
                deletedValue = parentValue.getAsJsonObject().remove(lastKey);
            }
        }

        Files.writeString(PATH, gson.toJson(database));

        writeLock.unlock();
        return deletedValue != null;
    }

    private JsonObject readDbContent() throws IOException {
        String dbString = new String(Files.readAllBytes(PATH));
        if (dbString.isEmpty()) {
            return new JsonObject();
        }
        return gson.fromJson(dbString, JsonObject.class);
    }
}
