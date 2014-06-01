package com.willydupreez.infusion.watch;

public class WatchException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public WatchException(String message, Throwable cause) {
		super(message, cause);
	}

	public WatchException(String message) {
		super(message);
	}

}
