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

public class DataElementInterfaceWriter extends InterfaceWriter {
	
	/**
	 * Location of Interface template for use with velocity.
	 *
	 * Use of Velocity ClasspathResourceLoader means files are discovered relative
	 * to the src/main/resources folder.
	 */
	private static final String TEMPLATE = "templates/ElementInterface.vm";
	
	@Builder
	public DataElementInterfaceWriter(String packageName, String interfaceName, @NonNull Fields fields, JavaFileObject generatedFile) {
		super(packageName, interfaceName, fields, generatedFile);
		this.template = TEMPLATE;
	}
	
	private DataElementInterfaceWriter() {
		super();
	}

	@Override
	public BiFunction<JavaFileObject, Map, List<VelocitySourceWriter>> getInitializer() {
		return (fileObject, context) -> {
			String name = (String)context.get(MetaTemplateProperty.QUALIFIED);
			return Arrays.asList(DataElementInterfaceWriter.builder()
					.packageName((String)context.get(MetaTemplateProperty.PACKAGE))
					.interfaceName((String)context.get(MetaTemplateProperty.INTERFACE))
					.fields((Fields)context.get(MetaTemplateProperty.FIELDS))
					.generatedFile(fileObject)
					.build());
		};
	}
	
	public static BiFunction<JavaFileObject, Map, List<VelocitySourceWriter>> getContextInitializer() {
		return new DataElementInterfaceWriter().getInitializer();
	}

}
