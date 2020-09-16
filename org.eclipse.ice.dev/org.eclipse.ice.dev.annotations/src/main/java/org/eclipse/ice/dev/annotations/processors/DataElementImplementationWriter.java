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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import javax.tools.JavaFileObject;

import org.eclipse.ice.dev.annotations.processors.DataElementInterfaceWriter.DataElementInterfaceWriterBuilder;

import lombok.Builder;

/**
 * Implementation of the VelocitySourceWriter that handles generating the
 * DataElementImplementation
 * 
 * @author Michael Walsh
 */
public class DataElementImplementationWriter extends ImplementationWriter {

	/**
	 * Location of DataElement template for use with velocity.
	 *
	 * Use of Velocity ClasspathResourceLoader means files are discovered relative
	 * to the src/main/resources folder.
	 */
	private static final String IMPL_TEMPLATE = "templates/DataElement.vm";

	/**
	 * Constructor
	 * 
	 * @param packageName
	 * @param interfaceName
	 * @param className
	 * @param fields
	 * @param generatedFile
	 */
	@Builder
	public DataElementImplementationWriter(String packageName, String interfaceName, String className, Fields fields,
			JavaFileObject generatedFile) {
		super(packageName, interfaceName, className, fields, generatedFile);
		this.template = IMPL_TEMPLATE;
	}

	/**
	 * Private argless constructor purely for use of the static method to have
	 * access to the inherited getInitializer() method
	 */
	private DataElementImplementationWriter() {
		super();
	}

	/**
	 * Supplies a lambda that will provide a fully initialized
	 * DataElementImplementationWriter given a map and a JavaFileObject
	 */
	@Override
	public BiFunction<JavaFileObject, Map, List<VelocitySourceWriter>> getInitializer() {
		return (fileObject, context) -> {
			String name = (String) context.get(MetaTemplateProperty.QUALIFIEDIMPL);
			return Arrays.asList(DataElementImplementationWriter.builder()
					.packageName((String) context.get(MetaTemplateProperty.PACKAGE))
					.interfaceName((String) context.get(MetaTemplateProperty.INTERFACE))
					.className((String) context.get(MetaTemplateProperty.CLASS))
					.fields((Fields) context.get(MetaTemplateProperty.FIELDS)).generatedFile(fileObject).build());
		};
	}

	/**
	 * Static method for cleanly fetching an initializer
	 * 
	 * @return DataElementImplementationWriter init lambda
	 */
	public static BiFunction<JavaFileObject, Map, List<VelocitySourceWriter>> getContextInitializer() {
		return new DataElementImplementationWriter().getInitializer();
	}

}
