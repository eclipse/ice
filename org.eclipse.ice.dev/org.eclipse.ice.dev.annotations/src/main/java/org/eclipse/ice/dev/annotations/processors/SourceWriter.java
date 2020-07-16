package org.eclipse.ice.dev.annotations.processors;

import java.io.Writer;

import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;

/**
 * Abstract base class for classes that render a Java Source file.
 * @author Daniel Bluhm
 */
public abstract class SourceWriter {

	protected String template = "";
	protected Context context;

	/**
	 * Write the Java Source file to the open writer.
	 * @param writer to which the java source will be written
	 */
	public void write(Writer writer) {
		// Make sure Velocity is initialized. Subsequent calls are harmless.
		Velocity.init(VelocityProperties.get());

		// Write template from context.
		Velocity.mergeTemplate(template, "UTF-8", context, writer);
	}
}
