package com.willydupreez.infusion.util;

import java.io.IOException;

public final class Consoles {

	public static void waitForKeyPress() {
		if (System.console() == null) {
			try {
				System.in.read();
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
