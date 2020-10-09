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

/**
 * Enum of keys mapped to extracted/generated metadata essential to persistence handler 
 * generation
 * 
 * @author Michael Walsh
 *
 */
public enum PersistenceHandlerTemplateProperty implements TemplateProperty {
	/**
	 * String name of the parameterized value in the implemented interface
	 */
	ELEMENT_INTERFACE,
	/**
	 * String name of the collection backing the persistence handler
	 */
	COLLECTION,
	/**
	 * String name of the generated persistence handler class
	 */
	CLASS,
	/**
	 * String name of the class Documents are converted to
	 */
	IMPLEMENTATION,
	/**
	 * Fully qualified name of the persistence handler
	 */
	QUALIFIED,
	/**
	 * String name of the interface that the generated persistence handler will implement
	 */
	INTERFACE;
}
