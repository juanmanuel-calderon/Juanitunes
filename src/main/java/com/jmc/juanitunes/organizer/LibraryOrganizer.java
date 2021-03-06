package com.jmc.juanitunes.organizer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jmc.juanitunes.organizer.api.builder.LibraryImporter;
import com.jmc.juanitunes.organizer.api.library.Album;
import com.jmc.juanitunes.organizer.api.library.AlbumArtist;
import com.jmc.juanitunes.organizer.api.library.Library;
import com.jmc.juanitunes.organizer.api.serial.LibrarySerializer;
import com.jmc.juanitunes.organizer.impl.builder.ConcurrentLibraryImporter;
import com.jmc.juanitunes.organizer.impl.library.MultiCDAlbum;
import com.jmc.juanitunes.organizer.impl.library.SimpleLibrary;
import com.jmc.juanitunes.organizer.impl.serial.SimpleLibrarySerializer;

public class LibraryOrganizer {

    private Library currentLibrary;
    private LibraryImporter importer = new ConcurrentLibraryImporter();
    
    public LibraryOrganizer(String libraryName) {
        currentLibrary = new SimpleLibrary(libraryName);
    }

    public void importLibrary() throws IOException {
        importLibrary(currentLibrary.getName());
    }
    
    public void importLibrary(String filename) throws IOException {
        
        String librarySource = new String(Files.readAllBytes(Paths.get(filename + ".library")));
        
        Library temp = new SimpleLibrarySerializer().deserialize(librarySource);
        currentLibrary.merge(temp);
        importMultiCDAlbums(filename + ".mcd");
    }
    
    private void importMultiCDAlbums(String filename) throws IOException {
        
        if(!(new File(filename).exists())) return;
        String mcdSource = new String(Files.readAllBytes(Paths.get(filename)));
        new File(filename).delete();
        
        Stream.of(mcdSource.split(System.lineSeparator()))
              .map(s -> reconstructMultiCDAlbum(s))
              .forEach(mcd -> combineAlbumsToMultiCD(mcd.getName(), new ArrayList<Album>(mcd.getAlbums())));
    }
    
    private MultiCDAlbum reconstructMultiCDAlbum(String source) {
        String[] nameAndAlbums = source.split(":");
        String name = nameAndAlbums[0];
        String albumsStr = nameAndAlbums[1];
        
        Set<Album> albums = Stream.of(albumsStr.split("\\|\\-\\-\\|"))
                                  .map(cn -> currentLibrary.getAllAlbums().stream()
                                                           .filter(a -> a.getCatalogNumber().equals(cn))
                                                           .findFirst())
                                  .filter(Optional::isPresent)
                                  .map(o -> o.get())
                                  .collect(Collectors.toSet());
        
        return new MultiCDAlbum(name, albums);
    }

    public void exportLibrary() throws IOException {
        exportLibrary(currentLibrary.getName());
    }
    
    public void exportLibrary(String filename) throws IOException {
        
        (new File(filename)).delete();
        LibrarySerializer librarySerializer = new SimpleLibrarySerializer();
        Files.write(Paths.get(filename + ".library"), librarySerializer.serialize(currentLibrary).getBytes());
        exportMultiCDAlbums(filename + ".mcd");
    }
    
    private void exportMultiCDAlbums(String filename) throws IOException {
        
        String multiCDAlbums = 
            currentLibrary.getAllAlbums().stream()
                                         .filter(a -> a instanceof MultiCDAlbum)
                                         .map(mcd -> getMultiCDAlbumDefinition((MultiCDAlbum) mcd))
                                         .collect(Collectors.joining(System.lineSeparator()));
        
        if(multiCDAlbums.isEmpty()) return;
        Files.write(Paths.get(filename), multiCDAlbums.getBytes());                                  
    }
    
    private String getMultiCDAlbumDefinition(MultiCDAlbum mcd) {
        String name = mcd.getName();
        String albums = mcd.getAlbums().stream()
                                       .map(a -> a.getCatalogNumber())
                                       .collect(Collectors.joining("|--|"));
        return name + ":" + albums;
    }
    
    public void addToCurrentLibrary(List<String> sources) throws RuntimeException {
        Library temp = importer.importSongs(sources);
        currentLibrary.merge(temp);
    }

    public void combineAlbumsToMultiCD(String name, List<Album> albums) {                
        MultiCDAlbum newAlbum = new MultiCDAlbum(name, new TreeSet<Album>(albums));
        albums.forEach(a -> currentLibrary.getAlbumArtists().stream()
                                                            .filter(aa -> aa.getAlbums().contains(a))
                                                            .forEach(aa -> replaceAlbumsforMultiCD(aa, albums, newAlbum)));
    }
    
    private void replaceAlbumsforMultiCD(AlbumArtist albumArtist, List<Album> oldAlbums, MultiCDAlbum newAlbum) {
        oldAlbums.forEach(a -> albumArtist.removeAlbum(a));
        albumArtist.addAlbum(newAlbum);
    }
    
    public Library getCurrentLibrary() {
        return currentLibrary;
    }
}
