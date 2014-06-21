package com.willydupreez.infusion.plugins

import org.gradle.api.Project
import org.gradle.internal.reflect.Instantiator

class InfusionPluginConvention {

	Project project

	InfusionPluginConvention(Project project, Instantiator instantiator) {
		this.project = project

		// Init defaults.
	}

}
