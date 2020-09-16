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

import lombok.Builder;
import lombok.NonNull;

/**
 * Implementation of the VelocitySourceWriter that handles generating the
 * DataElementInterfaceWriter
 * 
 * @author Michael Walsh
 */
public class DataElementInterfaceWriter extends InterfaceWriter {

	/**
	 * Location of Interface template for use with velocity.
	 *
	 * Use of Velocity ClasspathResourceLoader means files are discovered relative
	 * to the src/main/resources folder.
	 */
	private static final String TEMPLATE = "templates/ElementInterface.vm";

	/**
	 * Constructor
	 * 
	 * @param packageName
	 * @param interfaceName
	 * @param fields
	 * @param generatedFile
	 */
	@Builder
	public DataElementInterfaceWriter(String packageName, String interfaceName, @NonNull Fields fields,
			JavaFileObject generatedFile) {
		super(packageName, interfaceName, fields, generatedFile);
		this.template = TEMPLATE;
	}

	/**
	 * Private argless constructor purely for use of the static method to have
	 * access to the inherited getInitializer() method
	 */
	private DataElementInterfaceWriter() {
		super();
	}

	/**
	 * Supplies a lambda that will provide a fully initialized
	 * DataElementInterfaceWriter given a map and a JavaFileObject
	 */
	@Override
	public BiFunction<JavaFileObject, Map, List<VelocitySourceWriter>> getInitializer() {
		return (fileObject, context) -> {
			String name = (String) context.get(MetaTemplateProperty.QUALIFIED);
			return Arrays.asList(DataElementInterfaceWriter.builder()
					.packageName((String) context.get(MetaTemplateProperty.PACKAGE))
					.interfaceName((String) context.get(MetaTemplateProperty.INTERFACE))
					.fields((Fields) context.get(MetaTemplateProperty.FIELDS)).generatedFile(fileObject).build());
		};
	}

	/**
	 * Static method for cleanly fetching an initializer
	 * 
	 * @return DataElementInterfaceWriter init lambda
	 */
	public static BiFunction<JavaFileObject, Map, List<VelocitySourceWriter>> getContextInitializer() {
		return new DataElementInterfaceWriter().getInitializer();
	}

}
