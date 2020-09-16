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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Pojo class used to encapsulate initial data extracted from Spec classes
 *
 * @author Michael Walsh
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpecClassMetadata {
	/**
	 * Base name of classes to be generated
	 */
	private String name;
	/**
	 * Base package name of classes to be generated
	 */
	private String packageName;
	/**
	 * Fully qualified version of the base name
	 */
	private String fullyQualifiedName;
	/**
	 * String name of the collection backing the persistence handler
	 * (see {@link org.eclipse.ice.dev.annotations.Persisted})
	 */
	private String collectionName;
	/**
	 * Collection of pre-existing fields in the Spec class
	 */
	private Fields fields;
}