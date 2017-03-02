package com.jmc.juanitunes.organizer.impl.library;

import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.jmc.juanitunes.organizer.Utils;
import com.jmc.juanitunes.organizer.api.library.Album;
import com.jmc.juanitunes.organizer.api.library.AlbumArtist;
import com.jmc.juanitunes.organizer.api.library.Song;

public class SimpleAlbumArtist implements AlbumArtist, Comparable<AlbumArtist> {

    private final String    name;
    private int             duration;
    private double          size;

    private Set<Album> albums = new TreeSet<Album>();

    public SimpleAlbumArtist(String name,
                             Set<Album> albums) {
        this.name = name;
        this.duration = 0;
        this.size = 0;
        albums.stream().forEach(this::addAlbum);
    }   
    
    public SimpleAlbumArtist(AlbumArtist albumArtist, boolean copyAlbums) {
        this(albumArtist.getName(),
             copyAlbums ? albumArtist.getAlbums() : new TreeSet<Album>());
    }
    
    public SimpleAlbumArtist(AlbumArtist albumArtist) {
        this (albumArtist, true);
    }

    public AlbumArtist addAlbum(Album album) {
        
        if(albums.contains(album)) return this;
        
        albums.add(album);
        size = size + album.getSizeInMegaBytes();
        duration = duration + album.getDurationInSeconds();
        return this;
    }

    public AlbumArtist removeAlbum(Album album) {
        
        if(!(albums.remove(album))) return this;
        size = size - album.getSizeInMegaBytes();
        duration = duration - album.getDurationInSeconds();
        return this;
    }

    public AlbumArtist sort(Comparator<Album>... criteria) {
        
        Set<Album> sortedAlbums = new TreeSet<Album>(Utils.generateFinalComparator(criteria));
        sortedAlbums.addAll(albums);
        albums = sortedAlbums;
        return this;
    }
    
    public AlbumArtist merge(AlbumArtist other) {
        other.getAlbums().stream()
                         .filter(a -> !albums.contains(a))
                         .forEach(a -> addAlbum(a));
        other.getAlbums().stream()
                         .filter(a -> albums.contains(a))
                         .forEach(a -> lookup(a).merge(a));
                         
        return this;
    }
    
    private Album lookup(Album album) {
        return albums.stream()
                     .filter(a -> a.equals(album))
                     .findFirst()
                     .get();
    }

    public Optional<AlbumArtist> match(String string) {

        if(name.toLowerCase().contains(string.toLowerCase())) return Optional.of(this);

        AlbumArtist filteredAlbumArtist = new SimpleAlbumArtist(this, false);
        albums.stream()
              .map(a -> a.match(string.toLowerCase()))
              .filter(Optional::isPresent)
              .map(Optional::get)
              .forEach(filteredAlbumArtist::addAlbum);

        return filteredAlbumArtist.getAlbums().isEmpty() ? Optional.empty()
                                                         : Optional.of(filteredAlbumArtist);
    }

    public String getName() {
        return name;
    }

    public int getDurationInSeconds() {
        return duration;
    }

    public double getSizeInMegaBytes() {
        return size;
    }

    public Set<Album> getAlbums() {
        return Collections.unmodifiableSet(albums); 
    }
    
    public Set<Song> getAllSongs() {
        Set<Song> allSongs = albums.stream()
                                   .map(Album::getSongs)
                                   .flatMap(Set::stream)
                                   .collect(Collectors.toSet());
        
        return Collections.unmodifiableSet(allSongs);
    }
    
    @Override
    public String toString() {
        StringBuilder albumArtistStr = new StringBuilder();
        
        albumArtistStr = albumArtistStr.append("Album Artist: " + name).append(System.lineSeparator());
        albumArtistStr = albumArtistStr.append("Albums:")              .append(System.lineSeparator());
        albumArtistStr.append(
                     albums.stream()
                           .map(Album::toString)
                           .collect(Collectors.joining(System.lineSeparator()))
                 );

        return albumArtistStr.toString();
    }
    
    @Override
    public boolean equals(Object other) {
        if(other == null) return false;
        if(!(other instanceof AlbumArtist)) return false;
        
        AlbumArtist otherAlbumArtist = (AlbumArtist) other;
        if(!otherAlbumArtist.getName().equals(name)) return false;
        
        return true;            
    }

    @Override
    public int compareTo(AlbumArtist other) {
        return name.compareTo(other.getName());
    }

}
