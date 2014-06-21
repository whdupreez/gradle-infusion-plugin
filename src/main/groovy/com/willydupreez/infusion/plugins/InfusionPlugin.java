package com.willydupreez.infusion.plugins;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class InfusionPlugin implements Plugin<Project> {

	public static final String CLEAN_TASK_NAME = "infusionClean";
	public static final String SITE_TASK_NAME = "infusionSite";
	public static final String SERVE_TASK_NAME = "infusionServe";
	public static final String WATCH_TASK_NAME = "infusionWatch";

	@Override
	public void apply(Project project) {
		project.getPlugins().apply(InfusionBasePlugin.class);

		InfusionPluginConvention infusionConvention = project.getConvention().getPlugin(InfusionPluginConvention.class);

	}

}