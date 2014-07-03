package com.willydupreez.infusion.processor.markdown;

import java.util.Collections;

import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.pegdown.Extensions;
import org.pegdown.LinkRenderer;
import org.pegdown.PegDownProcessor;
import org.pegdown.VerbatimSerializer;
import org.pegdown.ast.RootNode;
import org.pegdown.plugins.ToHtmlSerializerPlugin;

import com.willydupreez.infusion.processor.MarkupProcessor;
import com.willydupreez.infusion.processor.MarkupProcessorException;

/**
 * A {@link MarkupProcessor} implementation that processes markdown
 * to produce HTML.
 *
 * @author Willy du Preez
 *
 */
public class MarkdownProcessor implements MarkupProcessor {

	private static final Logger log = Logging.getLogger(MarkdownProcessor.class);

	private PegDownProcessor processor;

	public MarkdownProcessor() {
		processor = new PegDownProcessor(Extensions.ALL);
	}

	@Override
	public String process(String markdown) {
		try {

			RootNode astRoot = processor.parseMarkdown(markdown.toCharArray());
			ExtendedMarkdownSerializer serializer = new ExtendedMarkdownSerializer(
            		new LinkRenderer(),
            		Collections.<String, VerbatimSerializer>emptyMap(),
            		Collections.<ToHtmlSerializerPlugin>emptyList());
			String html = serializer.toHtml(astRoot);
			String toc = serializer.getTableOfContents();
			log.lifecycle(toc);
			return html;
//			return replaceTocTag(html, toc);
		} catch (Exception e) {
			throw new MarkupProcessorException("Failed to process markdown.", e);
		}
	}

//	private String generateTableOfContents() {
//		return "<h1>Table of Contents</h1>";
////		return new ToHtmlSerializer(linkRenderer, verbatimSerializerMap).toHtml(astRoot);
//	}
//
//	private String replaceTocTag(String html, String toc) {
//		return html.replaceAll("[TABLE OF CONTENTS]", toc);
//	}

}
