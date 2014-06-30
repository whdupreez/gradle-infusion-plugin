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
			String html = processor.markdownToHtml(markdown);
			String toc = generateTableOfContents();
			return replaceTocTag(html, toc);
		} catch (Exception e) {
			throw new MarkupProcessorException("Failed to process markdown.", e);
		}
	}

	private String generateTableOfContents() {
		return "<h1>Table of Contents</h1>";
//		return new ToHtmlSerializer(linkRenderer, verbatimSerializerMap).toHtml(astRoot);
	}

	private String replaceTocTag(String html, String toc) {
		return html.replaceAll("[TABLE OF CONTENTS]", toc);
	}

}
