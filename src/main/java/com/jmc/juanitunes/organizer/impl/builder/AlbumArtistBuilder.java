package com.jmc.juanitunes.organizer.impl.builder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.jmc.juanitunes.organizer.api.builder.ComponentBuilder;
import com.jmc.juanitunes.organizer.api.library.Album;
import com.jmc.juanitunes.organizer.api.library.AlbumArtist;
import com.jmc.juanitunes.organizer.api.library.Song;
import com.jmc.juanitunes.organizer.impl.library.SimpleAlbumArtist;

public class AlbumArtistBuilder implements ComponentBuilder<AlbumArtist> {

    public Set<AlbumArtist> createNew(Set<Song> songs) {
        Set<AlbumArtist> albumArtists = new TreeSet<AlbumArtist>();
        Set<Album> allAlbums = new AlbumBuilder().createNew(songs);
        
        Map<List<String>, Set<Song>> groupedSongs =
                songs.stream()
                     .collect(Collectors.groupingBy(Song::getAlbumArtists,
                                                     Collectors.mapping(Function.identity(), Collectors.toSet())));
        
        Map<String, Set<Song>> unfoldedAlbumArtists = new HashMap<String, Set<Song>>();
        groupedSongs.entrySet().stream()
                               .map(e -> unfoldEntry(e))
                               .forEach(m -> unfoldedAlbumArtists.putAll(m));
        
        albumArtists = unfoldedAlbumArtists.entrySet().stream()
                                                      .map(aa -> createAlbumArtist(aa, allAlbums))
                                                      .collect(Collectors.toSet());

        return albumArtists;
    }
    
    private Map<String, Set<Song>> unfoldEntry(Entry<List<String>, Set<Song>> entry) {
        Map<String, Set<Song>> target = new HashMap<String, Set<Song>>();
        
        List<String> newKeys = entry.getKey();
        newKeys.forEach(k -> target.put(k, entry.getValue()));
        
        return target;
    }
    
    private AlbumArtist createAlbumArtist(Entry<String, Set<Song>> songsByAlbumArtist, Set<Album> albums) {
        Set<Album> filteredAlbums = albums.stream()
                                          .filter(a -> a.getSongs().iterator().next().getAlbumArtists().contains(songsByAlbumArtist.getKey()))
                                          .collect(Collectors.toSet());
        
        return new SimpleAlbumArtist(songsByAlbumArtist.getKey(), filteredAlbums);            
    }


}
