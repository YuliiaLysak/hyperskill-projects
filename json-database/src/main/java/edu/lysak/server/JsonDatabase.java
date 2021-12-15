package edu.lysak.server;

import java.util.Arrays;

public class JsonDatabase {
    private final String[] database = new String[100];

    {
        Arrays.fill(database, "");
    }

    public boolean set(int index, String text) {
        if (isInvalid(index)) {
            return false;
        }
        database[index - 1] = text;
        return true;
    }

    public String get(int index) {
        if (isInvalid(index) || database[index - 1].isEmpty()) {
            return null;
        }
        return database[index - 1];
    }

    public boolean delete(int index) {
        if (isInvalid(index)) {
            return false;
        }
        database[index - 1] = "";
        return true;
    }

    private boolean isInvalid(int index) {
        return index < 1 || index > 100;
    }
}
