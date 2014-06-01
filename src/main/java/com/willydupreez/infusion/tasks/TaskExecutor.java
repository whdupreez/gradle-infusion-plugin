package com.willydupreez.infusion.tasks;

import static java.util.Arrays.asList;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;

public class TaskExecutor {

	private Project project;
	private File projectDir;
	private File gradleHomeDir;

	public TaskExecutor(Project project) {
		this.project = project;
		this.projectDir = project.getProjectDir();
		this.gradleHomeDir = project.getGradle().getGradleHomeDir();
	}

	public void execute(String ... tasks) {

		validateTasks(asList(tasks));

		ProjectConnection connection = GradleConnector.newConnector()
				.useInstallation(gradleHomeDir)
				.forProjectDirectory(projectDir)
				.connect();

		connection.newBuild()
				.forTasks("infusionSite")
				.run();
	}

	private void validateTasks(List<String> tasks) {
		List<String> taskNames = project.getTasks().stream()
				.map(Task::getName)
				.collect(Collectors.toList());
		for (String task : tasks) {
			if (taskNames.contains(task)) {
				throw new RuntimeException("No such task");
			}
		}
	}

}
