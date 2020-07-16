package org.eclipse.ice.dev.annotations.processors;

import lombok.AllArgsConstructor;
import lombok.Getter;

class ClassTemplateProperties {

	@AllArgsConstructor
	public enum Meta {
		PACKAGE("package"),
		FIELDS("fields"),
		INTERFACE("interface"),
		CLASS("class"),
		QUALIFIEDIMPL("fullyQualifiedImplementationName"),
		QUALIFIED("fullyQualifiedName");
		@Getter private String key;
	}
	
	@AllArgsConstructor
	public enum PersistenceHandler {
		ELEMENT_INTERFACE("elementInterface"),
		COLLECTION("collection"),
		CLASS("persistenceClassName"),
		IMPLEMENTATION("implementation"),
		QUALIFIED("fullyQualifiedPersistenceHandler");;
		
		@Getter private String key;
	}
	
}
