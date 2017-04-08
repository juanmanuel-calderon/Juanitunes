package com.jmc.juanitunes.organizer.api.builder;

import java.util.List;

import com.jmc.juanitunes.organizer.api.library.Library;

public interface LibraryImporter {
	
	public Library importSongs(List<String> sources);

}
