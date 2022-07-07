package edu.lysak.phonebook.model;

public class SearchResult {
    private final int all;
    private final int found;

    public SearchResult(int all, int found) {
        this.all = all;
        this.found = found;
    }

    public int getAll() {
        return all;
    }

    public int getFound() {
        return found;
    }
}
