package com.jmc.juanitunes.organizer.api.serial;

import com.jmc.juanitunes.organizer.api.library.Song;

public interface SongSerializer {
    
    public String serialize(Song source);
    public Song deserialize(String source);
}
