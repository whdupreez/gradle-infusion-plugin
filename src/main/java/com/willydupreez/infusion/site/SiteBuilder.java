package com.willydupreez.infusion.site;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.willydupreez.infusion.processor.MarkdownProcessor;
import com.willydupreez.infusion.processor.TemplateProcessor;

public class SiteBuilder {

	private File siteSrc;
	private File siteBuild;
	private File siteDist;

	private File siteSrcHtml;
	private File siteSrcMarkdown;
	private File siteSrcResources;
	private File siteSrcTemplates;

	private File siteBuildMd2html;

	public SiteBuilder(File siteSrc, File siteBuild, File siteDist) {
		this.siteSrc = siteSrc;
	}

	public void build() {

		siteSrcHtml 		= new File(siteSrc, "html");
		siteSrcMarkdown 	= new File(siteSrc, "markdown");
		siteSrcResources 	= new File(siteSrc, "resources");
		siteSrcTemplates 	= new File(siteSrc, "templates");

		siteBuildMd2html		= new File(siteBuild, "md2html");

		siteBuild.mkdirs();
		siteBuildMd2html.mkdirs();

		siteDist.mkdirs();

//		prepareSiteBuildDir();
//		processMarkdown();
//		copySite();

	}

	private void prepareSiteBuildDir() throws IOException {

		// Copy Markdown into site build directory.
//		Files.walk(Paths.get(siteSrcMarkdown.toURI())).forEach( path -> {
//			if (path.toString().endsWith(".md")) {
//				Files.copy(source, out);
//			}
//		});
//		project.copy {
//			from srcMarkdown
//			into tmpMd2html
//			include '**/*.md'
//		}
	}

	private void processMarkdown() {

		MarkdownProcessor mdProcessor = new MarkdownProcessor();
		TemplateProcessor tProcessor = new TemplateProcessor();
		File template = new File(siteSrcTemplates, "article.tpl.html");

		// Convert Markdown into HTML partials.
//		project.fileTree(siteTmpMd2html) {
//			include "**/*.md"
//		}.each { File markdown ->
//			mdProcessor.process(markdown, md2htmlOut(markdown))
//		}

		// Apply templates to the HTML partials.
//		project.fileTree(siteTmpMd2html) {
//			include "**/*.md2html"
//		}.each { File md2html ->
//			tProcessor.process(template, md2html, htmlOut(md2html))
//		}

	}

	private void copySite() {

		// Access to closure.
//		def srcHtml = siteSrcHtml
//		def srcResources = siteSrcResources
//		def tmpMd2html = siteTmpMd2html
//		def dist = siteDist

		// Copy HTML.
//		project.copy {
//			from srcHtml
//			into dist
//			include '**/*.html'
//		}

		// Copy resources.
//		project.copy {
//			from srcResources
//			into dist
//			include '**/*'
//		}

		// Copy processed markdown.
//		project.copy {
//			from tmpMd2html
//			into dist
//			include '**/*.html'
//		}

	}

	private File md2htmlOut(File markdown) {
		return new File(markdown.getParentFile(), setExtension(markdown.getName(),  "md2html"));
	}

	private File htmlOut(File md2html) {
		return new File(md2html.getParentFile(), setExtension(md2html.getName(),  "html"));
	}

	private String setExtension(String filename, String extension) {
		int idx = filename.lastIndexOf('.');
		String stripped = idx > 0 ? filename.substring(0, idx) : filename;
		return stripped + "." + extension;
	}


}
