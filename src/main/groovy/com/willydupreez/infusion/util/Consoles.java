package com.willydupreez.infusion.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public final class Consoles {

	public static void waitForKeyPress() {
		if (System.console() == null) {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
				reader.readLine();
			} catch (IOException e) {
				throw new RuntimeException("Unexpected error", e);
			}
		} else {
			System.console().readLine();
		}
	}

	private Consoles() {
	}

}
