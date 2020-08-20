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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import javax.tools.JavaFileObject;

import org.eclipse.ice.dev.annotations.processors.DataElementImplementationWriter.DataElementImplementationWriterBuilder;

import lombok.Builder;
import lombok.NonNull;

/**
 * Implementation of the VelocitySourceWriter that handles generating the
 * DataElementPersistenceHandlerWriter
 *
 */
public class DataElementPersistenceHandlerWriter extends PersistenceHandlerWriter {

	/**
	 * Location of PersistenceHandler template for use with velocity.
	 *
	 * Use of Velocity ClasspathResourceLoader means files are discovered relative
	 * to the src/main/resources folder.
	 */
	private static final String PERSISTENCE_HANDLER_TEMPLATE = "templates/PersistenceHandler.vm";

	@Builder
	public DataElementPersistenceHandlerWriter(String packageName, String elementInterface, String className,
			String interfaceName, String implementation, String collection, @NonNull Fields fields,
			JavaFileObject generatedFile) {
		super(packageName, elementInterface, className, interfaceName, implementation, collection, fields,
				generatedFile);
		this.template = PERSISTENCE_HANDLER_TEMPLATE;
	}

	private DataElementPersistenceHandlerWriter() {
		super();
	}

	/**
	 * Supplies a lambda that will provide a fully initialized
	 * DataElementPersistenceHandlerWriter given a map and a JavaFileObject
	 */
	@Override
	public BiFunction<JavaFileObject, Map, List<VelocitySourceWriter>> getInitializer() {
		return (fileObject, context) -> {
			String name = (String) context.get(PersistenceHandlerTemplateProperty.QUALIFIED);
			return Arrays.asList(DataElementPersistenceHandlerWriter.builder()
					.packageName((String) context.get(MetaTemplateProperty.PACKAGE))
					.className((String) context.get(PersistenceHandlerTemplateProperty.CLASS))
					.interfaceName((String) context.get(PersistenceHandlerTemplateProperty.INTERFACE))
					.fields((Fields) context.get(MetaTemplateProperty.FIELDS))
					.elementInterface((String) context.get(PersistenceHandlerTemplateProperty.ELEMENT_INTERFACE))
					.collection((String) context.get(PersistenceHandlerTemplateProperty.COLLECTION))
					.implementation((String) context.get(PersistenceHandlerTemplateProperty.IMPLEMENTATION))
					.generatedFile(fileObject).build());
		};
	}

	/**
	 * Static method for cleanly fetching an initializer
	 * 
	 * @return DataElementPersistenceHandlerWriter init lambda
	 */
	public static BiFunction<JavaFileObject, Map, List<VelocitySourceWriter>> getContextInitializer() {
		return new DataElementPersistenceHandlerWriter().getInitializer();
	}

}
