package com.jmc.juanitunes.organizer.impl.serial;

import java.util.ArrayList;
import java.util.List;

import com.jmc.juanitunes.organizer.api.library.Song;
import com.jmc.juanitunes.organizer.api.serial.SongSerializer;
import com.jmc.juanitunes.organizer.impl.library.SimpleSong;

public class SimpleSongSerializer implements SongSerializer {
    
    public String serialize(Song s) {
        
        StringBuilder str = new StringBuilder("");
        str.append("\t<simplesong>")     .append(System.lineSeparator());
        str.append("\t\t<filename = ")   .append(s.getFilename())         .append("/>").append(System.lineSeparator());
        str.append("\t\t<title = ")      .append(s.getTitle())            .append("/>").append(System.lineSeparator());
        str.append("\t\t<cdnumber = ")   .append(s.getCDNumber())         .append("/>").append(System.lineSeparator());
        str.append("\t\t<tracknumber = ").append(s.getTrackNumber())      .append("/>").append(System.lineSeparator());
        str.append("\t\t<genre = ")      .append(s.getGenre())            .append("/>").append(System.lineSeparator());
        str.append("\t\t<rating = ")     .append(s.getRating())           .append("/>").append(System.lineSeparator());
        str.append("\t\t<duration = ")   .append(s.getDurationInSeconds()).append("/>").append(System.lineSeparator());
        str.append("\t\t<size = ")       .append(s.getSizeInMegaBytes())  .append("/>").append(System.lineSeparator());
        str.append("\t\t<bitrate = ")    .append(s.getBitrate())          .append("/>").append(System.lineSeparator());
        str.append("\t\t<ext = ")        .append(s.getExtension())        .append("/>").append(System.lineSeparator());
        str.append("\t\t<year = ")       .append(s.getYear())             .append("/>").append(System.lineSeparator());
        str.append("\t\t<album = ")      .append(s.getAlbum())            .append("/>").append(System.lineSeparator());
        str.append("\t\t<catalog = ")    .append(s.getCatalogNumber())    .append("/>").append(System.lineSeparator());
        str.append(serializeArtists(s.getArtists()));
        str.append(serializeAlbumArtists(s.getAlbumArtists()));
        str.append("\t</simplesong>")    .append(System.lineSeparator());        
        return str.toString();    
    }
    
    private String serializeArtists(List<String> artists) {
        StringBuilder str = new StringBuilder("");
        str.append("\t\t<artists>").append(System.lineSeparator());
        artists.stream()
               .forEach(a -> str.append("\t\t\t<artist = ")
                                .append(a)
                                .append("/>")
                                .append(System.lineSeparator())
                        );
        str.append("\t\t</artists>").append(System.lineSeparator());
        return str.toString();
    }
    
    private String serializeAlbumArtists(List<String> albumArtists) {
        StringBuilder str = new StringBuilder("");
        str.append("\t\t<albumartists>").append(System.lineSeparator());
        albumArtists.stream()
                    .forEach(a -> str.append("\t\t\t<albumartist = ")
                                     .append(a)
                                     .append("/>")
                                     .append(System.lineSeparator())
                            );
        str.append("\t\t</albumartists>").append(System.lineSeparator());
        return str.toString();
    }

    public Song deserialize(String source) {
        String filename = "";
        String title = "";
        int cdNumber = 0;
        int trackNumber = 0;
        String genre = "";
        int rating = -1;
        int duration = 0;
        double size = 0;
        int bitrate = 0;
        String extension = "";
        String album = "";
        String catalog = "";
        String year = "";
        List<String> albumArtists = new ArrayList<String>();
        List<String> artists = new ArrayList<String>();
        
        String[] lines = source.split(System.lineSeparator());
        for(String line : lines) {
            line = line.trim();
            line = line.replace("<", "");
            line = line.replace("/>", "");
            
            String[] keyValue = line.split(" = ");
            if(keyValue.length != 2) continue;
            String key = keyValue[0];
            String value = keyValue[1];
            switch(key) {
            case "filename":        filename    = value;                     break;
            case "title":           title       = value;                     break;
            case "cdnumber":        cdNumber    = Integer.parseInt(value);   break;
            case "tracknumber":     trackNumber = Integer.parseInt(value);   break;
            case "genre":           genre       = value;                     break;
            case "rating":          rating      = Integer.parseInt(value);   break;
            case "duration":        duration    = Integer.parseInt(value);   break;
            case "size":            size        = Double.parseDouble(value); break;
            case "bitrate":         bitrate     = Integer.parseInt(value);   break;
            case "ext":             extension   = value;                     break;
            case "album":           album       = value;                     break;
            case "catalog":         catalog     = value;                     break;
            case "year":            year        = value;                     break;
            case "artist":          artists.add(value);                      break;
            case "albumartist":     albumArtists.add(value);                 break;
            default: break;
            }
        }
        
        return new SimpleSong(filename, title, cdNumber, trackNumber, 
                              genre, rating, duration, size, bitrate, 
                              extension, year, catalog, albumArtists, 
                              album, artists);
    }
}
