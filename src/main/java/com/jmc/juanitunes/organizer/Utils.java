package com.jmc.juanitunes.organizer;

import java.io.File;
import java.util.Comparator;
import java.util.stream.Stream;

public class Utils {
	
	public static String secondsToMinutes(int seconds) {

		String res = "";
		res += seconds / 60;
		res += ":";
		res += String.format("%02d", seconds % 60);

		return res;
	}

	public static String getFileExtension(File file) {
		String name = file.getName();
		try {
			return name.substring(name.lastIndexOf(".") + 1);
		} catch (IndexOutOfBoundsException e) {
			return "";
		}
	}
	
	public static <U> Comparator<U> generateFinalComparator(Comparator<U>... criteria) {
		Comparator<U> initial = (s1, s2) -> 0;
		return Stream.of(criteria).reduce(initial, (c1, c2) -> c1.thenComparing(c2));
	}
}
