package com.jmc.juanitunes.organizer.impl.builder;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.jmc.juanitunes.organizer.api.builder.ComponentBuilder;
import com.jmc.juanitunes.organizer.api.library.Album;
import com.jmc.juanitunes.organizer.api.library.Song;
import com.jmc.juanitunes.organizer.impl.library.SimpleAlbum;

public class AlbumBuilder implements ComponentBuilder<Album> {

    public Set<Album> createNew(Set<Song> songs) {
        Set<Album> albums = new TreeSet<Album>();
        
        Map<String, Set<Song>> groupedSongs =
                songs.stream()
                     .collect(Collectors.groupingBy(Song::getCatalogNumber,
                                                     Collectors.mapping(Function.identity(), Collectors.toSet())));
        
        albums = groupedSongs.entrySet().stream()
                                         .map(e -> createAlbum(e))
                                         .collect(Collectors.toSet());
        
        return albums;
    }
    
    private Album createAlbum(Entry<String, Set<Song>> songsInAlbum) {    
        Song representativeSong = songsInAlbum.getValue().iterator().next();        
        String name = representativeSong.getAlbum();
        String year = representativeSong.getYear();
        String catalogNumber = songsInAlbum.getKey();
        
        return new SimpleAlbum(name, year, catalogNumber, songsInAlbum.getValue());
    }

}
