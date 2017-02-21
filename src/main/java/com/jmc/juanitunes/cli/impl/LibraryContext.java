package com.jmc.juanitunes.cli.impl;

import java.util.stream.Collectors;

import com.jmc.juanitunes.cli.api.Context;
import com.jmc.juanitunes.organizer.Utils;
import com.jmc.juanitunes.organizer.api.library.AlbumArtist;
import com.jmc.juanitunes.organizer.api.library.Library;
import com.jmc.juanitunes.organizer.api.sort.AlbumArtistComparator;

public class LibraryContext implements Context {
	
	private Library library;
	private AlbumArtist[] albumArtists;

	public LibraryContext(Library library) {
		this.library = library;
		albumArtists = library.sort(AlbumArtistComparator.BY_NAME)
							  .getAlbumArtists()
							  .stream()
							  .toArray(AlbumArtist[]::new);
	}	
	
	@Override
	public Context open(int child) {
		if(child < 1) 
			throw new IllegalArgumentException("Choose a correct album artist number");
		if(child > albumArtists.length) 
			throw new IllegalArgumentException("Choose a correct album artist number");
		
		AlbumArtist aa = albumArtists[child - 1];
		return new AlbumArtistContext(aa, this);
	}

	@Override
	public Context close() {
		throw new UnsupportedOperationException("Library is at root level");
	}

	@Override
	public String pwd() {
		return "/" + library.getName();
	}

	@Override
	public String list() {
		String res = "Album artists:" + System.lineSeparator();
		for(int i = 0; i < albumArtists.length; i++) {
			res += String.format("%02d", (i+1)) + ". " + albumArtists[i].getName() + System.lineSeparator();
		}
		return res;
	}

	@Override
	public String detail(int child) {
		if(child < 1) 
			throw new IllegalArgumentException("Choose a correct album artist number");
		if(child > albumArtists.length) 
			throw new IllegalArgumentException("Choose a correct album artist number");
		
		AlbumArtist aa = albumArtists[child - 1];
		String res = "Details for " + aa.getName() + System.lineSeparator();
		res += "Duration: " + Utils.secondsToMinutes(aa.getDurationInSeconds()) + System.lineSeparator();
		res += "Size: " + aa.getSizeInMegaBytes() + " MB" + System.lineSeparator();
		res += "Albums:\n";
		res += aa.getAlbums().stream()
					  		 .map(a -> "\t" + a.getName())
					  		 .collect(Collectors.joining(System.lineSeparator()));
		return res;
	}
	
	@Override
	public void help() {
		Context.super.help();
		System.out.println("close operation is not supported by this node");
	}
}
