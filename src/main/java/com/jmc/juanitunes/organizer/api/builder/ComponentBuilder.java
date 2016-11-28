package com.jmc.juanitunes.organizer.api.builder;

import java.util.Set;

import com.jmc.juanitunes.organizer.api.library.Song;

public interface ComponentBuilder<T> {
    
    public Set<T> createNew(Set<Song> songs);

}
