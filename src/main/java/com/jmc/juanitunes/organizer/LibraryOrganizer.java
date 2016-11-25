package com.jmc.juanitunes.organizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.jmc.juanitunes.organizer.api.builder.SongBuilder;
import com.jmc.juanitunes.organizer.api.library.AlbumArtist;
import com.jmc.juanitunes.organizer.api.library.Library;
import com.jmc.juanitunes.organizer.api.library.Song;
import com.jmc.juanitunes.organizer.impl.builder.AlbumArtistBuilder;
import com.jmc.juanitunes.organizer.impl.builder.GeneralSongBuilder;
import com.jmc.juanitunes.organizer.impl.library.SimpleLibrary;

public class LibraryOrganizer {

	private Library currentLibrary;

	public void export(String filename) throws IOException {
		Files.write(Paths.get(filename),
				    (Iterable<String>)currentLibrary.getAllSongs().stream()
				    											  .map(s -> s.getFilename())::iterator);
	}
	
	public void addToCurrentLibrary(String source) throws IOException {
		Library temp = createLibraryFromDirectory(source);
		currentLibrary.merge(temp);
	}
	
	private Library createLibraryFromDirectory(String source) throws IOException {
		
		SongBuilder songBuilder = new GeneralSongBuilder();
		
		Set<Song> songs = getAllFilenames(source).stream()
												  .map(f -> songBuilder.createNew(f))
												  .filter(s -> s.isPresent())
												  .map(s -> s.get())
												  .collect(Collectors.toSet());
		
		Set<AlbumArtist> albumArtists = new AlbumArtistBuilder().createNew(songs);
		Library temp = new SimpleLibrary("temp");
		albumArtists.forEach(aa -> temp.addAlbumArtist(aa));
		
		return temp;
	}

	private List<String> getAllFilenames(String source) throws IOException {
		return Files.walk(Paths.get(source))
					 .filter(Files::isRegularFile)
					 .map(p -> p.toFile().getAbsolutePath())
					 .collect(Collectors.toList());
	}

	public Library getCurrentLibrary() {
		return currentLibrary;
	}


}
