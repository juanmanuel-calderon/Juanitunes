package com.jmc.juanitunes.organizer.impl.builder;

import java.io.File;
import java.util.Optional;

import com.jmc.juanitunes.organizer.Utils;
import com.jmc.juanitunes.organizer.api.builder.SongBuilder;
import com.jmc.juanitunes.organizer.api.library.Song;

public class GeneralSongBuilder implements SongBuilder {
    
    public Optional<Song> createNew(String source) {
        
        String ext = Utils.getFileExtension(new File(source)).toLowerCase();
        switch(ext) {
            case "flac"     :     return new FlacSongBuilder().createNew(source);
            case "mp3"		:     return new Mp3SongBuilder().createNew(source);
            default         :     return Optional.empty(); 
        }
    }
}
