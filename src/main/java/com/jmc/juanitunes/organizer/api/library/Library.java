package com.jmc.juanitunes.organizer.api.library;

import java.util.Comparator;
import java.util.Set;

public interface Library {
	
	public Library addAlbumArtist(AlbumArtist albumArtist);
	public Library removeAlbumArtist(AlbumArtist albumArtist);	
	public Library sort(Comparator<AlbumArtist>... criterion);
	public Library merge(Library other);
	
	public Set<AlbumArtist> getAlbumArtists();
	public Set<Album> getAllAlbums();
	public Set<Song> getAllSongs();
	public String getName();
	public int getDurationInSeconds();
	public double getSizeInMegaBytes();

}
