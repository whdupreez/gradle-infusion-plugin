package com.willydupreez.infusion.processor.markdown;

import java.util.List;
import java.util.Map;

import org.pegdown.LinkRenderer;
import org.pegdown.ToHtmlSerializer;
import org.pegdown.VerbatimSerializer;
import org.pegdown.ast.HeaderNode;
import org.pegdown.ast.TextNode;
import org.pegdown.plugins.ToHtmlSerializerPlugin;

public class ExtendedMarkdownSerializer extends ToHtmlSerializer {

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
	protected void printTag(TextNode node, String tag) {
		if (processingHeader) {
			tableOfContents.append(node.getText());
		}
		super.printTag(node, tag);
	}

	public String getTableOfContents() {
		return tableOfContents.toString();
	}

}
