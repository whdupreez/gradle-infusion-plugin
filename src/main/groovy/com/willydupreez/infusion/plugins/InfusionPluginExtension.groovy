package com.willydupreez.infusion.plugins

import org.gradle.api.Project

class InfusionPluginExtension {

	Project project

	InfusionPluginExtension(Project project) {
		this.project = project

		// Init defaults.
	}

}
