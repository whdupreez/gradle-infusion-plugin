package com.willydupreez.infusion.processor;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;

public class MarkdownProcessor {

	private PegDownProcessor processor;

	public MarkdownProcessor() {
		processor = new PegDownProcessor(Extensions.ALL);
	}

	public void process(File markdownFile, File htmlFile) {
		try {
			String markdown = new String(Files.readAllBytes(Paths.get(markdownFile.toURI())));
			String html = processor.markdownToHtml(markdown);
			Files.write(
				Paths.get(htmlFile.toURI()),
				html.getBytes(),
				StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING,
				StandardOpenOption.WRITE);
		} catch (Exception e) {
			throw new ProcessorException("Failed to process markdown.", e);
		}
	}

}

