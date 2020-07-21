/*******************************************************************************
 * Copyright (c) 2020- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Daniel Bluhm - Initial implementation
 *******************************************************************************/

package org.eclipse.ice.dev.annotations.processors;

import lombok.Builder;
import lombok.NonNull;

/**
 * Writer for DataElement Interfaces.
 * @author Daniel Bluhm
 */
public class InterfaceWriter extends VelocitySourceWriter {

	/**
	 * Location of Interface template for use with velocity.
	 *
	 * Use of Velocity ClasspathResourceLoader means files are discovered relative
	 * to the src/main/resources folder.
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

	@Builder
	public InterfaceWriter(
		String packageName, String interfaceName, @NonNull Fields fields
	) {
		super();
		this.template = TEMPLATE;
		context.put(PACKAGE, packageName);
		context.put(INTERFACE, interfaceName);
		context.put(FIELDS, fields);
	}
}
