package org.eclipse.ice.dev.annotations.processors;

import java.io.Writer;

import lombok.Builder;
import lombok.NonNull;

public class DataElementInterfaceWriter extends InterfaceWriter {
	
	/**
	 * Location of Interface template for use with velocity.
	 *
	 * Use of Velocity ClasspathResourceLoader means files are discovered relative
	 * to the src/main/resources folder.
	 */
	private static final String TEMPLATE = "templates/ElementInterface.vm";
	
	@Builder
	public DataElementInterfaceWriter(String packageName, String interfaceName, @NonNull Fields fields, Writer writer) {
		super(packageName, interfaceName, fields, writer);
		this.template = TEMPLATE;
	}

}
