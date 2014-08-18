package com.willydupreez.infusion.properties;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

/**
 * Loads properties as a {@link Map}.
 *
 * @author Willy du Preez
 *
 */
public class PropertiesLoader {

	private static final Logger log = Logging.getLogger(PropertiesLoader.class);

	private Map<String, String> contentCache = new HashMap<>();

	public Map<String, String> load(File propertiesDir, String propertiesExt, String contentExt) {

		Map<String, String> tokens = new HashMap<>();

		try {

			Files.walk(propertiesDir.toPath()).filter(path -> {
				return isPropertyFile(path, propertiesExt);
			}).forEach(path -> {
				Properties properties = loadProperties(path);
				Map<String, String> processedProperties = processProperties(properties, path.getParent(), contentExt);
				tokens.putAll(processedProperties);
			});

		} catch (IOException e) {
			throw new PropertiesException("Failed to load properties", e);
		}

		return tokens;
	}

	private boolean isPropertyFile(Path path, String propertiesExt) {
		return Files.isRegularFile(path) && path.toString().endsWith(propertiesExt);
	}

	private Properties loadProperties(Path path) {
		Properties properties = new Properties();
		try (InputStream in = Files.newInputStream(path, StandardOpenOption.READ)) {
			properties.load(in);
		} catch (IOException e) {
			throw new PropertiesException("Failed to load properties: " + path.toString(), e);
		}
		return properties;
	}

	private Map<String, String> processProperties(Properties properties, Path contentPath, String contentExt) {
		Map<String, String> processed = new HashMap<>();
		for (Entry<Object, Object> entry : properties.entrySet()) {
			String key = entry.getKey().toString();
			String value = entry.getValue().toString();
			if (isContentPlaceholder(value)) {
				value = loadContent(value, contentPath, contentExt);
			}
			processed.put(key, value);
		}
		return processed;
	}

	private boolean isContentPlaceholder(String value) {
		String trimmedValue = value.trim();
		return trimmedValue.startsWith("content[") && trimmedValue.endsWith("]");
	}

	private synchronized String loadContent(String contentKey, Path contentPath, String contentExt) {
		if (contentCache.containsKey(contentKey)) {
			return contentCache.get(contentKey);
		} else {
			Path contentFile = getContentFile(contentKey, contentPath, contentExt);
			String content;
			try {
				content = new String(Files.readAllBytes(contentFile));
			} catch (IOException e) {
				throw new PropertiesException("Failed to load content for key: " + contentKey);
			}
			contentCache.put(contentKey, content);
			return content;
		}
	}

	private Path getContentFile(String contentKey, Path contentPath, String contentExt) {
		String contentFilename = contentKey.substring(contentKey.indexOf('[') + 1, contentKey.length() - 1);
		log.lifecycle("Loading content from file: " + contentFilename + contentExt);
		return contentPath.resolve(contentFilename + contentExt);
	}

}
