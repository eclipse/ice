package org.eclipse.ice.dev.annotations.processors;

import java.io.Writer;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class InterfaceWriter implements SourceWriter {

	/**
	 * Location of Interface template for use with velocity.
	 *
	 * Use of Velocity ClasspathResourceLoader means files are discovered relative
	 * to the src/main/resources folder.
	 */
	private static final String INTERFACE_TEMPLATE = "templates/ElementInterface.vm";

	private static final String PACKAGE = "package";
	private static final String INTERFACE = "interface";
	private static final String FIELDS = "fields";

	private String package_;
	private String interface_;
	private Fields fields;

	@Override
	public void write(Writer writer) {
		// Make sure Velocity is initialized. Subsequent calls are harmless.
		Velocity.init(VelocityProperties.get());

		// Prepare context of template
		final VelocityContext context = new VelocityContext();
		context.put(PACKAGE, package_);
		context.put(INTERFACE, interface_);
		context.put(FIELDS, fields);

		Velocity.mergeTemplate(INTERFACE_TEMPLATE, "UTF-8", context, writer);
	}
}
