/*******************************************************************************
 * Copyright (c) 2020- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Daniel Bluhm - Initial implementation
 *******************************************************************************/

package org.eclipse.ice.dev.annotations.processors;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.ToolContext;
import org.apache.velocity.tools.ToolManager;

/**
 * Abstract base class for classes that render a Java Source file through
 * velocity templates.
 * 
 * @author Daniel Bluhm
 */
public abstract class VelocitySourceWriter implements FileWriter {

	/**
	 * Tool configuration file.
	 */
	private static final String TOOL_CONFIG = "tools.xml";

	/**
	 * Velocity Engine.
	 */
	private static VelocityEngine engine = configureVelocityEngine();

	/**
	 * Velocity Tools.
	 */
	private static ToolManager tools = configureTools();

	/**
	 * Set up the velocity engine with properties.
	 * @return initialized VelocityEngine
	 */
	private static VelocityEngine configureVelocityEngine() {
		VelocityEngine engine = new VelocityEngine();
		engine.init(VelocityProperties.get());
		return engine;
	}

	/**
	 * Set up the velocity tool manager.
	 * @return
	 */
	private static ToolManager configureTools() {
		// autoConfigure = false, includeDefaults = true
		ToolManager tools = new ToolManager(false, true);
		tools.configure(TOOL_CONFIG);
		return tools;
	}

	/**
	 * Template for writing. Should be filled by concrete classes.
	 */
	protected String template;

	/**
	 * Context for template. Should be filled by concrete classes.
	 */
	protected Map<String, Object> context;

	public VelocitySourceWriter() {
		this.context = new HashMap<>();
	}

	/**
	 * Write the Java Source file to the open writer.
	 * 
	 * @param writer to which the java source will be written
	 */
	public void write(Writer writer) {
		if (template == null || template.isEmpty()) {
			throw new IllegalStateException("template must be set by concrete VelocitySourceWriter.");
		}

		// Make tool context (subclass of velocity context) from generic map
		// context. This places all tools into the template context.
		ToolContext velocityContext = tools.createContext();
		// Put all our values into the template context.
		velocityContext.putAll(context);

		// Write template from context.
		engine.mergeTemplate(template, "UTF-8", velocityContext, writer);
	}
}