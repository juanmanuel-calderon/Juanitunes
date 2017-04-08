package com.jmc.juanitunes.organizer.impl.builder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.id3.framebody.AbstractFrameBodyTextInfo;

import com.jmc.juanitunes.organizer.Utils;
import com.jmc.juanitunes.organizer.api.builder.SongBuilder;
import com.jmc.juanitunes.organizer.api.library.Song;
import com.jmc.juanitunes.organizer.impl.library.SimpleSong;

public class Mp3SongBuilder implements SongBuilder {

	public Optional<Song> createNew(String source) {
		File file = new File(source);
		try {
			MP3File songFile = (MP3File) AudioFileIO.read(file);
			AudioHeader audioHeader = songFile.getAudioHeader();
			ID3v24Tag tag = songFile.getID3v2TagAsv24();
			
			String ext 		= "mp3";
			int duration 	= audioHeader.getTrackLength();
			int bitrate 	= new Long(audioHeader.getBitRateAsNumber()).intValue();
			double size 	= file.length() / (1024d * 1024d);
			
			String title = tag.getFirst(FieldKey.TITLE).trim();
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
            AbstractID3v2Frame artistsFrame = tag.getFirstField(ID3v24Frames.FRAME_ID_ARTIST);
            if(artistsFrame.getBody() instanceof AbstractFrameBodyTextInfo) {
            	AbstractFrameBodyTextInfo textBody = (AbstractFrameBodyTextInfo)artistsFrame.getBody(); 
            	artists.addAll(Stream.of(textBody.getText().split(";")).collect(Collectors.toList()));
            }
            
            if(artists.isEmpty()) artists.add("[[Unknown]]");
            
            List<String> albumArtists = new ArrayList<String>();
            AbstractID3v2Frame aaFrame = tag.getFirstField(ID3v24Frames.FRAME_ID_ACCOMPANIMENT); //TPE2 ID3v24 tag
            if(aaFrame.getBody() instanceof AbstractFrameBodyTextInfo) {
            	AbstractFrameBodyTextInfo textBody = (AbstractFrameBodyTextInfo)aaFrame.getBody(); 
            	albumArtists.addAll(Stream.of(textBody.getText().split(";")).collect(Collectors.toList()));
            }
            if(albumArtists.isEmpty()) albumArtists.add("[[Unknown]]");
            
            Song mp3Song = new SimpleSong(source,
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
            
            return Optional.of(mp3Song);
			
		} catch (Exception e) {
			System.err.println("Can't parse song " + source);
            System.err.println("Error is : " + e.getMessage());
		}
		
		return Optional.empty();
	}

}
