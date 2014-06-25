package com.willydupreez.infusion.plugins;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class InfusionBasePlugin implements Plugin<Project> {

	private static final String INFUSION_PLUGIN_EXTENSION = "infusion";

	@Override
	public void apply(Project project) {
		project.getExtensions().create(INFUSION_PLUGIN_EXTENSION, InfusionPluginExtension.class, project);
	}

	private void configureClean(Project project) {
//		project.getTasks().
	}

	private void configureServe(Project project) {

	}

}
