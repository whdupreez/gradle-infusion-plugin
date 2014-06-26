package com.willydupreez.infusion.tasks

import static io.undertow.Handlers.resource;
import io.undertow.Undertow
import io.undertow.server.handlers.resource.FileResourceManager

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.TaskAction

import com.willydupreez.infusion.util.Consoles;

class InfusionServeTask extends DefaultTask {

	boolean waitForKeypress = true
	String host
	int port

	@InputDirectory
	File siteDist

	@TaskAction
	def serve() {
		startServing(siteDist, port, host)
	}

	private void startServing(File site, int port, String host) {
		logger.println "Starting to serve site: ${site}"

		def siteHandler = resource(new FileResourceManager(site, 100))
				.setDirectoryListingEnabled(true)

		Undertow server = Undertow.builder()
				.addHttpListener(port, host)
				.setHandler(siteHandler)
				.build()
		server.start()

		logger.println "Server started: http://localhost:/${port}"
		logger.println ""

		if (waitForKeypress) {
			logger.println "Press any key to continue ..."
			logger.println ""
			Consoles.waitForKeyPress()
		}
	}

}
