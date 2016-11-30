package com.jmc.juanitunes.organizer.api.builder;

import java.util.Optional;

import com.jmc.juanitunes.organizer.api.library.Song;

public interface SongBuilder {
    
    public Optional<Song> createNew(String source);

}