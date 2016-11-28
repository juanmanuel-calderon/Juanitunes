package com.jmc.juanitunes.organizer.api.sort;

import java.util.Comparator;

import com.jmc.juanitunes.organizer.api.library.AlbumArtist;

public enum AlbumArtistComparator implements Comparator<AlbumArtist> {
	
	BY_NAME(		"NAME", 	(a, b) -> a.getName().compareTo(b.getName())),
	BY_DURATION(	"DURATION", (a, b) -> Integer.compare(a.getDurationInSeconds(), b.getDurationInSeconds())),
	BY_SIZE(		"SIZE", 	(a, b) -> Double.compare(a.getSizeInMegaBytes(), b.getSizeInMegaBytes()));
	
	private final String field;
	private final Comparator<AlbumArtist> comparator;
	
	private AlbumArtistComparator(final String field, final Comparator<AlbumArtist> comparator) {
		this.field = field;
		this.comparator = comparator;
	}
	
	public String getField() {
		return field;
	}

	@Override
	public int compare(AlbumArtist o1, AlbumArtist o2) {
		return comparator.compare(o1, o2);
	}
	
	public static Comparator<AlbumArtist> reversed(final Comparator<AlbumArtist> other) {
        return new Comparator<AlbumArtist>() {
            public int compare(AlbumArtist o1, AlbumArtist o2) {
                return -1 * other.compare(o1, o2);
            }
        };
    }

}
