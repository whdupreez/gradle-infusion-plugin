package com.willydupreez.infusion.watch;

import groovy.lang.Closure;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;

import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

public class FilePatternWatcher implements Runnable {

	private final Logger log = Logging.getLogger(FilePatternWatcher.class);

	private File directory;
	private String pattern;
	private Closure<Void> closure;

	private Thread thread;
	private boolean run;

	private final WatchService watchService;
	private final Map<WatchKey, File> keys;

	FilePatternWatcher(File directory, Closure<Void> closure) {
		this.directory = directory;
		this.closure = closure;
		this.keys = new HashMap<>();

		try {
			this.watchService = FileSystems.getDefault().newWatchService();
		} catch (Exception e) {
			throw new WatchException("Failed to create WatchService", e);
		}
	}

	public void start() {
		log.lifecycle("Start watching directory: ${directory}");
		watchRecursively(directory);
		run = true;
		thread = new Thread(this);
		thread.start();
	}

	public void stop() {
		log.lifecycle("Stop watching directory: ${directory}");
		run = false;
		try {
			watchService.close();
		} catch (IOException e) {
			// Fail silently.
		}
		try {
			thread.join();
		} catch (InterruptedException e) {
			// Fail silently.
		}
	}

	@Override
	public void run() {
		processEvents();
	}

	private void processEvents() {

		while (run) {

			WatchKey key;
			try {
				log.lifecycle("Watching ...");
				key = watchService.take();
			} catch (Exception e) {
				continue;
			}

			// Poll all the events queued for the key.
			for (WatchEvent<?> event : key.pollEvents()){
				WatchEvent.Kind kind = event.kind();
				switch (kind.name()){
					case "ENTRY_CREATE":
						log.lifecycle("Created");
//						watch(event);
						break;
					case "ENTRY_MODIFY":
						log.lifecycle("Modified: " + event.context());
						break;
					case "ENTRY_DELETE":
						log.lifecycle("Delete: " + event.context());
						break;
				}
				closure.call(event.context());
			}

			// Reset is invoked to put the key back to ready state
			boolean valid = key.reset();

			// If the key is invalid, just exit.
			if (!valid) {
				keys.remove(key);
				if (keys.isEmpty()) {
					log.lifecycle("No more directories are accessible. Stopping Watcher.");
					run = false;
				}
			}
		}
	}

	private void watchRecursively(File dir) {
		try {
			Files.walk(Paths.get(dir.toURI())).forEach(path -> {
				if (Files.isDirectory(path)) {
					watch(path.toFile());
				}
			});
		} catch (IOException e) {
			throw new WatchException("Failed to watch directory recursively: " + dir, e);
		}

		// Note: Recursive create directories!
	}

	private void watch(File dir) {
		try {

			WatchKey key = Paths.get(dir.toURI()).register(
				watchService,
				StandardWatchEventKinds.ENTRY_CREATE,
				StandardWatchEventKinds.ENTRY_DELETE,
				StandardWatchEventKinds.ENTRY_MODIFY);

			keys.put(key, dir);

			log.lifecycle("Watching directory: " + dir.getName());

		} catch (Exception e) {
			throw new WatchException("Failed to register watchService for directory: " + dir, e);
		}
	}

}
