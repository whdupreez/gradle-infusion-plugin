package com.willydupreez.infusion.plugins;

import java.io.File;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.Delete;

import com.willydupreez.infusion.tasks.InfusionSiteTask;

public class InfusionBasePlugin implements Plugin<Project> {

	public static final String INFUSION_GROUP = "infusion";

	public static final String CLEAN_TASK_NAME = "infusionClean";
	public static final String SITE_TASK_NAME = "infusionSite";
	public static final String SERVE_TASK_NAME = "infusionServe";
	public static final String WATCH_TASK_NAME = "infusionWatch";

	private static final String CLEAN_TASK_DESCRIPTION = "Cleans the infusion site build directories";
	private static final String SITE_TASK_DESCRIPTION = "Generates the infusion site";

	private static final String INFUSION_PLUGIN_EXTENSION = "infusion";

	@Override
	public void apply(Project project) {
		InfusionPluginExtension infusionExtension = project.getExtensions()
				.create(INFUSION_PLUGIN_EXTENSION, InfusionPluginExtension.class, project);

		configureClean(project, infusionExtension);
		configureSite(project, infusionExtension);
	}

	private void configureClean(Project project, InfusionPluginExtension infusionExtension) {
		Delete cleanTask = project.getTasks().create(CLEAN_TASK_NAME, Delete.class);
		cleanTask.setDescription(CLEAN_TASK_DESCRIPTION);
		cleanTask.setGroup(INFUSION_GROUP);
		cleanTask.delete(
				new File(project.getBuildDir(), "site"),
				new File(project.getBuildDir(), "site-tmp"));
	}

	private void configureSite(Project project, InfusionPluginExtension infusionExtension) {
		InfusionSiteTask siteTask = project.getTasks().create(SITE_TASK_NAME, InfusionSiteTask.class);
		siteTask.setDescription(SITE_TASK_DESCRIPTION);
		siteTask.setSiteSrc(new File(project.getProjectDir(), "src/site"));
		siteTask.setSiteDist(new File(project.getBuildDir(), "site"));
		siteTask.setSiteTmp(new File(project.getBuildDir(), "site-tmp"));
	}

	private void configureServe(Project project) {

	}

}
