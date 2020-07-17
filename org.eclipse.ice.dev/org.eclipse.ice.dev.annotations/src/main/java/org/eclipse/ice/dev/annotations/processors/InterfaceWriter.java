package org.eclipse.ice.dev.annotations.processors;

import java.util.HashMap;

import lombok.Builder;
import lombok.NonNull;

public class InterfaceWriter extends VelocitySourceWriter {

	/**
	 * Location of Interface template for use with velocity.
	 *
	 * Use of Velocity ClasspathResourceLoader means files are discovered relative
	 * to the src/main/resources folder.
	 */
	private static final String TEMPLATE = "templates/ElementInterface.vm";

	private static final String PACKAGE = "package";
	private static final String INTERFACE = "interface";
	private static final String FIELDS = "fields";

	@Builder
	public InterfaceWriter(
		String packageName, String interfaceName, @NonNull Fields fields
	) {
		super();
		this.template = TEMPLATE;
		this.context = new HashMap<>();
		context.put(PACKAGE, packageName);
		context.put(INTERFACE, interfaceName);
		context.put(FIELDS, fields);
	}
}
