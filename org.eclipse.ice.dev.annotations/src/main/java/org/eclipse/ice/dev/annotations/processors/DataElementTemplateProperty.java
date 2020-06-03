package org.eclipse.ice.dev.annotations.processors;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enumeration for Keys used in DataElement Velocity Template.
 */
@AllArgsConstructor
enum DataElementTemplateProperty {
	PACKAGE("package"),
	FIELDS("fields"),
	INTERFACE("interface"),
	CLASS("class");

	@Getter private String key;
}