package com.willydupreez.infusion.tasks;

import java.io.File;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.TaskAction;

import com.willydupreez.infusion.server.ServerContext;
import com.willydupreez.infusion.server.UndertowServer;
import com.willydupreez.infusion.util.Consoles;

public abstract class AbstractInfusionServeTask extends DefaultTask {

	private String host;
	private int port;

	@InputDirectory
	private File siteDist;

	@TaskAction
	public final void serve() {

		onInit();

		ServerContext context = new ServerContext();
		context.setPort(port);
		context.setHost(host);
		context.setSite(siteDist);

		UndertowServer server = new UndertowServer();
		server.init(context);

		onStart();
		server.start();

		getLogger().lifecycle("Press any key to continue ...");
		Consoles.waitForKeyPress();

		onStop();
		server.stop();
	}

	protected abstract void onInit();
	protected abstract void onStart();
	protected abstract void onStop();

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public File getSiteDist() {
		return siteDist;
	}

	public void setSiteDist(File siteDist) {
		this.siteDist = siteDist;
	}

}
