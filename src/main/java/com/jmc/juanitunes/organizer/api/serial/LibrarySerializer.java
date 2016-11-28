package com.jmc.juanitunes.organizer.api.serial;

import com.jmc.juanitunes.organizer.api.library.Library;

public interface LibrarySerializer {
    
    public String serialize(Library source);
    public Library deserialize(String source);

}
