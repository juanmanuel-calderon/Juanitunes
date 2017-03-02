package com.jmc.juanitunes.cli;

import java.util.Scanner;

import com.jmc.juanitunes.cli.api.Context;
import com.jmc.juanitunes.cli.impl.AlbumArtistContext;
import com.jmc.juanitunes.cli.impl.LibraryContext;
import com.jmc.juanitunes.organizer.LibraryOrganizer;

public class CommandLineInterface {
    
    private Context currentContext;
    
    public CommandLineInterface(LibraryOrganizer libraryOrganizer) {
        currentContext = new LibraryContext(libraryOrganizer.getCurrentLibrary());
    }
    
    public void start() {
        System.out.println("Current in " + currentContext.pwd());
        Scanner sc = new Scanner(System.in);
        while(true) {
            String input;
            try {
                input = sc.nextLine().trim();               
                String[] command = input.split(" ");
                switch(command[0].trim().toLowerCase()) {
                    case "open": 
                        currentContext = currentContext.open(Integer.parseInt(command[1]));
                        break;
                    case "close":
                        currentContext = currentContext.close();
                        break;
                    case "pwd": 
                        System.out.println(currentContext.pwd());
                        break;
                    case "list":
                        System.out.print(currentContext.list()); //print because list ends in a line separator
                        break;
                    case "detail":
                        System.out.println(currentContext.detail(Integer.parseInt(command[1])));
                        break;
                    case "find":
                        System.out.println(currentContext.find(command[1]));
                        break;
                    case "merge":
                        if(currentContext instanceof AlbumArtistContext) {
                            AlbumArtistContext aac = (AlbumArtistContext) currentContext;
                            aac.merge(Integer.parseInt(command[1]), Integer.parseInt(command[2]), command[3]);
                        } else {
                            throw new UnsupportedOperationException("merge operation not supported");
                        }
                        break;
                    case "exit": 
                        sc.close();
                        return;
                    case "help":
                    default: currentContext.help();
                }
            } catch(UnsupportedOperationException e) {
                System.err.println("Operation not supported for node");
                System.err.println(e.getMessage());
            } catch(IllegalArgumentException e) {
                System.err.println("Illegal argument for the operation");
                System.err.println(e.getMessage());
            } catch(Exception e) {
                System.err.println("Error while reading command, try again");
                e.printStackTrace();
                continue;
            }
            
        }
    }

}
