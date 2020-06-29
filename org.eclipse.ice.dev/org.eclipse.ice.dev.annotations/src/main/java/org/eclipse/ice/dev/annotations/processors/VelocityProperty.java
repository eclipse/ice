package org.eclipse.ice.dev.annotations.processors;

/**
 * Properties used to initialize Velocity.
 */
enum VelocityProperty {
	RESOURCE_LOADER("resource.loader", "class"),
	CLASS_RESOURCE_LOADER(
		"class.resource.loader.class",
		"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader"
	);

	private String key;
	private String value;

	VelocityProperty(String key, String value) {
		this.key = key;
		this.value = value;
	}

	String key() {
		return this.key;
	}

	String value() {
		return this.value;
	}
}