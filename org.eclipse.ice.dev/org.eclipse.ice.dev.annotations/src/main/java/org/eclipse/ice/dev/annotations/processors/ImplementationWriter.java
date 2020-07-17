package org.eclipse.ice.dev.annotations.processors;

import java.util.Map;

import lombok.Builder;

public class ImplementationWriter extends VelocitySourceWriter {

	/**
	 * Location of DataElement template for use with velocity.
	 *
	 * Use of Velocity ClasspathResourceLoader means files are discovered relative
	 * to the src/main/resources folder.
	 */
	private static final String IMPL_TEMPLATE = "templates/DataElement.vm";

	private static final String PACKAGE = "package";
	private static final String INTERFACE = "interface";
	private static final String FIELDS = "fields";
	private static final String CLASS = "class";

	@Builder
	public ImplementationWriter(
		String packageName, String interfaceName, String className, Fields fields
	) {
		super();
		this.template = IMPL_TEMPLATE;
		this.context = Map.ofEntries(
			Map.entry(PACKAGE, packageName),
			Map.entry(INTERFACE, interfaceName),
			Map.entry(CLASS, className),
			Map.entry(FIELDS, fields)
		);
	}
}
