package com.jmc.juanitunes.organizer.api.sort;

import java.util.Comparator;

import com.jmc.juanitunes.organizer.api.library.Album;

public enum AlbumComparator implements Comparator<Album> {
    
    BY_NAME(            "NAME",     (a, b) -> a.getName().compareTo(b.getName())),
    BY_DURATION(        "DURATION", (a, b) -> Integer.compare(a.getDurationInSeconds(), b.getDurationInSeconds())),
    BY_SIZE(            "SIZE",     (a, b) -> Double.compare(a.getSizeInMegaBytes(), b.getSizeInMegaBytes())),
    BY_YEAR(            "YEAR",     (a, b) -> a.getYear().compareTo(b.getYear())),
    BY_CATALOGNUMBER(  "CATALOG_NUMBER", (a, b) -> a.getCatalogNumber().compareTo(b.getCatalogNumber()));
    
    private final String field;
    private final Comparator<Album> comparator;
    
    private AlbumComparator(final String field, final Comparator<Album> comparator) {
        this.field = field;
        this.comparator = comparator;
    }
    
    public String getField() {
        return field;
    }

    @Override
    public int compare(Album o1, Album o2) {
        return comparator.compare(o1, o2);
    }
    
    public static Comparator<Album> reversed(final Comparator<Album> other) {
        return new Comparator<Album>() {
            public int compare(Album o1, Album o2) {
                return -1 * other.compare(o1, o2);
            }
        };
    }

}
