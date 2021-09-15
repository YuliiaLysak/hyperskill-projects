package edu.lysak.advisor;

import java.util.List;

public class Release {
    private String albumName;
    private List<String> artists;
    private String href;

    public Release(String albumName, List<String> artists, String href) {
        this.albumName = albumName;
        this.artists = artists;
        this.href = href;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public List<String> getArtists() {
        return artists;
    }

    public void setArtists(List<String> artists) {
        this.artists = artists;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @Override
    public String toString() {
        return String.format("%s%n%s%n%s%n", albumName, artists, href);
    }
}
