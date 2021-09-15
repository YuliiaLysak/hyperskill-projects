package edu.lysak.advisor;

public class Playlist {
    private String name;
    private String href;

    public Playlist(String name, String href) {
        this.name = name;
        this.href = href;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @Override
    public String toString() {
        return String.format("%s%n%s%n", name, href);
    }
}
