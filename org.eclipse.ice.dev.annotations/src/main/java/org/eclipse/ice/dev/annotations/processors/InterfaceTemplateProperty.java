package org.eclipse.ice.dev.annotations.processors;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enumeration of keys of Interface Template.
 * @author Daniel Bluhm
 */
@AllArgsConstructor
public enum InterfaceTemplateProperty {
	PACKAGE("package"),
	INTERFACE("interface"),
	FIELDS("fields");

	@Getter private String key;
}
