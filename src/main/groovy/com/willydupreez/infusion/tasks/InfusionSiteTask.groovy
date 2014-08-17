package com.willydupreez.infusion.tasks

import static com.willydupreez.infusion.util.FileUtils.*

import org.apache.tools.ant.filters.ReplaceTokens
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

import com.willydupreez.infusion.processor.markdown.MarkdownProcessor
import com.willydupreez.infusion.properties.PropertiesLoader
import com.willydupreez.infusion.template.TemplateProcessor
import com.willydupreez.infusion.util.FileUtils

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
	private File siteSrcProperties

	private File siteTmpMd2html
	private File siteTmpProperties

	@TaskAction
	def site() {

		siteSrcHtml 		= new File(siteSrc, "html")
		siteSrcMarkdown 	= new File(siteSrc, "markdown")
		siteSrcResources 	= new File(siteSrc, "resources")
		siteSrcTemplates 	= new File(siteSrc, "templates")
		siteSrcProperties	= new File(siteSrc, "properties")

		siteTmpMd2html		= new File(siteTmp, "md2html")
		siteTmpProperties	= new File(siteTmp, "properties")

		siteTmp.mkdirs()
		siteTmpMd2html.mkdirs()
		siteTmpProperties.mkdirs()

		siteDist.mkdirs()

		prepareSiteTmp()
		processMarkdown()
		processProperties()
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
			File md2htmlFile = createWithExtension(markdownFile, "md2html")
			FileUtils.writeToFile(html, md2htmlFile);
		}

		// Apply templates to the HTML partials.
		project.fileTree(siteTmpMd2html) {
			include "**/*.md2html"
		}.each { File md2html ->
			tProcessor.process(template, md2html, createWithExtension(md2html, "html"))
		}

	}

	def processProperties() {

		// Access to closure.
		def srcHtml = siteSrcHtml
		def tmpMd2html = siteTmpMd2html
		def dest = siteTmpProperties

		def props = new PropertiesLoader().load(siteSrcProperties, ".properties", ".content");

		// Copy HTML.
		project.copy {
			from srcHtml
			into dest
			include '**/*.html'
			filter ReplaceTokens, tokens: props
		}

		// Copy processed markdown.
		project.copy {
			from tmpMd2html
			into dest
			include '**/*.html'
			filter ReplaceTokens, tokens: props
		}

	}

	def copySite() {

		// Access to closure.
		def srcResources = siteSrcResources
		def tmpProperties = siteTmpProperties
		def dist = siteDist

		// Copy HTML processed properties.
		project.copy {
			from tmpProperties
			into dist
			include '**/*.html'
		}

		// Copy resources.
		project.copy {
			from srcResources
			into dist
			include '**/*'
		}

	}

}
