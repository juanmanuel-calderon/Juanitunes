package com.jmc.juanitunes.organizer.impl.library;

import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.jmc.juanitunes.organizer.Utils;
import com.jmc.juanitunes.organizer.api.library.Album;
import com.jmc.juanitunes.organizer.api.library.Song;

public class SimpleAlbum implements Album, Comparable<Album> {
    
    private final String    name;
    private final String    year;
    private final String    catalogNumber;
    private int             duration;
    private double          size;
    
    private Set<Song> songs = new TreeSet<Song>();
    
    public SimpleAlbum(String name,
                       String year,
                       String catalogNumber,
                       Set<Song> songs) {
        
        this.name = name;
        this.year = year;
        this.catalogNumber = catalogNumber;
        this.duration = 0;
        this.size = 0;
        songs.stream().forEach(this::addSong);
    }
    
    public SimpleAlbum(Album album, boolean copySongs) {    
        this(album.getName(),
             album.getYear(),
             album.getCatalogNumber(),
             copySongs ? album.getSongs() : new TreeSet<Song>());
    }
    
    public SimpleAlbum(Album album) {
        this(album, true);
    }

    public Album addSong(Song song) {
        
        if(songs.contains(song)) return this;
        
        songs.add(song);
        size = size + song.getSizeInMegaBytes();
        duration = duration + song.getDurationInSeconds();
        return this;
    }

    public Album removeSong(Song song) {
        
        if(!(songs.remove(song))) return this;    
        size = size - song.getSizeInMegaBytes();
        duration = duration - song.getDurationInSeconds();    
        return this;
    }

    public Album sort(Comparator<Song>... criteria) {
        
        Set<Song> sortedSongs = new TreeSet<Song>(Utils.generateFinalComparator(criteria));
        sortedSongs.addAll(songs);
        songs = sortedSongs;
        return this;
    }
    
    public Album merge(Album other) {
        other.getSongs().forEach(s -> addSong(s));
        return this;
    }

    public Optional<Album> match(String string) {
        
        if(name.toLowerCase().contains(string.toLowerCase())) return Optional.of(this);
        if(year.toLowerCase().contains(string.toLowerCase())) return Optional.of(this);
        if(catalogNumber.toLowerCase().contains(string.toLowerCase())) return Optional.of(this);
        
        Album filteredAlbum = new SimpleAlbum(this, false);
        songs.stream()
             .map(s -> s.match(string.toLowerCase()))
             .filter(Optional::isPresent)
             .map(Optional::get)
             .forEach(filteredAlbum::addSong);

        return filteredAlbum.getSongs().isEmpty() ? Optional.empty()
                                                  : Optional.of(filteredAlbum);
    }

    public String getName() {
        return name;
    }

    public String getYear() {
        return year;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }
    
    public int getDurationInSeconds() {
        return duration;
    }

    public double getSizeInMegaBytes() {
        return size;
    }
    
    public Set<Song> getSongs() {
        return Collections.unmodifiableSet(songs); 
    }
    
    @Override
    public String toString() {
        StringBuilder albumStr = new StringBuilder();
        
        albumStr = albumStr.append("Album Name: " + name)   .append(System.lineSeparator());
        albumStr = albumStr.append("Year: " + year)         .append(System.lineSeparator());
        albumStr = albumStr.append("Code: " + catalogNumber).append(System.lineSeparator());
        albumStr = albumStr.append("Songs:")                .append(System.lineSeparator());
        albumStr.append(
                     songs.stream()
                          .map(Song::toString)
                          .collect(Collectors.joining(System.lineSeparator()))
                 );

        return albumStr.toString();
    }
    
    @Override
    public boolean equals(Object other) {
        if(other == null) return false;
        if(!(other instanceof Album)) return false;
        
        Album otherAlbum = (Album) other;
        if(!otherAlbum.getCatalogNumber().equals(catalogNumber)) return false;
        
        return true;            
    }

    public int compareTo(Album other) {
        return catalogNumber.compareTo(other.getCatalogNumber());
    }
}
