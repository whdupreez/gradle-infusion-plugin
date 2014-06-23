package com.willydupreez.infusion.tasks

import static com.willydupreez.infusion.util.FileUtils.*

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

import com.willydupreez.infusion.processor.MarkdownProcessor
import com.willydupreez.infusion.template.TemplateProcessor
import com.willydupreez.infusion.util.FileUtils;

class InfusionSiteTask extends DefaultTask {

	@InputDirectory
	File siteSrc

	@OutputDirectory
	File siteDist

	@OutputDirectory
	File siteTmp

	private File siteSrcHtml
	private File siteSrcMarkdown
	private File siteSrcResources
	private File siteSrcTemplates

	private File siteTmpMd2html

	@TaskAction
	def site() {

		siteSrcHtml 		= new File(siteSrc, "html")
		siteSrcMarkdown 	= new File(siteSrc, "markdown")
		siteSrcResources 	= new File(siteSrc, "resources")
		siteSrcTemplates 	= new File(siteSrc, "templates")

		siteTmpMd2html		= new File(siteTmp, "md2html")

		siteTmp.mkdirs()
		siteTmpMd2html.mkdirs()

		siteDist.mkdirs()

		prepareSiteTmp()
		processMarkdown()
		copySite()

	}

	def prepareSiteTmp() {

		// Access to closure
		def srcMarkdown = siteSrcMarkdown
		def tmpMd2html = siteTmpMd2html

		// Copy Markdown into site-tmp.
		project.copy {
			from srcMarkdown
			into tmpMd2html
			include '**/*.md'
		}
	}

	def processMarkdown() {

		MarkdownProcessor mdProcessor = new MarkdownProcessor()
		TemplateProcessor tProcessor = new TemplateProcessor()
		File template = new File(siteSrcTemplates, "article.tpl.html")

		// Convert Markdown into HTML partials.
		project.fileTree(siteTmpMd2html) {
			include "**/*.md"
		}.each { File markdownFile ->
			String markdown = FileUtils.readAsString(markdownFile)
			String html = mdProcessor.process(markdown)
			File md2htmlFile = createWithExtension(markdown, "md2html")
			FileUtils.writeToFile(html, md2htmlFile);
		}

		// Apply templates to the HTML partials.
		project.fileTree(siteTmpMd2html) {
			include "**/*.md2html"
		}.each { File md2html ->
			tProcessor.process(template, md2html, createWithExtension(md2html, "html"))
		}

	}

	def copySite() {

		// Access to closure.
		def srcHtml = siteSrcHtml
		def srcResources = siteSrcResources
		def tmpMd2html = siteTmpMd2html
		def dist = siteDist

		// Copy HTML.
		project.copy {
			from srcHtml
			into dist
			include '**/*.html'
		}

		// Copy resources.
		project.copy {
			from srcResources
			into dist
			include '**/*'
		}

		// Copy processed markdown.
		project.copy {
			from tmpMd2html
			into dist
			include '**/*.html'
		}

	}

}
