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
 * Writer for DataElement Interfaces.
 *
 * @author Daniel Bluhm
 */
public class InterfaceWriter
	extends VelocitySourceWriter
	implements GeneratedFileWriter
{

	/**
	 * Location of Interface template for use with velocity.
	 *
	 * Use of Velocity ClasspathResourceLoader means files are discovered
	 * relative to the src/main/resources folder.
	 */
	private static final String TEMPLATE = "templates/ElementInterface.vm";

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
	 * Fully qualified name of generated interface;
	 */
	private String fqn;


	/**
	 * Constructor
	 *
	 * @param packageName
	 * @param interfaceName
	 * @param fields
	 * @param generatedFile
	 */
	public InterfaceWriter(
		DataElementMetadata data
	) {
		super(TEMPLATE);
		this.fqn = data.getFullyQualifiedName();
		Fields fields = data.getFields().getNonDefaultFields();
		context.put(PACKAGE, data.getPackageName());
		context.put(INTERFACE, data.getName());
		context.put(FIELDS, fields);
		context.put(TYPES, fields.getTypes());
	}

	@Override
	public Writer openWriter(Filer filer) throws IOException {
		return filer.createSourceFile(fqn).openWriter();
	}
}
