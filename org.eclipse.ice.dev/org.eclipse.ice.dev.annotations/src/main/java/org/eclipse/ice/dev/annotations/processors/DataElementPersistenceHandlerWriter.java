/*******************************************************************************
 * Copyright (c) 2020- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michael Walsh - Initial implementation
 *******************************************************************************/
package org.eclipse.ice.dev.annotations.processors;

import java.io.Writer;

import javax.tools.JavaFileObject;

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
	public DataElementPersistenceHandlerWriter(String packageName, String elementInterface, String className, String interfaceName,
			String implementation, String collection, @NonNull Fields fields, JavaFileObject generatedFile) {
		super(packageName, elementInterface, className, interfaceName, implementation, collection, fields, generatedFile);
		this.template = PERSISTENCE_HANDLER_TEMPLATE;
	}

}
