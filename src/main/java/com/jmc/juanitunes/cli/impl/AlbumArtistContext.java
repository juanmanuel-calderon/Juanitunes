package com.jmc.juanitunes.cli.impl;

import java.util.stream.Collectors;

import com.jmc.juanitunes.cli.api.Context;
import com.jmc.juanitunes.organizer.Utils;
import com.jmc.juanitunes.organizer.api.library.Album;
import com.jmc.juanitunes.organizer.api.library.AlbumArtist;
import com.jmc.juanitunes.organizer.api.sort.AlbumComparator;

public class AlbumArtistContext implements Context {

	private AlbumArtist albumArtist;
	private Album[] albums;
	private LibraryContext parentContext;
	
	public AlbumArtistContext(AlbumArtist albumArtist, LibraryContext parentContext) {
		this.albumArtist = albumArtist;
		this.parentContext = parentContext;
		albums = albumArtist.sort(AlbumComparator.BY_YEAR)
				   		    .getAlbums()
							.stream()
							.toArray(Album[]::new);
	}
	
	@Override
	public Context open(int child) {
		if(child < 1) 
			throw new IllegalArgumentException("Choose a correct album number");
		if(child > albums.length) 
			throw new IllegalArgumentException("Choose a correct album number");
		
		Album a = albums[child - 1];
		return new AlbumContext(a, this);
	}

	@Override
	public Context close() {
		return parentContext;
	}

	@Override
	public String pwd() {
		return parentContext.pwd() + "/" + albumArtist.getName();
	}

	@Override
	public String list() {
		String res = "Albums:" + System.lineSeparator();
		for(int i = 0; i < albums.length; i++) {
			Album a = albums[i];
			res += String.format("%02d", (i+1)) + ". [" + a.getYear() + "] " + a.getName() + System.lineSeparator();
		}
		return res;
	}

	@Override
	public String detail(int child) {
		if(child < 1) 
			throw new IllegalArgumentException("Choose a correct album number");
		if(child > albums.length) 
			throw new IllegalArgumentException("Choose a correct album number");
		
		Album a = albums[child - 1];
		String res = "Details for " + a.getName() + System.lineSeparator();
		res += "Year: " + a.getYear() + System.lineSeparator();
		res += "Catalog number: " + a.getCatalogNumber() + System.lineSeparator();
		res += "Duration: " + Utils.secondsToMinutes(a.getDurationInSeconds()) + System.lineSeparator();
		res += "Size: " + a.getSizeInMegaBytes() + " MB" + System.lineSeparator();
		res += "Tracklist:\n";
		res += a.getSongs().stream()
					  	   .map(s -> "\t" + String.format("%02d", s.getCDNumber()) + "-" + 
					  			   			String.format("%02d", s.getTrackNumber()) + 
					  			   			". " + s.getTitle())
					  	   .collect(Collectors.joining(System.lineSeparator()));
		return res;
	}

}
