package com.willydupreez.infusion.processor.markdown;

import org.pegdown.Printer;
import org.pegdown.ast.Node;
import org.pegdown.ast.Visitor;
import org.pegdown.plugins.ToHtmlSerializerPlugin;

public class TableOfContentsPlugin implements ToHtmlSerializerPlugin {

	@Override
	public boolean visit(Node node, Visitor visitor, Printer printer) {
		// TODO Auto-generated method stub
		return false;
	}

}
