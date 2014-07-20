package com.willydupreez.infusion.plugins;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

public class InfusionBasePlugin implements Plugin<Project> {

	@SuppressWarnings("unused")
	private final Logger log = Logging.getLogger(InfusionBasePlugin.class);

	private static final String INFUSION_PLUGIN_EXTENSION = "infusion";

	@Override
	public void apply(Project project) {
		InfusionPluginExtension infusionExtension = project.getExtensions()
				.create(INFUSION_PLUGIN_EXTENSION, InfusionPluginExtension.class, project);

		configureProcessors(infusionExtension);
		configureTemplates(infusionExtension);
	}

	private void configureProcessors(InfusionPluginExtension infusionExtension) {

	}

	private void configureTemplates(InfusionPluginExtension infusionExtension) {

	}

}
