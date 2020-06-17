package org.eclipse.ice.dev.annotations.processors;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * String constants corresponding to the annotation values in DataField.
 * @see org.eclipse.ice.dev.annotations.DataField
 * @see org.eclipse.ice.dev.annotations.processors.Field
 * @author Daniel Bluhm
 */
@AllArgsConstructor
enum DataFieldValues {
	GETTER("getter"),
	SETTER("setter"),
	MATCH("match"),
	UNIQUE("unique"),
	SEARCH("search"),
	NULLABLE("nullable");

	@Getter private String key;

	/**
	 * String constants corresponding to the annotation values in DataField.Default.
	 * @see org.eclipse.ice.dev.annotations.DataField.Default
	 * @author Daniel Bluhm
	 */
	@AllArgsConstructor
	public enum Default {
		IS_STRING("isString"),
		VALUE("value");

		@Getter private String key;
	}
}
