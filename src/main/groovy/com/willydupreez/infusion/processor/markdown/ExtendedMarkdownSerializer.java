package com.willydupreez.infusion.processor.markdown;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.pegdown.LinkRenderer;
import org.pegdown.ToHtmlSerializer;
import org.pegdown.VerbatimSerializer;
import org.pegdown.ast.HeaderNode;
import org.pegdown.ast.RootNode;
import org.pegdown.ast.SuperNode;
import org.pegdown.ast.TextNode;
import org.pegdown.plugins.ToHtmlSerializerPlugin;

public class ExtendedMarkdownSerializer extends ToHtmlSerializer {

	private static final String TOC_MARKER = "\\[(TOC|toc)\\]";

	@SuppressWarnings("unused")
	private static final Logger log = Logging.getLogger(ExtendedMarkdownSerializer.class);

	private boolean processingHeader;
	private List<TocEntry> tableOfContents;
	private TocEntry currentEntry;

	public ExtendedMarkdownSerializer(LinkRenderer linkRenderer,
			Map<String, VerbatimSerializer> verbatimSerializers,
			List<ToHtmlSerializerPlugin> plugins) {
		super(linkRenderer, verbatimSerializers, plugins);
	}

	@Override
	public void visit(HeaderNode node) {

		processingHeader = true;

		currentEntry = new TocEntry();
		currentEntry.level = node.getLevel();
		currentEntry.id = "toc-" + tableOfContents.size();

		super.visit(node);

		tableOfContents.add(currentEntry);

		processingHeader = false;
	}

	@Override
	public void visit(TextNode node) {
		if (processingHeader) {
			currentEntry.text = node.getText();
		}
		super.visit(node);
	}

	@Override
	protected void printTag(SuperNode node, String tag) {
		if (node instanceof HeaderNode) {
			printer.print('<').print(tag).print(" id=\"" + currentEntry.id + "\">");
	        visitChildren(node);
	        printer.print('<').print('/').print(tag).print('>');
		} else {
			super.printTag(node, tag);
		}
	}

	@Override
	public String toHtml(RootNode astRoot) {

		processingHeader = false;
		tableOfContents = new ArrayList<>();

		String html = super.toHtml(astRoot);
		String tocHtml = createTableOfContentsHtml();

		return insertTocAtMarker(html, tocHtml);
	}

	private String createTableOfContentsHtml() {
		StringBuilder tocHtml = new StringBuilder();
		tocHtml.append("<div class=\"toc\">");
		int currentLevel = 0;
		for (TocEntry entry : tableOfContents) {
			if (entry.level > currentLevel) {
				tocHtml.append("<ul>");
			} else if (entry.level < currentLevel) {
				tocHtml.append("</ul>");
			}
			currentLevel = entry.level;
			tocHtml.append("<li><a href=\"#" + entry.id + "\">");
			tocHtml.append(entry.text);
			tocHtml.append("</a></li>");
		}
		tocHtml.append("</div>");
		return tocHtml.toString();
	}

	private String insertTocAtMarker(String html, String tocHtml) {
		return html.replaceAll(TOC_MARKER, tocHtml);
	}

	private class TocEntry {
		private int level;
		private String id;
		private String text;
	}

}
