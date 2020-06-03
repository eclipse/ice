package org.eclipse.ice.dev.annotations.processors;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enumeration of keys of Persistence Handler Template.
 * @author Daniel Bluhm
 */
@AllArgsConstructor
enum PersistenceHandlerTemplateProperty {
	PACKAGE("package"),
	ELEMENT_INTERFACE("elementInterface"),
	CLASS("class"),
	COLLECTION("collection"),
	IMPLEMENTATION("implementation");
	
	@Getter private String key;
}