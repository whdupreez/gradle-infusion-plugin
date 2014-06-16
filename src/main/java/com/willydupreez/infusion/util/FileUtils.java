package com.willydupreez.infusion.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils {

	public static String readAsString(File file) throws IOException {
		return new String(Files.readAllBytes(Paths.get(file.toURI())));
	}

	/**
	 * Creates a {@link File} object in the same location as the original
	 * file, but with the specified extension.
	 *
	 * @param original the original file.
	 * @param extension the extension of the new file object.
	 * @return the new file object, in the same location, with the
	 * 		specified extension.
	 */
	public static File createWithExtension(File original, String extension) {
		return new File(original.getParentFile(), setExtension(original.getName(), extension));
	}

	public static String setExtension(String filename, String extension) {
		int idx = filename.lastIndexOf('.');
		String stripped = idx > 0 ? filename.substring(0, idx) : filename;
		return stripped + "." + extension;
	}

}
