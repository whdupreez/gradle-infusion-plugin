package com.willydupreez.infusion.processor;

import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;

/**
 * A {@link MarkupProcessor} implementation that processes markdown
 * to produce HTML.
 *
 * @author Willy du Preez
 *
 */
public class MarkdownProcessor implements MarkupProcessor {

	private PegDownProcessor processor;

	public MarkdownProcessor() {
		processor = new PegDownProcessor(Extensions.ALL);
	}

	@Override
	public String process(String markdown) {
		try {
			return processor.markdownToHtml(markdown);
		} catch (Exception e) {
			throw new MarkupProcessorException("Failed to process markdown.", e);
		}
	}

}
