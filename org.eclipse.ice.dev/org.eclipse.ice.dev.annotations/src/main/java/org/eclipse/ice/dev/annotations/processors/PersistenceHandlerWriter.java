/*******************************************************************************
 * Copyright (c) 2020- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Daniel Bluhm - Initial implementation
 *    Michael Walsh - Modifications
 *******************************************************************************/

package org.eclipse.ice.dev.annotations.processors;

import java.io.IOException;
import java.io.Writer;

import javax.annotation.processing.Filer;

/**
 * Writer for DataElement Persistence classes.
 *
 * @author Daniel Bluhm
 */
public class PersistenceHandlerWriter
	extends VelocitySourceWriter
	implements GeneratedFileWriter
{

	/**
	 * Context key for package.
	 */
	private static final String PACKAGE = "package";

	/**
	 * Context key for element interface.
	 */
	private static final String ELEMENT_INTERFACE = "elementInterface";

	/**
	 * Context key for class
	 */
	private static final String CLASS = "class";

	/**
	 * Context key for collection.
	 */
	private static final String COLLECTION = "collection";

	/**
	 * Context key for implementation.
	 */
	private static final String IMPLEMENTATION = "implementation";

	/**
	 * Context key for fields.
	 */
	private static final String FIELDS = "fields";

	/**
	 * Context key for types.
	 */
	private static final String TYPES = "types";

	/**
	 * Path to template of persistence handler.
	 */
	private static final String TEMPLATE = "templates/PersistenceHandler.vm";

	/**
	 * Fully qualified name of the class for file output.
	 */
	private String fqn;

	/**
	 * Create instance of persistence handler writer from metadata.
	 * @param dataElement DataElementMetadata
	 * @param persistence PersistenceMetadata
	 */
	public PersistenceHandlerWriter(
		DataElementMetadata dataElement,
		PersistenceMetadata persistence
	) {
		super(TEMPLATE);
		this.fqn = persistence.getHandlerName(
			dataElement.getFullyQualifiedName()
		);
		this.context.put(PACKAGE, dataElement.getPackageName());
		this.context.put(ELEMENT_INTERFACE, dataElement.getName());
		this.context.put(CLASS, persistence.getHandlerName(dataElement.getName()));
		this.context.put(COLLECTION, persistence.getCollection());
		this.context.put(IMPLEMENTATION, dataElement.getImplementationName());
		this.context.put(FIELDS, dataElement.getFields());
		this.context.put(TYPES, dataElement.getFields().getTypes());
	}

	@Override
	public Writer openWriter(Filer filer) throws IOException {
		return filer.createSourceFile(fqn).openWriter();
	}
}
