package com.willydupreez.infusion.tasks;

import java.io.File;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.TaskAction;

import com.willydupreez.infusion.server.ServerContext;
import com.willydupreez.infusion.server.UndertowServer;

public abstract class AbstractInfusionServeTask extends DefaultTask {

	private String host;
	private int port;

	private boolean waitforKeyPress;

	@InputDirectory
	private File siteDist;

	@TaskAction
	public void serve() {

		onInit();

		ServerContext context = new ServerContext();
		context.setPort(port);
		context.setHost(host);
		context.setSite(siteDist);

		UndertowServer server = new UndertowServer();
		server.init(context);

		onStart();

		server.start();

		onStop();
	}

	abstract void onInit();
	abstract void onStart();
	abstract void onStop();

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

	public boolean isWaitforKeyPress() {
		return waitforKeyPress;
	}

	public void setWaitforKeyPress(boolean waitforKeyPress) {
		this.waitforKeyPress = waitforKeyPress;
	}

}
