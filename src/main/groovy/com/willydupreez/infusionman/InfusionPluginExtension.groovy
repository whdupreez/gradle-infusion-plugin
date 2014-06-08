package com.willydupreez.infusionman;

class InfusionPluginExtension {

	File siteSrc
	File siteTmp
	File siteDist

	String host = "localhost"
	int port = 9090

	InfusionPluginExtension(File siteSrc, File siteTmp, File siteDist) {
		this.siteSrc = siteSrc
		this.siteTmp = siteTmp
		this.siteDist = siteDist
	}
}