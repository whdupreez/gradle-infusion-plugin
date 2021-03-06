package com.willydupreez.infusion.plugins;

import static java.util.Arrays.asList;

import java.io.File;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.Delete;

import com.willydupreez.infusion.tasks.InfusionServeTask;
import com.willydupreez.infusion.tasks.InfusionSiteTask;
import com.willydupreez.infusion.tasks.InfusionWatchTask;

public class InfusionPlugin implements Plugin<Project> {

	public static final String INFUSION_GROUP = "infusion";

	public static final String CLEAN_TASK_NAME = "infusionClean";
	public static final String SITE_TASK_NAME = "infusionSite";
	public static final String SERVE_TASK_NAME = "infusionServe";
	public static final String WATCH_TASK_NAME = "infusionWatch";

	private static final String CLEAN_TASK_DESCRIPTION = "Cleans the infusion site build directories";
	private static final String SITE_TASK_DESCRIPTION = "Generates the infusion site";
	private static final String SERVE_TASK_DESCRIPTION = "Serves the generated site using an embedded web server";
	private static final String WATCH_TASK_DESCRIPTION = "Watches the site directory for changes and reloads the web server";

	@Override
	public void apply(Project project) {
		project.getPlugins().apply(InfusionBasePlugin.class);

		InfusionPluginExtension infusionExtension = project.getExtensions().getByType(InfusionPluginExtension.class);

		configureCleanTask(project, infusionExtension);
		configureSiteTask(project, infusionExtension);
		configureServeTask(project);
		configureWatchTask(project);

	}

	private void configureCleanTask(Project project, InfusionPluginExtension infusionExtension) {
		Delete cleanTask = project.getTasks().create(CLEAN_TASK_NAME, Delete.class);
		cleanTask.setDescription(CLEAN_TASK_DESCRIPTION);
		cleanTask.setGroup(INFUSION_GROUP);
		cleanTask.delete(
				new File(project.getBuildDir(), "site"),
				new File(project.getBuildDir(), "site-tmp"));
	}

	private void configureSiteTask(Project project, InfusionPluginExtension infusionExtension) {
		InfusionSiteTask siteTask = project.getTasks().create(SITE_TASK_NAME, InfusionSiteTask.class);
		siteTask.setDescription(SITE_TASK_DESCRIPTION);
		siteTask.setGroup(INFUSION_GROUP);
		siteTask.setSiteSrc(new File(project.getProjectDir(), "src/site"));
		siteTask.setSiteDist(new File(project.getBuildDir(), "site"));
		siteTask.setSiteTmp(new File(project.getBuildDir(), "site-tmp"));
	}

	private void configureServeTask(Project project) {
		InfusionServeTask serveTask = project.getTasks().create(SERVE_TASK_NAME, InfusionServeTask.class);
		serveTask.setDescription(SERVE_TASK_DESCRIPTION);
		serveTask.setGroup(INFUSION_GROUP);
		serveTask.setHost("0.0.0.0");
		serveTask.setPort(9000);
		serveTask.setSiteDist(new File(project.getBuildDir(), "site"));
		serveTask.setDependsOn(asList(SITE_TASK_NAME));
	}

	private void configureWatchTask(Project project) {
		InfusionWatchTask watchTask = project.getTasks().create(WATCH_TASK_NAME, InfusionWatchTask.class);
		watchTask.setDescription(WATCH_TASK_DESCRIPTION);
		watchTask.setGroup(INFUSION_GROUP);
		watchTask.setHost("0.0.0.0");
		watchTask.setPort(9000);
		watchTask.setSiteDist(new File(project.getBuildDir(), "site"));
		watchTask.setTaskToExecute(SITE_TASK_NAME);
		watchTask.setSiteSrc(new File(project.getProjectDir(), "src/site"));
	}

}
