package com.willydupreez.infusion.tasks;

import java.io.File;

import groovy.lang.Closure;

import org.gradle.api.Project;
import org.gradle.api.tasks.InputDirectory;

import com.willydupreez.infusion.watch.FilePatternWatcher;
import com.willydupreez.infusion.watch.TaskExecutor;

public class InfusionWatchTask extends AbstractInfusionServeTask {

	private String taskToExecute;

	private FilePatternWatcher watcher;

	@InputDirectory
	private File siteSrc;

	@Override
	protected void onInit() {
		Project project = this.getProject();
		watcher = new FilePatternWatcher(siteSrc, new Closure<Void>(this) {

			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unused")
			public void doCall(Object arguments) {
				new TaskExecutor(project).execute(taskToExecute);
			}

		});	}

	@Override
	protected void onStart() {
		getLogger().lifecycle("Watcher started: " + getSiteDist().getAbsolutePath().toString());
		watcher.start();
	}

	@Override
	protected void onStop() {
		getLogger().lifecycle("Watcher stopped.");
		watcher.stop();
	}

	public String getTaskToExecute() {
		return taskToExecute;
	}

	public void setTaskToExecute(String taskToExecute) {
		this.taskToExecute = taskToExecute;
	}

	public File getSiteSrc() {
		return siteSrc;
	}

	public void setSiteSrc(File siteSrc) {
		this.siteSrc = siteSrc;
	}

}
