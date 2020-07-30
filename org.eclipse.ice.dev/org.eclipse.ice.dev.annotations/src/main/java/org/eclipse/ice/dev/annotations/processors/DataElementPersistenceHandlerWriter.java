package org.eclipse.ice.dev.annotations.processors;

import java.io.Writer;

import org.eclipse.ice.dev.annotations.processors.DataElementImplementationWriter.DataElementImplementationWriterBuilder;

import lombok.Builder;
import lombok.NonNull;

public class DataElementPersistenceHandlerWriter extends PersistenceHandlerWriter {
	
	/**
	 * Location of PersistenceHandler template for use with velocity.
	 *
	 * Use of Velocity ClasspathResourceLoader means files are discovered relative
	 * to the src/main/resources folder.
	 */
	private static final String PERSISTENCE_HANDLER_TEMPLATE = "templates/PersistenceHandler.vm";

	@Builder
	public DataElementPersistenceHandlerWriter(String packageName, String elementInterface, String className,
			String implementation, String collection, @NonNull Fields fields, Writer writer) {
		super(packageName, elementInterface, className, implementation, collection, fields, writer);
		this.template = PERSISTENCE_HANDLER_TEMPLATE;
	}

}
