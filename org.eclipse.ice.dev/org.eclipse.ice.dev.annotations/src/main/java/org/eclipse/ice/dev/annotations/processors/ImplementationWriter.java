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
 * Writer for DataElement Implementation classes.
 *
 * @author Daniel Bluhm
 */
public class ImplementationWriter
	extends VelocitySourceWriter
	implements GeneratedFileWriter
{

	/**
	 * Location of DataElement template for use with velocity.
	 */
	private static final String IMPL_TEMPLATE = "templates/DataElement.vm";

	/**
	 * Context key for package.
	 */
	private static final String PACKAGE = "package";

	/**
	 * Context key for interface.
	 */
	private static final String INTERFACE = "interface";

	/**
	 * Context key for fields.
	 */
	private static final String FIELDS = "fields";

	/**
	 * Context key for types.
	 */
	private static final String TYPES = "types";

	/**
	 * Context key for class.
	 */
	private static final String CLASS = "class";

	/**
	 * Fully qualified name of class to be generated.
	 */
	private String fqn;

		/**
	 * Constructor
	 *
	 * @param packageName
	 * @param interfaceName
	 * @param className
	 * @param fields
	 * @param generatedFile
	 */
	public ImplementationWriter(DataElementMetadata data) {
		super(IMPL_TEMPLATE);
		this.fqn = data.getFullyQualifiedImplName();
		this.context.put(PACKAGE, data.getPackageName());
		this.context.put(INTERFACE, data.getName());
		this.context.put(CLASS, data.getImplementationName());
		this.context.put(FIELDS, data.getFields());
		this.context.put(TYPES, data.getFields().getTypes());
	}

	@Override
	public Writer openWriter(Filer filer) throws IOException {
		return filer.createSourceFile(fqn).openWriter();
	}
}
