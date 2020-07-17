package org.eclipse.ice.dev.annotations.processors;

import java.util.HashMap;

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
		this.context = new HashMap<>();
		this.context.put(PACKAGE, packageName);
		this.context.put(INTERFACE, interfaceName);
		this.context.put(CLASS, className);
		this.context.put(FIELDS, fields);
	}
}
