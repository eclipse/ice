package org.eclipse.ice.dev.annotations.processors;

import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * POJO representing metadata extracted from DataElement and associated
 * annotations.
 *
 * @author Daniel Bluhm
 */
@Data
@SuperBuilder
public class DataElementMetadata {
	/**
	 * Base name of classes to be generated.
	 */
	protected String name;

	/**
	 * Package of classes to be generated.
	 */
	protected String packageName;

	/**
	 * Collected fields of the DataElement.
	 */
	protected Fields fields;

	/**
	 * Fully qualified name (package + name) of the DataElement.
	 * @return fully qualified name.
	 */
	public String getFullyQualifiedName() {
		return String.format("%s.%s", this.packageName, this.name);
	}
}
