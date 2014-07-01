package com.willydupreez.infusion.processor.markdown;

import java.util.List;
import java.util.Map;

import org.pegdown.LinkRenderer;
import org.pegdown.ToHtmlSerializer;
import org.pegdown.VerbatimSerializer;
import org.pegdown.ast.HeaderNode;
import org.pegdown.plugins.ToHtmlSerializerPlugin;

public class MarkdownToHtmlSerializer extends ToHtmlSerializer {

	public MarkdownToHtmlSerializer(LinkRenderer linkRenderer,
			Map<String, VerbatimSerializer> verbatimSerializers,
			List<ToHtmlSerializerPlugin> plugins) {
		super(linkRenderer, verbatimSerializers, plugins);
	}

	@Override
	public void visit(HeaderNode node) {
		super.visit(node);
	}
	
	

}
