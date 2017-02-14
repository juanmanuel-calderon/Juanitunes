package com.jmc.juanitunes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogManager;

import com.jmc.juanitunes.organizer.LibraryOrganizer;

public class Main {
    
    public static void main(String[] args) {
        LogManager.getLogManager().reset();
        
        final String path = "/home/juanmanuel/Documents/workspace/juanitunes/test-files/Noragami/2014-01-29 - Goya no Machiawase/01. Goya no Machiawase.flac";
        
        List<String> sources = new ArrayList<String>();
        sources.add(path);
        
        LibraryOrganizer test = new LibraryOrganizer("test");
        long start = System.currentTimeMillis();
        try {
            test.addToCurrentLibrary(sources);
        } catch (RuntimeException e) {
            System.err.println("Error when parsing source");
            e.printStackTrace();
        }
        
        System.out.println("Parsing took : " + (System.currentTimeMillis() - start)+ " ms");
        start = System.currentTimeMillis();
        try {
            test.exportLibrary();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Export took : " + (System.currentTimeMillis() - start)+ " ms");
        
        LibraryOrganizer test2 = new LibraryOrganizer("test");
        start = System.currentTimeMillis();
        try {
            test2.importLibrary();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Import took : " + (System.currentTimeMillis() - start)+ " ms");
        
        //System.out.println(test2.getCurrentLibrary());
        //test2.getCurrentLibrary().getAllSongs().forEach(System.out::println);
        
        //System.out.println(test.getCurrentLibrary());
    }

}
