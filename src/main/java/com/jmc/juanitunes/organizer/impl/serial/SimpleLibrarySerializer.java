package com.jmc.juanitunes.organizer.impl.serial;

import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jmc.juanitunes.organizer.api.library.AlbumArtist;
import com.jmc.juanitunes.organizer.api.library.Library;
import com.jmc.juanitunes.organizer.api.library.Song;
import com.jmc.juanitunes.organizer.api.serial.LibrarySerializer;
import com.jmc.juanitunes.organizer.impl.builder.AlbumArtistBuilder;
import com.jmc.juanitunes.organizer.impl.library.SimpleLibrary;

public class SimpleLibrarySerializer implements LibrarySerializer {

    public String serialize(Library s) {
        StringBuilder str = new StringBuilder("");
        str.append("<library>")            .append(System.lineSeparator());
        s.getAllSongs().forEach(song -> str.append(new SimpleSongSerializer().serialize(song)));
        str.append("</library>")        .append(System.lineSeparator());        
        return str.toString();    
    }

    public Library deserialize(String source) {
        Set<Song> songs = deserializeSongs(source);
        Set<AlbumArtist> albumArtists = new AlbumArtistBuilder().createNew(songs);
        Library temp = new SimpleLibrary("temp");
        albumArtists.forEach(aa -> temp.addAlbumArtist(aa));
        
        return temp;
    }
    
    private Set<Song> deserializeSongs(String source) {
        Set<Song> songs = new TreeSet<Song>();
        SimpleSongSerializer songSerializer = new SimpleSongSerializer();
        String regex = Pattern.quote("<simplesong>") + "(?s)(.*?)" + Pattern.quote("</simplesong>");
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        while(matcher.find()) {
            songs.add(songSerializer.deserialize(matcher.group(1)));
        }
        
        return songs;
    }

}
