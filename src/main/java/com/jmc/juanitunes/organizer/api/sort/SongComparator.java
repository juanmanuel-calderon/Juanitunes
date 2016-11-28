package com.jmc.juanitunes.organizer.api.sort;

import java.util.Comparator;

import com.jmc.juanitunes.organizer.api.library.Song;

public enum SongComparator implements Comparator<Song> {
    BY_FILENAME(        "FILENAME",     (a, b) -> a.getFilename().compareTo(b.getFilename())),
    BY_TITLE(            "TITLE",         (a, b) -> a.getTitle().compareTo(b.getTitle())),
    BY_CDNUMBER(        "CD_NUMBER",     (a, b) -> Integer.compare(a.getCDNumber(), b.getCDNumber())),
    BY_TRACKNUMBER(    "TRACK_NUMBER", (a, b) -> Integer.compare(a.getTrackNumber(), b.getTrackNumber())),
    BY_GENRE(            "GENRE",         (a, b) -> a.getGenre().compareTo(b.getGenre())),
    BY_RATING(            "RATING",         (a, b) -> Integer.compare(a.getRating(), b.getRating())),
    BY_DURATION(        "DURATION",     (a, b) -> Integer.compare(a.getDurationInSeconds(), b.getDurationInSeconds())),
    BY_SIZE(            "SIZE",         (a, b) -> Double.compare(a.getSizeInMegaBytes(), b.getSizeInMegaBytes())),
    BY_BITRATE(        "BITRATE",         (a, b) -> Integer.compare(a.getBitrate(), b.getBitrate())),
    BY_EXTENSION(        "GENRE",         (a, b) -> a.getExtension().compareTo(b.getExtension()));
    
    private final String field;
    private final Comparator<Song> comparator;
    
    private SongComparator(final String field, final Comparator<Song> comparator) {
        this.field = field;
        this.comparator = comparator;
    }
    
    public String getField() {
        return field;
    }

    @Override
    public int compare(Song o1, Song o2) {
        return comparator.compare(o1, o2);
    }
    
    public static Comparator<Song> reversed(final Comparator<Song> other) {
        return new Comparator<Song>() {
            public int compare(Song o1, Song o2) {
                return -1 * other.compare(o1, o2);
            }
        };
    }

}
