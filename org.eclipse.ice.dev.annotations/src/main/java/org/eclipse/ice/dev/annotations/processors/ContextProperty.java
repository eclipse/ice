package org.eclipse.ice.dev.annotations.processors;

/**
 * Enumeration for Keys used in DataElement Velocity Template.
 */
enum ContextProperty {
	PACKAGE("package"),
	FIELDS("fields"),
	INTERFACE("interface"),
	CLASS("class");

	private String key;

	ContextProperty(String key) {
		this.key = key;
	}

	String key() {
		return this.key;
	}
}