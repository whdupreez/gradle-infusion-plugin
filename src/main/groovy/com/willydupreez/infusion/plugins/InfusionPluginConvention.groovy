package com.willydupreez.infusion.plugins

import org.gradle.api.internal.project.ProjectInternal
import org.gradle.internal.reflect.Instantiator

class InfusionPluginConvention {

	ProjectInternal project

	InfusionPluginConvention(ProjectInternal project, Instantiator instantiator) {
		this.project = project

		// Init defaults.
	}

}
