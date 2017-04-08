package com.jmc.juanitunes.organizer.impl.builder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.jmc.juanitunes.organizer.api.builder.LibraryImporter;
import com.jmc.juanitunes.organizer.api.builder.SongBuilder;
import com.jmc.juanitunes.organizer.api.library.AlbumArtist;
import com.jmc.juanitunes.organizer.api.library.Library;
import com.jmc.juanitunes.organizer.api.library.Song;
import com.jmc.juanitunes.organizer.impl.library.SimpleLibrary;

public class LinearLibraryImporter implements LibraryImporter {

	public Library importSongs(List<String> sources) {
		return createLibraryFromSource(sources);
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
			String message = "Message: " + e.getMessage() + System.lineSeparator();
			message += "Cause: " + e.getCause();
			throw new RuntimeException(message);
		}
	}

	private List<String> getAllFilenames(String source) throws IOException {

		return Files.walk(Paths.get(source))
				.filter(Files::isRegularFile)
				.map(p -> p.toFile().getAbsolutePath())
				.collect(Collectors.toList());
	}

}
