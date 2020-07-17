package org.eclipse.ice.dev.annotations.processors;

import java.util.Map;

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
		this.context = Map.ofEntries(
			Map.entry(PACKAGE, packageName),
			Map.entry(INTERFACE, interfaceName),
			Map.entry(FIELDS, fields)
		);
	}
}
