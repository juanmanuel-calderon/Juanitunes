package com.jmc.juanitunes.organizer.api.library;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;

public interface AlbumArtist {
	
	public AlbumArtist addAlbum(Album album);
	public AlbumArtist removeAlbum(Album album);
	
	public AlbumArtist sort(Comparator<Album>... criteria);
	public AlbumArtist merge(AlbumArtist other);
	public Optional<AlbumArtist> match(String string);
	
	public Set<Album> getAlbums();
	public Set<Song> getAllSongs();
	public String getName();
	public int getDurationInSeconds();
	public double getSizeInMegaBytes();

}
