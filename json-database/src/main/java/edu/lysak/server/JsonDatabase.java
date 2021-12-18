package edu.lysak.server;

import java.util.HashMap;
import java.util.Map;

public class JsonDatabase {
    private final Map<String, String> database = new HashMap<>();

    public void set(String key, String value) {
        database.put(key, value);
    }

    public String get(String key) {
        return database.get(key);
    }

    public boolean delete(String key) {
        String deletedValue = database.remove(key);
        return deletedValue != null;
    }
}
