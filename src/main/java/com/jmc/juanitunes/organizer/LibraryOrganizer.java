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
	
	public LibraryOrganizer(String libraryName) {
		currentLibrary = new SimpleLibrary(libraryName);
	}

	public void export(String filename) throws IOException {
		Files.write(Paths.get(filename),
				    (Iterable<String>)currentLibrary.getAllSongs().stream()
				    											  .map(s -> s.getFilename())::iterator);
	}
	
	public void addToCurrentLibrary(List<String> sources) throws RuntimeException {
		Library temp = createLibraryFromSource(sources);
		currentLibrary.merge(temp);
	}
	
	private Library createLibraryFromSource(List<String> sources) throws RuntimeException {
		
		SongBuilder songBuilder = new GeneralSongBuilder();
		
		Set<Song> songs = sources.stream()
								  .map(s -> safeGetAllFilenames(s))
								  .flatMap(List::stream)
								  .map(f -> songBuilder.createNew(f))
								  .filter(s -> s.isPresent())
								  .map(s -> s.get())
								  .collect(Collectors.toSet());
		
		Set<AlbumArtist> albumArtists = new AlbumArtistBuilder().createNew(songs);
		Library temp = new SimpleLibrary("temp");
		albumArtists.forEach(aa -> temp.addAlbumArtist(aa));
		
		return temp;
	}
	
	private List<String> safeGetAllFilenames(String source) throws RuntimeException {
		try {
			return getAllFilenames(source);
		} catch(IOException e) {
			throw new RuntimeException(e.getCause());
		}
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
