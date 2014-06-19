package com.willydupreez.infusion.tasks;

import java.io.File;

import org.gradle.api.Project;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;

/**
 * Executes Gradle build tasks.
 *
 * @author Willy du Preez
 *
 */
public class TaskExecutor {

	private File projectDir;
	private File gradleHomeDir;

	/**
	 * Create a task executor for the project.
	 *
	 * @param project the project
	 */
	public TaskExecutor(Project project) {
		this.projectDir = project.getProjectDir();
		this.gradleHomeDir = project.getGradle().getGradleHomeDir();
	}

	/**
	 * Executes the specified Gradle tasks.
	 *
	 * @param tasks the tasks to execute
	 */
	public void execute(String ... tasks) {

		ProjectConnection connection = GradleConnector.newConnector()
				.useInstallation(gradleHomeDir)
				.forProjectDirectory(projectDir)
				.connect();

		connection.newBuild()
				.forTasks(tasks)
				.run();

		connection.close();
	}

}
