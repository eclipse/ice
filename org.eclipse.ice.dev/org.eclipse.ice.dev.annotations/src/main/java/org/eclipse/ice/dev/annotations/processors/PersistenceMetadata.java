package org.eclipse.ice.dev.annotations.processors;

import lombok.Builder;
import lombok.Data;

/**
 * Metadata extracted from persistence annotations.
 * @author Daniel Bluhm
 */
@Data
@Builder
public class PersistenceMetadata {
	/**
	 * Collection name for persistence.
	 */
	private String collection;
}
