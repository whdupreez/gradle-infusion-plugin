package com.willydupreez.infusion.plugins;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class InfusionPlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		project.getPlugins().apply(InfusionBasePlugin.class);

		InfusionPluginExtension infusionExtension = project.getExtensions().getByType(InfusionPluginExtension.class);

		configureProcessors(infusionExtension);
		configureSite(infusionExtension);
		configureTemplates(infusionExtension);
		configureWatch(infusionExtension);
	}

	private void configureProcessors(InfusionPluginExtension infusionExtension) {

	}

	private void configureSite(InfusionPluginExtension infusionExtension) {

	}

	private void configureTemplates(InfusionPluginExtension infusionExtension) {

	}

	private void configureWatch(InfusionPluginExtension infusionExtension) {

	}

}
