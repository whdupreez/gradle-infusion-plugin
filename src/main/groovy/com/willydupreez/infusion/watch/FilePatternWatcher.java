/*
 * Copyright (c) 2014 Willy du Preez.
 */

package com.willydupreez.infusion.watch;

import groovy.lang.Closure;

import java.io.File;
import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

/**
 * Watches a directory recursively for CREATE, MODIFY and DELETE events,
 * and invokes a closure, passing a list of changed {@link Path}'s of
 * objects that have changed as argument, if an event is raised. The
 * watcher uses the producer-consumer pattern to process all events
 * before invoking the closure. This results in events being raised
 * while the closure is being invoked to all be processed together
 * before invoking the closure again.
 *
 * @author Willy du Preez
 *
 */
// TODO Patterns & Documentation
// TODO Fix file lock preventing delete when deleting a directory tree
public class FilePatternWatcher {

	private final Logger log = Logging.getLogger(FilePatternWatcher.class);

	private File directory;
//	private String pattern;
	private Closure<Void> closure;

	private Thread eventProducer;
	private Thread eventConsumer;
	private BlockingQueue<Event> eventQueue;

	private boolean run;

	private final WatchService watchService;
	private final Map<WatchKey, File> keys;

	/**
	 * Creates a watcher that watches for changes in
	 * the specified directory and invokes the closure
	 * provided if any change events are raised.
	 *
	 * @param directory the directory to watch
	 * @param closure the closure to invoke
	 */
	public FilePatternWatcher(File directory, Closure<Void> closure) {
		this.directory = directory;
		this.closure = closure;
		this.keys = new HashMap<>();
		this.eventProducer = new Thread(new WatchEventProducer());
		this.eventConsumer = new Thread(new WatchEventConsumer());
		this.eventQueue = new LinkedBlockingQueue<Event>();

		try {
			this.watchService = FileSystems.getDefault().newWatchService();
		} catch (Exception e) {
			throw new WatchException("Failed to create WatchService", e);
		}
	}

	/**
	 * Start watching.
	 */
	public void start() {
		log.lifecycle("Start watching directory: ${directory}");
		watchRecursively(directory);
		run = true;
		eventProducer.start();
		eventConsumer.start();
	}

	/**
	 * Stop watching.
	 */
	public void stop() {
		log.lifecycle("Stop watching directory: " + directory);
		run = false;
		try {
			watchService.close();
		} catch (IOException e) {
			// Ignore.
		}
		try {
			eventProducer.interrupt();
			eventProducer.join();
		} catch (InterruptedException e) {
			// Ignore.
		}
		try {
			eventConsumer.interrupt();
			eventConsumer.join();
		} catch (InterruptedException e) {
			// Ignore.
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

	private class Event {

		private WatchKey key;
		private WatchEvent<?> watchEvent;

		public Event(WatchKey key, WatchEvent<?> event) {
			this.key = key;
			this.watchEvent = event;
		}
	}

	private class WatchEventProducer implements Runnable {

		@Override
		public void run() {
			while (run) {

				WatchKey key;
				try {
					log.lifecycle("Watching ...");
					key = watchService.take();
				} catch (ClosedWatchServiceException | InterruptedException e) {
					continue;
				}

				// Poll all the events queued for the key.
				for (WatchEvent<?> event : key.pollEvents()) {
					try {
						eventQueue.put(new Event(key, event));
					} catch (InterruptedException e) {
						// Ignore.
					}
				}

				// Put the key back to ready state.
				boolean valid = key.reset();

				if (!valid) {
					keys.remove(key);
					if (keys.isEmpty()) {
						log.lifecycle("No more directories are accessible. Stopping Watcher.");
						run = false;
					}
				}
			}
		}

	}

	private class WatchEventConsumer implements Runnable {

		@Override
		public void run() {

			List<Path> changedPaths = new ArrayList<Path>(); // TODO Just a single-named path. Fix

			while (run) {

				Event event;
				try {
					event = eventQueue.take();
					// Sleep to allow the OS to release any file locks.
					Thread.sleep(100);
				} catch (InterruptedException e) {
					continue;
				}

				WatchEvent.Kind<?> kind = event.watchEvent.kind();
				Path path = (Path) event.watchEvent.context();
				changedPaths.add(path);

				switch (kind.name()){
					case "ENTRY_CREATE":
						log.lifecycle("Created: " + path);
						File file = keys.get(event.key);
						if (file.isDirectory()) {
							watchRecursively(file);
						}
						break;
					case "ENTRY_MODIFY":
						log.lifecycle("Modified: " + path);
						break;
					case "ENTRY_DELETE":
						log.lifecycle("Delete: " + path);
						break;
				}

				if (eventQueue.peek() == null) {
					try {
						closure.call((Object) changedPaths);
					} catch (Exception e) {
						e.printStackTrace();
						stop();
						if (!(e instanceof InterruptedException)) {
							throw e;
						}
					}
					changedPaths = new ArrayList<Path>();
				}

			}
		}

	}

}
