package com.willydupreez.infusion.server;

import java.io.File;

public class ServerContext {

	private String host;
	private int port;

	private File site;

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

	public File getSite() {
		return site;
	}

	public void setSite(File site) {
		this.site = site;
	}

}
