package org.eclipse.ice.dev.annotations.processors;

import java.util.Properties;

/**
 * Properties used to initialize Velocity.
 */
enum VelocityProperties {

	// Set up Velocity using the Singleton approach; ClasspathResourceLoader allows
	// us to load templates from src/main/resources
	RESOURCE_LOADER("resource.loader", "class"),
	CLASS_RESOURCE_LOADER(
		"class.resource.loader.class",
		"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader"
	);

	private String key;
	private String value;

	VelocityProperties(String key, String value) {
		this.key = key;
		this.value = value;
	}

	String key() {
		return this.key;
	}

	String value() {
		return this.value;
	}

	public static Properties get() {
		Properties p = new Properties();
		for (VelocityProperties vp : VelocityProperties.values()) {
			p.setProperty(vp.key(), vp.value());
		}
		return p;
	}
}