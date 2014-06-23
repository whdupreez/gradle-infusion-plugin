package com.willydupreez.infusion.processor;

/**
 * Thrown if an error occurs while processing markup.
 *
 * @author Willy du Preez
 *
 */
public class MarkupProcessorException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MarkupProcessorException(String message, Throwable cause) {
		super(message, cause);
	}

	public MarkupProcessorException(String message) {
		super(message);
	}

}
