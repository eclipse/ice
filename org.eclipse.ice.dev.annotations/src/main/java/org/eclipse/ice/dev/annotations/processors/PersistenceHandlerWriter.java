package org.eclipse.ice.dev.annotations.processors;

import java.io.Writer;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PersistenceHandlerWriter implements SourceWriter {

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

	private Object package_;
	private Object elementInterface;
	private Object class_;
	private Object implementation;
	private Object collection;
	private Object fields;

	@Override
	public void write(Writer writer) {
		// Make sure Velocity is initialized. Subsequent calls are harmless.
		Velocity.init(VelocityProperties.get());

		// Prepare context of template
		final VelocityContext context = new VelocityContext();
		context.put(PACKAGE, package_);
		context.put(ELEMENT_INTERFACE, elementInterface);
		context.put(CLASS, class_);
		context.put(COLLECTION, collection);
		context.put(IMPLEMENTATION, implementation);
		context.put(FIELDS, fields);

		Velocity.mergeTemplate(PERSISTENCE_HANDLER_TEMPLATE, "UTF-8", context, writer);

	}

}
