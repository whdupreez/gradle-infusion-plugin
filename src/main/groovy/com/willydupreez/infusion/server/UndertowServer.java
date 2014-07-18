package com.willydupreez.infusion.server;

import static io.undertow.Handlers.resource;

import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

import io.undertow.Undertow;
import io.undertow.server.handlers.resource.FileResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;

public class UndertowServer implements Server {

	private static final Logger logger = Logging.getLogger(UndertowServer.class);

	private Undertow server;
	private ServerContext context;

	public void init(ServerContext context) {

		logger.lifecycle("Starting to serve site: ${site}");

		this.context = context;

		ResourceHandler siteHandler = resource(new FileResourceManager(this.context.getSite(), 100))
				.setDirectoryListingEnabled(true);

		server = Undertow.builder()
				.addHttpListener(this.context.getPort(), this.context.getHost())
				.setHandler(siteHandler)
				.build();

	}

	public void start() {
		server.start();
		logger.lifecycle("Server started: http://localhost:/${port}");
	}

	public void stop() {
		server.stop();
		context = null;
	}

}
