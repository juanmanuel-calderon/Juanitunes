package com.jmc.juanitunes.organizer.api.library;

import java.util.List;
import java.util.Optional;

public interface Song {
    
    public Optional<Song> match(String string);
    
    public String getFilename();
    public String getTitle();
    public int    getCDNumber();
    public int    getTrackNumber();
    public String getGenre();
    public int    getRating();
    public int    getDurationInSeconds();
    public double getSizeInMegaBytes();
    public int    getBitrate();
    public String getExtension();
    public String getYear();
    public String getCatalogNumber();
    
    public List<String> getAlbumArtists();
    public String       getAlbum();
    public List<String> getArtists();
    
}
