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
	public DataElementImplementationWriter(String packageName, String interfaceName, String className, Fields fields, JavaFileObject generatedFile) {
		super(packageName, interfaceName, className, fields, generatedFile);
		this.template = IMPL_TEMPLATE;
	}

}
