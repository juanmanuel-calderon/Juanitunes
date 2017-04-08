package com.jmc.juanitunes.organizer.impl.builder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import com.jmc.juanitunes.organizer.Utils;
import com.jmc.juanitunes.organizer.api.builder.SongBuilder;
import com.jmc.juanitunes.organizer.api.library.Song;
import com.jmc.juanitunes.organizer.impl.library.SimpleSong;

public class FlacSongBuilder implements SongBuilder {

    public Optional<Song> createNew(String source) {
        File file = new File(source);
        try {
            AudioFile songFile = AudioFileIO.read(file);
            Tag tag = songFile.getTag();
            AudioHeader audioHeader = songFile.getAudioHeader();
            
            String ext      = "flac";
            int bitrate     = new Long(audioHeader.getBitRateAsNumber()).intValue();
            int duration    = audioHeader.getTrackLength();
            double size     = file.length() / (1024d * 1024d);
            
            String title    = tag.getFirst(FieldKey.TITLE).trim();
            if(title.isEmpty()) title = new File(source).getName().toString();
            
            int cdNumber = Utils.parseOrDefault(tag.getFirst(FieldKey.DISC_NO), 0);  
            int trackNumber = Utils.parseOrDefault(tag.getFirst(FieldKey.TRACK), 0);     
            int rating = Utils.parseOrDefault(tag.getFirst(FieldKey.RATING), -1);
            
            String genre         = tag.getFirst(FieldKey.GENRE).trim();
            String year          = tag.getFirst(FieldKey.YEAR).trim();
            String catalogNumber = tag.getFirst(FieldKey.COMMENT).trim();
            
            String album         = tag.getFirst(FieldKey.ALBUM).trim();
            if(album.isEmpty()) album = "[[Unknown]]";
            
            List<String> artists = new ArrayList<String>();
            tag.getFields(FieldKey.ARTIST).stream()
                                          .map(t -> t.toString().trim())
                                          .forEach(t -> artists.add(t));
            if(artists.isEmpty()) artists.add("[[Unknown]]");
            
            List<String> albumArtists = new ArrayList<String>();
            tag.getFields(FieldKey.ALBUM_ARTIST).stream()
                                                .map(t -> t.toString().trim())
                                                .forEach(t -> albumArtists.add(t));
            if(albumArtists.isEmpty()) albumArtists.add("[[Unknown]]");
            
            Song flacSong = new SimpleSong(source,
                                           title,
                                           cdNumber,
                                           trackNumber,
                                           genre,
                                           rating,
                                           duration,
                                           size,
                                           bitrate,
                                           ext,
                                           year,
                                           catalogNumber,
                                           albumArtists,
                                           album,
                                           artists);
            
            return Optional.of(flacSong);
            
        } catch(Exception e) {
            System.err.println("Can't parse song " + source);
            System.err.println("Error is : " + e.getMessage());
        }
        
        return Optional.empty();
    }
    
}
