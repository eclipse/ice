package org.eclipse.ice.dev.annotations.processors;

import java.io.Writer;

import org.eclipse.ice.dev.annotations.processors.DataElementInterfaceWriter.DataElementInterfaceWriterBuilder;

import lombok.Builder;
import lombok.NonNull;

public class DataElementImplementationWriter extends ImplementationWriter {

	/**
	 * Location of DataElement template for use with velocity.
	 *
	 * Use of Velocity ClasspathResourceLoader means files are discovered relative
	 * to the src/main/resources folder.
	 */
	private static final String IMPL_TEMPLATE = "templates/DataElement.vm";
	
	@Builder
	public DataElementImplementationWriter(String packageName, String interfaceName, String className, Fields fields, Writer writer) {
		super(packageName, interfaceName, className, fields, writer);
		this.template = IMPL_TEMPLATE;
	}

}
