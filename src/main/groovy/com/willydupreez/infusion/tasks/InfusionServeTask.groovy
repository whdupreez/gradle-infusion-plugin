package com.willydupreez.infusion.tasks

import static io.undertow.Handlers.resource
import io.undertow.Undertow
import io.undertow.server.handlers.resource.FileResourceManager

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction

import com.willydupreez.infusion.server.ServerContext
import com.willydupreez.infusion.server.UndertowServer
import com.willydupreez.infusion.util.Consoles

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

		def context = new ServerContext()
		context.port = port
		context.host = host
		context.site = site

		def server = new UndertowServer()
		server.init(context)
		server.start()

		if (waitForKeypress) {
			logger.println ""
			logger.println "Press any key to continue ..."
			logger.println ""
			Consoles.waitForKeyPress()
		}
	}

}
