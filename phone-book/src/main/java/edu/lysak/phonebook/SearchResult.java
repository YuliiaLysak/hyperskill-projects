package edu.lysak.phonebook;

public class SearchResult {
    private int all;
    private int found;

    public SearchResult() {
    }

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
