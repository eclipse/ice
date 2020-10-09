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
 * DataElementPersistenceHandlerWriter
 *
 * @author Michael Walsh
 */
public class DataElementPersistenceHandlerWriter extends PersistenceHandlerWriter {

	/**
	 * Location of PersistenceHandler template for use with velocity.
	 *
	 * Use of Velocity ClasspathResourceLoader means files are discovered relative
	 * to the src/main/resources folder.
	 */
	private static final String PERSISTENCE_HANDLER_TEMPLATE = "templates/PersistenceHandler.vm";

	/**
	 * Constructor
	 *
	 * @param packageName
	 * @param elementInterface
	 * @param className
	 * @param interfaceName
	 * @param implementation
	 * @param collection
	 * @param fields
	 * @param generatedFile
	 */
	@Builder
	public DataElementPersistenceHandlerWriter(
		String packageName, String elementInterface, String className,
		String interfaceName, String implementation, String collection,
		@NonNull Fields fields, Types types, FileObject generatedFile
	) {
		super(
			packageName, elementInterface, className, interfaceName,
			implementation, collection, fields, types, generatedFile
		);
		this.template = PERSISTENCE_HANDLER_TEMPLATE;
	}

	/**
	 * Private argless constructor purely for use of the static method to have
	 * access to the inherited getInitializer() method
	 */
	private DataElementPersistenceHandlerWriter() {
		super();
	}

	/**
	 * Supplies a lambda that will provide a fully initialized
	 * DataElementPersistenceHandlerWriter given a map and a JavaFileObject
	 */
	@Override
	public BiFunction<FileObject, Map, List<SelfInitializingWriter>> getInitializer() {
		return (fileObject, context) -> Arrays.asList(
			DataElementPersistenceHandlerWriter.builder()
				.packageName((String) context.get(MetaTemplateProperty.PACKAGE))
				.className((String) context.get(PersistenceHandlerTemplateProperty.CLASS))
				.types(((Fields) context.get(MetaTemplateProperty.FIELDS)).getTypes())
				.interfaceName((String) context.get(PersistenceHandlerTemplateProperty.INTERFACE))
				.fields((Fields) context.get(MetaTemplateProperty.FIELDS))
				.types(((Fields) context.get(MetaTemplateProperty.FIELDS)).getTypes())
				.elementInterface((String) context.get(PersistenceHandlerTemplateProperty.ELEMENT_INTERFACE))
				.collection((String) context.get(PersistenceHandlerTemplateProperty.COLLECTION))
				.implementation((String) context.get(PersistenceHandlerTemplateProperty.IMPLEMENTATION))
				.generatedFile(fileObject).build()
		);
	}

	/**
	 * Static method for cleanly fetching an initializer
	 *
	 * @return DataElementPersistenceHandlerWriter init lambda
	 */
	public static BiFunction<FileObject, Map, List<SelfInitializingWriter>> getContextInitializer() {
		return new DataElementPersistenceHandlerWriter().getInitializer();
	}
}
