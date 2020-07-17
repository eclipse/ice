package org.eclipse.ice.dev.annotations.processors;

import java.io.Writer;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;

/**
 * Abstract base class for classes that render a Java Source file.
 * @author Daniel Bluhm
 */
public abstract class VelocitySourceWriter {

	protected String template;
	protected Map<String, Object> context;

	/**
	 * Write the Java Source file to the open writer.
	 * @param writer to which the java source will be written
	 */
	public void write(Writer writer) {
		// Make sure Velocity is initialized. Subsequent calls are harmless.
		Velocity.init(VelocityProperties.get());

		// Make velocity context from generic map context.
		Context velocityContext = new VelocityContext(context);

		// Write template from context.
		Velocity.mergeTemplate(template, "UTF-8", velocityContext, writer);
	}
}
