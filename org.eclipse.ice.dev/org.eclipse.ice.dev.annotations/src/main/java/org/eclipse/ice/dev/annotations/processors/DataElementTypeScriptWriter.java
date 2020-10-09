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

import javax.tools.FileObject;
import javax.tools.JavaFileObject;

import lombok.Builder;
import lombok.NonNull;

/**
 * Implementation of the VelocitySourceWriter that handles generating the
 * DataElementImplementation
 * 
 * @author Daniel Bluhm
 */
public class DataElementTypeScriptWriter extends TypeScriptWriter {

	/**
	 * Template used for this writer.
	 */
	private static final String TEMPLATE = "templates/TypeScript.vm";

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
	public DataElementTypeScriptWriter(
		String name, @NonNull Fields fields, @NonNull Types types,
		FileObject generatedFile
	) {
		super(name, fields, types, generatedFile);
		this.template = TEMPLATE;
	}

	/**
	 * Private argless constructor purely for use of the static method to have
	 * access to the inherited getInitializer() method
	 */
	private DataElementTypeScriptWriter() {
		super();
	}

	/**
	 * Supplies a lambda that will provide a fully initialized
	 * DataElementImplementationWriter given a map and a JavaFileObject
	 */
	@Override
	public BiFunction<FileObject, Map, List<VelocitySourceWriter>> getInitializer() {
		return (fileObject, context) -> {
			Fields trimmed = ((Fields) context.get(MetaTemplateProperty.FIELDS)).getNonDefaultFields();
			return Arrays.asList(
				DataElementTypeScriptWriter.builder()
				.name((String) context.get(MetaTemplateProperty.CLASS))
				.fields(trimmed)
				.types(trimmed.getTypes())
				.generatedFile(fileObject).build()
			);
		};
	}

	/**
	 * Static method for cleanly fetching an initializer
	 * 
	 * @return DataElementTypeScriptWriter init lambda
	 */
	public static BiFunction<FileObject, Map, List<VelocitySourceWriter>> getContextInitializer() {
		return new DataElementTypeScriptWriter().getInitializer();
	}

}
