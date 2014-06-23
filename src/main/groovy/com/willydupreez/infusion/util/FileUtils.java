package com.willydupreez.infusion.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * File utilities.
 *
 * @author Willy du Preez
 *
 */
public class FileUtils {

	/**
	 * Reads a file as a String.
	 *
	 * @param file the file to read
	 * @return the file content as a string
	 * @throws FileException if an error occurs
	 */
	public static String readAsString(File file) throws FileException {
		try {
			return new String(Files.readAllBytes(Paths.get(file.toURI())));
		} catch (IOException e) {
			throw new FileException("Failed to read file as a String: " + file.getAbsolutePath(), e);
		}
	}

	/**
	 * Writes the content to the provided file. The file is created if
	 * it does not exist, and truncated if it exists.
	 *
	 * @param file the file to write to
	 * @param content the content to write
	 */
	public static void writeToFile(String content, File file) {
		try {
			Files.write(
					Paths.get(file.toURI()),
					content.getBytes(),
					StandardOpenOption.CREATE,
					StandardOpenOption.TRUNCATE_EXISTING,
					StandardOpenOption.WRITE);
		} catch (IOException e) {
			throw new FileException("Failed to write content to file: " + file.getAbsolutePath(), e);
		}
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
