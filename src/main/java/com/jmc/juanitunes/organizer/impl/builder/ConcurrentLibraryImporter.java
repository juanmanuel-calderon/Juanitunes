package com.jmc.juanitunes.organizer.impl.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import com.jmc.juanitunes.organizer.api.builder.LibraryImporter;
import com.jmc.juanitunes.organizer.api.library.Library;
import com.jmc.juanitunes.organizer.impl.library.SimpleLibrary;

public class ConcurrentLibraryImporter implements LibraryImporter {

	private final int CHUNK_SIZE = 500;
	
	public Library importSongs(List<String> sources) {

		List<List<String>> chunks = separate(sources, CHUNK_SIZE);
		
		List<ImporterThread> importers =
				chunks.stream()
					  .map(ImporterThread::new)
					  .collect(Collectors.toList());
		
		ExecutorService executor = Executors.newFixedThreadPool(10);
		importers.forEach(i -> executor.execute(i));
		executor.shutdown();
		while(!executor.isTerminated());
		
		return importers.stream()
						.map(ImporterThread::getResult)
						.reduce(new SimpleLibrary("temp"), 
							    (o, n) -> o.merge(n));
	}
	
	private List<List<String>> separate(List<String> bigList, int n){
	    List<List<String>> chunks = new ArrayList<List<String>>();

	    for (int i = 0; i < bigList.size(); i += n) {
	    	List<String> chunk = bigList.subList(i, Math.min(bigList.size(), i + n));
	    	chunks.add(chunk);
	    }

	    return chunks;
	}
	
	private class ImporterThread implements Runnable {

		private LibraryImporter importer = new LinearLibraryImporter();
		private Library result;
		private List<String> sources;
		
		public ImporterThread(List<String> sources) {
			this.sources = sources;
		}
		
		public void run() {
			result = importer.importSongs(sources);
		}
		
		public Library getResult() { return result; }
		
	}

}
