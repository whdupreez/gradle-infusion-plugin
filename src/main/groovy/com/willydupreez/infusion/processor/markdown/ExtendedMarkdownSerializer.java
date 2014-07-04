package com.willydupreez.infusion.processor.markdown;

import java.util.List;
import java.util.Map;

import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.pegdown.LinkRenderer;
import org.pegdown.ToHtmlSerializer;
import org.pegdown.VerbatimSerializer;
import org.pegdown.ast.HeaderNode;
import org.pegdown.ast.TextNode;
import org.pegdown.plugins.ToHtmlSerializerPlugin;

public class ExtendedMarkdownSerializer extends ToHtmlSerializer {

	@SuppressWarnings("unused")
	private static final Logger log = Logging.getLogger(ExtendedMarkdownSerializer.class);

	private static final String SPACES = "                    ";

	private boolean processingHeader;
	private StringBuilder tableOfContents = new StringBuilder();

	public ExtendedMarkdownSerializer(LinkRenderer linkRenderer,
			Map<String, VerbatimSerializer> verbatimSerializers,
			List<ToHtmlSerializerPlugin> plugins) {
		super(linkRenderer, verbatimSerializers, plugins);
	}

	@Override
	public void visit(HeaderNode node) {
		processingHeader = true;
		tableOfContents.append(SPACES.substring(0, node.getLevel()));
		super.visit(node);
		tableOfContents.append("\n");
		processingHeader = false;
	}

	@Override
	public void visit(TextNode node) {
		if (processingHeader) {
			tableOfContents.append(node.getText());
		}
		super.visit(node);
	}

	public String getTableOfContents() {
		return tableOfContents.toString();
	}

}
