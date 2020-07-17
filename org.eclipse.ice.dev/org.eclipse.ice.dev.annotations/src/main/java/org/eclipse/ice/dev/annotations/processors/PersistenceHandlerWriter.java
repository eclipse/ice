package org.eclipse.ice.dev.annotations.processors;

import java.util.Map;

import lombok.Builder;
import lombok.NonNull;

public class PersistenceHandlerWriter extends VelocitySourceWriter {

	/**
	 * Location of PersistenceHandler template for use with velocity.
	 *
	 * Use of Velocity ClasspathResourceLoader means files are discovered relative
	 * to the src/main/resources folder.
	 */
	private static final String PERSISTENCE_HANDLER_TEMPLATE = "templates/PersistenceHandler.vm";

	private static final String PACKAGE = "package";
	private static final String ELEMENT_INTERFACE = "elementInterface";
	private static final String CLASS = "class";
	private static final String COLLECTION = "collection";
	private static final String IMPLEMENTATION = "implementation";
	private static final String FIELDS = "fields";

	@Builder
	public PersistenceHandlerWriter(
		String packageName, String elementInterface, String className,
		String implementation, String collection, @NonNull Fields fields
	) {
		super();
		this.template = PERSISTENCE_HANDLER_TEMPLATE;
		this.context = Map.ofEntries(
			Map.entry(PACKAGE, packageName),
			Map.entry(ELEMENT_INTERFACE, elementInterface),
			Map.entry(CLASS, className),
			Map.entry(COLLECTION, collection),
			Map.entry(IMPLEMENTATION, implementation),
			Map.entry(FIELDS, fields)
		);
	}
}
