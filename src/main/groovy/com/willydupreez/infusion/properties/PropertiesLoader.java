package com.willydupreez.infusion.properties;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;



public class PropertiesLoader {

	public Map<String, String> load(File propertiesDir, String propertiesExt, String contentExt) {
		Map<String, String> map = new HashMap<>();
		try {
			Files.walk(propertiesDir.toPath()).filter(path -> {
				return Files.isRegularFile(path) && path.toString().endsWith(propertiesExt);
			}).forEach(path -> {
				Properties properties = new Properties();
				try (InputStream in = Files.newInputStream(path, StandardOpenOption.READ)) {
					properties.load(in);
				} catch (IOException e) {
					throw new PropertiesException("Failed to load properties: " + path.toString(), e);
				}
				for (Entry<Object, Object> entry : properties.entrySet()) {
					map.put(entry.getKey().toString(), entry.getValue().toString());
				}
			});
		} catch (IOException e) {
			throw new PropertiesException("Failed to load properties", e);
		}
		return map;
	}

}
