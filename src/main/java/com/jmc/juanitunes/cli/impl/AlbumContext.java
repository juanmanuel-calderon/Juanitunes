package com.jmc.juanitunes.cli.impl;

import java.util.Optional;
import java.util.stream.Collectors;

import com.jmc.juanitunes.cli.api.Context;
import com.jmc.juanitunes.organizer.Utils;
import com.jmc.juanitunes.organizer.api.library.Album;
import com.jmc.juanitunes.organizer.api.library.Song;
import com.jmc.juanitunes.organizer.api.sort.SongComparator;

public class AlbumContext implements Context {

    private Album album;
    private Song[] songs;
    private AlbumArtistContext parentContext;
    
    public AlbumContext(Album album, AlbumArtistContext parentContext) {
        this.album = album;
        this.parentContext = parentContext;
        songs = album.sort(SongComparator.BY_TRACKNUMBER)
                     .getSongs()
                     .stream()
                     .toArray(Song[]::new);
    }
    
    @Override
    public Context open(int child) {
        throw new UnsupportedOperationException("Can't go further");
    }

    @Override
    public Context close() {
        return parentContext;
    }

    @Override
    public String pwd() {
        return parentContext.pwd() + "/" + album.getName();
    }

    @Override
    public String list() {
        String res = "Tracklist:" + System.lineSeparator();
        for(int i = 0; i < songs.length; i++) {
            Song s = songs[i];
            res += String.format("%02d", (i+1)) + ". " + String.format("%02d", s.getCDNumber()) + "-" + 
                                                         String.format("%02d", s.getTrackNumber()) + 
                                                         ". " + s.getTitle() + System.lineSeparator();
        }
        return res;
    }

    @Override
    public String detail(int child) {
        if(child < 1) 
            throw new IllegalArgumentException("Choose a correct track number");
        if(child > songs.length) 
            throw new IllegalArgumentException("Choose a correct track number");
        
        Song s = songs[child - 1];
        String res = "Details for " + s.getTitle() + System.lineSeparator();
        res += "Artists: " + s.getArtists().stream().collect(Collectors.joining("; ")) + System.lineSeparator();
        res += "CD#-Track#: " + String.format("%02d", s.getCDNumber()) + "-" + String.format("%02d", s.getTrackNumber()) + System.lineSeparator();
        res += "Genre: " + s.getGenre() + System.lineSeparator();
        res += "Rating: " + s.getRating() + System.lineSeparator();
        res += "Duration: " + Utils.secondsToMinutes(s.getDurationInSeconds()) + System.lineSeparator();
        res += "Size: " + s.getSizeInMegaBytes() + " MB" + System.lineSeparator();
        res += "Bitrate: " + s.getBitrate() + System.lineSeparator();
        res += "Extension: " + s.getExtension();
        return res;
    }
    
    @Override
    public String find(String string) {
        Optional<Album> matchResult = album.match(string);
        if(matchResult.isPresent()) {
            Album found = matchResult.get();
            return
                found.getSongs()
                     .stream()
                     .map((Song s) -> String.format("%02d", s.getCDNumber()) + "-" + 
                                      String.format("%02d", s.getTrackNumber()) + ". " + s.getTitle())
                     .collect(Collectors.joining(System.lineSeparator()));
        } else {
            return "No match found for " + string;
        }
    }
    
    @Override
    public void help() {
        Context.super.help();
        System.out.println("open operation is not supported by this node");
    }

}
