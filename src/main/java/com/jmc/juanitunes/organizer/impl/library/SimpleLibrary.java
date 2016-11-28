package com.jmc.juanitunes.organizer.impl.library;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.jmc.juanitunes.organizer.Utils;
import com.jmc.juanitunes.organizer.api.library.Album;
import com.jmc.juanitunes.organizer.api.library.AlbumArtist;
import com.jmc.juanitunes.organizer.api.library.Library;
import com.jmc.juanitunes.organizer.api.library.Song;

public class SimpleLibrary implements Library {

	private final String 	name;
	private int			duration;
	private double			size;
	
	private Set<AlbumArtist> albumArtists = new TreeSet<AlbumArtist>();
	
	public SimpleLibrary(String name) {
		this.name = name;
	}
	
	public Library addAlbumArtist(AlbumArtist albumArtist) {
		
		if(albumArtists.contains(albumArtist)) return this;
		
		albumArtists.add(albumArtist);
		size = size + albumArtist.getSizeInMegaBytes();
		duration = duration + albumArtist.getDurationInSeconds();
		return this;
	}
	
	public Library removeAlbumArtist(AlbumArtist albumArtist) {
		
		if(!(albumArtists.remove(albumArtist))) return this;
		size = size - albumArtist.getSizeInMegaBytes();
		duration = duration - albumArtist.getDurationInSeconds();
		return this;
	}
	
	public Library sort(Comparator<AlbumArtist>... criteria) {
		
		Set<AlbumArtist> sortedAlbumArtists = new TreeSet<AlbumArtist>(Utils.generateFinalComparator(criteria));
		sortedAlbumArtists.addAll(albumArtists);
		albumArtists = sortedAlbumArtists;
		return this;
	}
	
	public Library merge(Library other) {
		other.getAlbumArtists().stream()
						 	   .filter(aa -> !albumArtists.contains(aa))
						 	   .forEach(aa -> addAlbumArtist(aa));
		other.getAlbumArtists().stream()
						 	   .filter(aa -> !albumArtists.contains(aa))
						 	   .forEach(aa -> lookup(aa).merge(aa));
		 
		return this;
	}
	
	private AlbumArtist lookup(AlbumArtist albumArtist) {
		return albumArtists.stream()
					  		.filter(aa -> aa.equals(albumArtist))
					  		.findFirst()
					  		.get();
	}
	
	public String getName() {
		return name;
	}

	public int getDurationInSeconds() {
		return duration;
	}

	public double getSizeInMegaBytes() {
		return size;
	}

	public Set<AlbumArtist> getAlbumArtists() {
		return Collections.unmodifiableSet(albumArtists); 
	}
	
	public Set<Album> getAllAlbums() {
		Set<Album> allAlbums = 
				albumArtists.stream()
							.map(AlbumArtist::getAlbums)
							.flatMap(Set::stream)
							.collect(Collectors.toSet());
		
		return Collections.unmodifiableSet(allAlbums);
	}
	
	public Set<Song> getAllSongs() {
		Set<Song> allSongs = 
				albumArtists.stream()
					  		.map(AlbumArtist::getAllSongs)
					  		.flatMap(Set::stream)
					  		.collect(Collectors.toSet());
		
		return Collections.unmodifiableSet(allSongs);
	}
	
	@Override
	public String toString() {
		StringBuilder albumArtistStr = new StringBuilder();
		
		albumArtistStr = albumArtistStr.append("Library: " + name).append(System.lineSeparator());
		albumArtistStr = albumArtistStr.append("Albums Artists:").append(System.lineSeparator());
		albumArtistStr.append(
				 	albumArtists.stream()
						 		.map(AlbumArtist::toString)
						 		.collect(Collectors.joining(System.lineSeparator()))
				 );

		return albumArtistStr.toString();
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null) return false;
		if(!(other instanceof Library)) return false;
		
		Library otherLibrary = (Library) other;
		if(!otherLibrary.getName().equals(name)) return false;
		
		return true;			
	}
	
	
}
