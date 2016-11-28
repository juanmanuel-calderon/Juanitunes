package com.jmc.juanitunes.organizer.api.library;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;

public interface Album {
    
    public Album addSong(Song song);
    public Album removeSong(Song song);
    
    public Album sort(Comparator<Song>... criteria);
    public Album merge(Album other);
    public Optional<Album> match(String string);
    
    public Set<Song> getSongs();
    public String getName();
    public String getYear();
    public String getCatalogNumber();
    public int getDurationInSeconds();
    public double getSizeInMegaBytes();

}
