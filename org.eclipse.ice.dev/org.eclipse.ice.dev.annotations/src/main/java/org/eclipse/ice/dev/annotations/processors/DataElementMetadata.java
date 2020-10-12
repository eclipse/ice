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
import lombok.Data;

/**
 * POJO representing metadata extracted from DataElement and associated
 * annotations.
 *
 * @author Daniel Bluhm
 */
@Data
@Builder
public class DataElementMetadata {
	/**
	 * Base name of classes to be generated.
	 */
	protected String name;

	/**
	 * Package of classes to be generated.
	 */
	protected String packageName;

	/**
	 * Collected fields of the DataElement.
	 */
	protected Fields fields;

	/**
	 * Fully qualified name (package + name) of the DataElement.
	 * @return fully qualified name.
	 */
	public String getFullyQualifiedName() {
		return String.format("%s.%s", this.packageName, this.name);
	}
}
