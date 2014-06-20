package com.willydupreez.infusion.plugins;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class InfusionPlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		project.getPlugins().apply(InfusionBasePlugin.class);
	}

}
