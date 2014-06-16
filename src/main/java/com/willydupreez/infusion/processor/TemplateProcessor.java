package com.willydupreez.infusion.processor;

import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.willydupreez.infusion.util.FileUtils;

public class TemplateProcessor {

	private SimpleTemplateEngine engine = new SimpleTemplateEngine();

	public void process(File templateFile, File content, File out) {
		try {
			Map<String, String> context = new HashMap<>();
			context.put("content", FileUtils.readAsString(content));
			Template template = engine.createTemplate(templateFile);
			template.make(context).writeTo(new FileWriter(out));
		} catch (IOException | ClassNotFoundException e) {
			throw new ProcessorException("Failed to process template: " + templateFile, e);
		}
	}

}
