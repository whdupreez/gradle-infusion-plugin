package com.willydupreez.infusion.properties;

public class PropertiesException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PropertiesException(String message, Throwable cause) {
		super(message, cause);
	}

	public PropertiesException(String message) {
		super(message);
	}

}
