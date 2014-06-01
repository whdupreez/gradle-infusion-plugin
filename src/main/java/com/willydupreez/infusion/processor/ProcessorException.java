package com.willydupreez.infusion.processor;

public class ProcessorException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ProcessorException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProcessorException(String message) {
		super(message);
	}

}
