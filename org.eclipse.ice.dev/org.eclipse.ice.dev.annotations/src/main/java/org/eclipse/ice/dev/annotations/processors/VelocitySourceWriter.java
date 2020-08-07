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

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import javax.tools.JavaFileObject;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;

/**
 * Abstract base class for classes that render a Java Source file through
 * velocity templates.
 * @author Daniel Bluhm
 */
public abstract class VelocitySourceWriter {

	protected String template;
	protected Map<String, Object> context;
	protected JavaFileObject generatedFile;
	
	public VelocitySourceWriter() {
		this.context = new HashMap<>();
	}
	
	/**
	 * Opens a writer and then proceed to write a source file
	 * @param generatedFile
	 * @throws IOException
	 */
	public void write(JavaFileObject generatedFile) throws IOException {
		try (Writer writer = generatedFile.openWriter()) {
			write(writer);
		}
	}
	
	/**
	 * Write the Java Source file to the open writer.
	 * @param writer to which the java source will be written
	 * @throws IOException 
	 */
	public void write(Writer writer) throws IOException {
		// Make sure Velocity is initialized. Subsequent calls are harmless.
		Velocity.init(VelocityProperties.get());

		// Make velocity context from generic map context.
		Context velocityContext = new VelocityContext(context);
		
		// Write template from context.
		Velocity.mergeTemplate(template, "UTF-8", velocityContext, writer);
	}
	
	
	public void write() throws IOException {
		write(this.generatedFile);
	}
	
	public abstract BiFunction<JavaFileObject, Map, List<VelocitySourceWriter>> getInitializer();
	
}
