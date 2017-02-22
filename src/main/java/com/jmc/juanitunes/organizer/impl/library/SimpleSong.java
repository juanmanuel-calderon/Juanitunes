package com.jmc.juanitunes.organizer.impl.library;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.jmc.juanitunes.organizer.Utils;
import com.jmc.juanitunes.organizer.api.library.Song;

public class SimpleSong implements Song, Comparable<Song> {
    
    private final String filename;
    private final String title;
    private final int    cdNumber;
    private final int    trackNumber;
    private final String genre;
    private final int    rating;
    private final int    duration;
    private final double size;
    private final int    bitrate;
    private final String extension;
    private final String year;
    private final String catalogNumber;
    
    private final List<String>  albumArtists    = new ArrayList<String>();
    private final String        album;
    private final List<String>  artists         = new ArrayList<String>();
    
    public SimpleSong(String filename,
                      String title,
                      int cdNumber,
                      int trackNumber,
                      String genre,
                      int rating,
                      int duration,
                      double size,
                      int bitrate,
                      String extension,
                      String year,
                      String catalogNumber,
                      List<String> albumArtists,
                      String album,
                      List<String> artists) {
        
        this.filename = filename;
        this.title = title;
        this.cdNumber = cdNumber;
        this.trackNumber = trackNumber;
        this.genre = genre;
        this.rating = rating;
        this.duration = duration;
        this.size = size;
        this.bitrate = bitrate;
        this.extension = extension;
        this.year = year;
        this.catalogNumber = catalogNumber;
        this.albumArtists.addAll(albumArtists);
        this.album = album;
        this.artists.addAll(artists);
    }
    
    public SimpleSong(Song song) {
        this(song.getFilename(),
             song.getTitle(),
             song.getCDNumber(),
             song.getTrackNumber(),
             song.getGenre(),
             song.getRating(),
             song.getDurationInSeconds(),
             song.getSizeInMegaBytes(),
             song.getBitrate(),
             song.getExtension(),
             song.getYear(),
             song.getCatalogNumber(),
             song.getAlbumArtists(),
             song.getAlbum(),
             song.getArtists());
    }
    
    
    public Optional<Song> match(String string) {
        
        if(title.toLowerCase().contains(string.toLowerCase())) return Optional.of(this);
        if(genre.toLowerCase().contains(string.toLowerCase())) return Optional.of(this);
        if(extension.toLowerCase().contains(string.toLowerCase())) return Optional.of(this);

        if(artists.stream().anyMatch(a -> a.contains(string.toLowerCase()))) return Optional.of(this);
        
        return Optional.empty();
    }

    public String getFilename() {
        return filename;
    }

    public String getTitle() {
        return title;
    }

    public int getCDNumber() {
        return cdNumber;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public String getGenre() {
        return genre;
    }

    public int getRating() {
        return rating;
    }

    public int getDurationInSeconds() {
        return duration;
    }

    public double getSizeInMegaBytes() {
        return size;
    }

    public int getBitrate() {
        return bitrate;
    }

    public String getExtension() {
        return extension;
    }
    
    public String getYear() {
        return year;
    }
    
    public String getCatalogNumber() {
        return catalogNumber;
    }

    public List<String> getAlbumArtists() {
        return Collections.unmodifiableList(albumArtists);
    }
    
    public String getAlbum() {
        return album;
    }
    
    public List<String> getArtists() {
        return Collections.unmodifiableList(artists);
    }

    @Override
    public String toString() {
        String durationInMinutes = Utils.secondsToMinutes(duration);
        
        StringBuilder songStr = new StringBuilder();
        
        songStr = songStr.append("Song located at: " + filename)  .append(System.lineSeparator());
        songStr = songStr.append("Title: " + title)               .append(System.lineSeparator());
        songStr = songStr.append("Artists: ")                     .append(System.lineSeparator());
        songStr = songStr.append(artists.stream()
                                        .map(a -> "\t" + a)
                                        .collect(Collectors.joining(System.lineSeparator())))
                         .append(System.lineSeparator());
        
        songStr = songStr.append("CD Number: " + cdNumber)        .append(System.lineSeparator());
        songStr = songStr.append("Track Number: " + trackNumber)  .append(System.lineSeparator());
        songStr = songStr.append("Rating: " + rating + "/100")    .append(System.lineSeparator());
        songStr = songStr.append("Duration: " + durationInMinutes).append(System.lineSeparator());
        songStr = songStr.append("Size: " + size + "MB")          .append(System.lineSeparator());
        songStr = songStr.append("Extension: ." + extension)      .append(System.lineSeparator());
        songStr = songStr.append("Bitrate: " + bitrate + " kbps");
        
        return songStr.toString();
    }
    
    @Override
    public boolean equals(Object other) {
        if(other == null) return false;
        if(!(other instanceof Song)) return false;
        
        Song otherSong = (Song) other;
        if(!otherSong.getFilename().equals(filename)) return false;
        
        return true;            
    }

    @Override
    public int compareTo(Song other) {
        return filename.compareTo(other.getFilename());
    }
}
