package com.willydupreez.infusion.watch;

public class SiteException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SiteException(String message, Throwable cause) {
		super(message, cause);
	}

	public SiteException(String message) {
		super(message);
	}

}
