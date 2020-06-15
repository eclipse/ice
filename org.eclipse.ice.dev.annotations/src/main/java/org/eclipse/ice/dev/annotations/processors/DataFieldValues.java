package org.eclipse.ice.dev.annotations.processors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum DataFieldValues {
	GETTER("getter"),
	SETTER("setter"),
	MATCH("match"),
	UNIQUE("unique"),
	SEARCH("search"),
	NULLABLE("nullable");

	@Getter private String key;

	@AllArgsConstructor
	public enum Default {
		IS_STRING("isString"),
		VALUE("value");

		@Getter private String key;
	}
}
