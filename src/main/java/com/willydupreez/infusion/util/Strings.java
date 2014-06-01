package com.willydupreez.infusion.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Strings {

	public static String readAsString(File file) throws IOException {
		return new String(Files.readAllBytes(Paths.get(file.toURI())));
	}

}
