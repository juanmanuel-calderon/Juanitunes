package com.jmc.juanitunes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogManager;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.jmc.juanitunes.cli.CommandLineInterface;
import com.jmc.juanitunes.organizer.LibraryOrganizer;

public class Main {
    
    public static void main(String[] args) {
        LogManager.getLogManager().reset();
        CommandLineParser parser = new DefaultParser();
        
        Options options = new Options();
        options.addOption("l", "library-file", true, "path to library file, mandatory, will create if it does not exist");
        options.addOption("p", "parse-path", true, "path to the media files to parse");
        options.addOption("n", "library-name", true, "name of the library to create");
        options.addOption("h", "help", false, "display this help message");
        
        try {
            CommandLine line = parser.parse(options, args);
            
            if(line.hasOption('h')) {
                new HelpFormatter().printHelp("juanitunes", options);
            }
            
            boolean hasLibraryFile = line.hasOption('l');
            boolean hasParsePath = line.hasOption('p');
            
            String name = line.hasOption('n') ? line.getOptionValue('n') : "root"; 
            LibraryOrganizer libraryOrganizer = new LibraryOrganizer(name);
            
            if(hasLibraryFile) {
                String libraryPath = line.getOptionValue('l') + "/" + name;
                File libraryFile = new File(libraryPath + ".library");
                
                if(!libraryFile.exists()) {
                    System.out.println("Library file does not exist: " + libraryPath);
                    checkParse(hasParsePath, libraryOrganizer, line, libraryPath);
                } else {
                    try {
                        libraryOrganizer.importLibrary(libraryPath);
                    } catch (IOException e) {
                        System.err.println("Error while importing library from: " + libraryPath);
                        System.err.println("Message: " + e.getMessage());
                    }
                    new CommandLineInterface(libraryOrganizer).start();
                    export(libraryOrganizer, libraryPath);
                }
            } else {
                checkParse(hasParsePath, libraryOrganizer, line, name);
            }
        } catch(ParseException exp) {
            System.out.println("Unexpected exception:" + exp.getMessage());
        }
    }
    
    
    private static void checkParse(boolean hasParsePath, LibraryOrganizer lo, CommandLine line, String path) {
        if(!hasParsePath) {
            System.err.println("Specify either a correct library file or a path to parse");
            return;
        } else {
            String parsePath = line.getOptionValue('p');
            doParse(lo, parsePath);
            export(lo, path);
        }
    }
    
    private static void doParse(LibraryOrganizer lo, String parsePath) {
        List<String> sources = new ArrayList<String>();
        if(!(new File(parsePath).exists())) {
            System.err.println("Cannot parse: path does not exist: " + parsePath);
            return;
        }
        sources.add(parsePath);
        lo.addToCurrentLibrary(sources);
        new CommandLineInterface(lo).start();
        
    }
    
    private static void export(LibraryOrganizer lo, String path) {
        try {
            lo.exportLibrary(path);
        } catch (IOException e) {
            System.err.println("Error while exporting library from: " + path);
            System.err.println("Message: " + e.getMessage());
        }
    }

}
