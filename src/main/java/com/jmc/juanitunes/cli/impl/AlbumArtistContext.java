package com.jmc.juanitunes.cli.impl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.jmc.juanitunes.cli.api.Context;
import com.jmc.juanitunes.organizer.Utils;
import com.jmc.juanitunes.organizer.api.library.Album;
import com.jmc.juanitunes.organizer.api.library.AlbumArtist;
import com.jmc.juanitunes.organizer.api.library.Song;
import com.jmc.juanitunes.organizer.api.sort.AlbumComparator;
import com.jmc.juanitunes.organizer.api.sort.SongComparator;
import com.jmc.juanitunes.organizer.impl.library.MultiCDAlbum;

public class AlbumArtistContext implements Context {

    private AlbumArtist albumArtist;
    private Album[] albums;
    private LibraryContext parentContext;
    
    public AlbumArtistContext(AlbumArtist albumArtist, LibraryContext parentContext) {
        this.albumArtist = albumArtist;
        this.parentContext = parentContext;

        albums = albumArtist.sort(AlbumComparator.BY_YEAR)
                            .getAlbums()
                            .stream()
                            .toArray(Album[]::new);
    }
    
    @Override
    public Context open(int child) {
        if(child < 1) 
            throw new IllegalArgumentException("Choose a correct album number");
        if(child > albums.length) 
            throw new IllegalArgumentException("Choose a correct album number");
        
        Album a = albums[child - 1];
        return new AlbumContext(a, this);
    }

    @Override
    public Context close() {
        return parentContext;
    }

    @Override
    public String pwd() {
        return parentContext.pwd() + "/" + albumArtist.getName();
    }

    @Override
    public String list() {
        String res = "Albums:" + System.lineSeparator();
        for(int i = 0; i < albums.length; i++) {
            Album a = albums[i];
            res += String.format("%02d", (i+1)) + ". [" + a.getYear() + "] " + a.getName() + System.lineSeparator();
        }
        return res;
    }

    @Override
    public String detail(int child) {
        if(child < 1) 
            throw new IllegalArgumentException("Choose a correct album number");
        if(child > albums.length) 
            throw new IllegalArgumentException("Choose a correct album number");
        
        Album a = albums[child - 1];
        String res = "Details for " + a.getName() + System.lineSeparator();
        res += "Year: " + a.getYear() + System.lineSeparator();
        res += "Catalog number: " + a.getCatalogNumber() + System.lineSeparator();
        res += "Duration: " + Utils.secondsToMinutes(a.getDurationInSeconds()) + System.lineSeparator();
        res += "Size: " + a.getSizeInMegaBytes() + " MB" + System.lineSeparator();
        res += "Tracklist:\n";
        res += a.getSongs().stream().sorted()
                           .map(s -> "\t" + String.format("%02d", s.getCDNumber()) + "-" + 
                                            String.format("%02d", s.getTrackNumber()) + 
                                            ". " + s.getTitle())
                           .collect(Collectors.joining(System.lineSeparator()));
        return res;
    }
    
    @Override
    public String find(String string) {
        Optional<AlbumArtist> matchResult = albumArtist.match(string);
        if(matchResult.isPresent()) {
            AlbumArtist found = matchResult.get();
            return
              found.getAlbums()
                   .stream()
                   .map((Album a) ->
                     a.getSongs()
                      .stream()
                      .map((Song s) -> a.getName() + "/" + String.format("%02d", s.getCDNumber()) + "-" + 
                                                           String.format("%02d", s.getTrackNumber()) + ". " + s.getTitle())
                      .collect(Collectors.joining(System.lineSeparator()))
                   )
                   .collect(Collectors.joining(System.lineSeparator()));
        } else {
            return "No match found for " + string;
        }
    }
    
    public void merge(int album1, int album2, String name) {
        Album a1 = albums[album1 - 1];
        Album a2 = albums[album2 - 1];
        
        albumArtist.removeAlbum(a1).removeAlbum(a2);
        
        Set<Album> mcdSet = new HashSet<Album>();
        mcdSet.add(a1); mcdSet.add(a2);
        Album mcd = new MultiCDAlbum(name, mcdSet)
                .sortInternal(AlbumComparator.BY_YEAR)
                .sort(SongComparator.BY_CDNUMBER, SongComparator.BY_TRACKNUMBER);
        albumArtist.addAlbum(mcd);
        
        albums = albumArtist.sort(AlbumComparator.BY_YEAR)
                .getAlbums()
                .stream()
                .toArray(Album[]::new);
    }
    
    @Override
    public void help() {
        Context.super.help();
        System.out.println("\tmerge <num> <num> <string>: merges two albums in one");
    }

}
