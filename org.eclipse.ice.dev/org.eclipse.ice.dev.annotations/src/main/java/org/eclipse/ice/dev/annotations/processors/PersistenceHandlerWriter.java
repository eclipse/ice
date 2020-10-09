/*******************************************************************************
 * Copyright (c) 2020- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Daniel Bluhm - Initial implementation
 *    Michael Walsh
 *******************************************************************************/

package org.eclipse.ice.dev.annotations.processors;

import javax.tools.JavaFileObject;

import lombok.NonNull;

/**
 * Writer for DataElement Persistence classes.
 * 
 * @author Daniel Bluhm
 */
public abstract class PersistenceHandlerWriter extends VelocitySourceWriter {

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
	 * Context key for interface of PersistenceHandlers
	 */
	private static final String INTERFACE = "interface";

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
	public PersistenceHandlerWriter(String packageName, String elementInterface, String className, String interfaceName,
			String implementation, String collection, @NonNull Fields fields, @NonNull Types types, JavaFileObject generatedFile) {
		super();
		this.context.put(PACKAGE, packageName);
		this.context.put(ELEMENT_INTERFACE, elementInterface);
		this.context.put(CLASS, className);
		this.context.put(INTERFACE, interfaceName);
		this.context.put(COLLECTION, collection);
		this.context.put(IMPLEMENTATION, implementation);
		this.context.put(FIELDS, fields);
		this.context.put(TYPES, types);
		this.generatedFile = generatedFile;
	}

	protected PersistenceHandlerWriter() {

	}

}
