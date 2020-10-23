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
import lombok.Getter;

/**
 * POJO representing metadata extracted from DataElement and associated
 * annotations.
 *
 * @author Daniel Bluhm
 */
@Getter
@Builder
public class DataElementMetadata {

	/**
	 * Suffix for Implementation name.
	 */
	private static final String IMPL_SUFFIX = "Implementation";

	/**
	 * Base name of classes to be generated.
	 */
	private String name;

	/**
	 * Package of classes to be generated.
	 */
	private String packageName;

	/**
	 * Collected fields of the DataElement.
	 */
	private Fields fields;

	/**
	 * Fully qualified name of this DataElement.
	 * @return fully qualified name.
	 */
	public String getFullyQualifiedName() {
		String value = null;
		if (packageName != null) {
			value = String.format("%s.%s", packageName, name);
		} else {
			value = name;
		}
		return value;
	}

	/**
	 * Name of implementation for this DataElement.
	 * @return Implementation name.
	 */
	public String getImplementationName() {
		return name + IMPL_SUFFIX;
	}

	/**
	 * Fully qualified name of this DataElement's implementation.
	 * @return fully qualified implementation name.
	 */
	public String getFullyQualifiedImplName() {
		return getFullyQualifiedName() + IMPL_SUFFIX;
	}
}