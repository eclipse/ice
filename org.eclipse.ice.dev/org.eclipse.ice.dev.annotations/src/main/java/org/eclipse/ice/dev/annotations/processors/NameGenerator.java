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

import javax.lang.model.element.Element;

/**
 * Interface for helper classes that generate the various names and metadata for
 * class generation
 * 
 * @author Michael Walsh
 */
public interface NameGenerator {

	/**
	 * Generate class implementation name
	 * @param name
	 * @return implName
	 */
	String getImplName(String name);

	/**
	 * Generate full qualified implementation name
	 * @param fullyQualifiedName
	 * @return qualifiedImplName
	 */
	String getQualifiedImplName(String fullyQualifiedName);

	/**
	 * Generate fully qualified persistence handler name
	 * @param fullyQualifiedName
	 * @return qualifiedPersistenceHandlerName
	 */
	String getQualifiedPersistenceHandlerName(String fullyQualifiedName);

	/**
	 * Generate persistence handler name
	 * @param name
	 * @return persistenceHandlerName
	 */
	String getPersistenceHandlerName(String name);

	/**
	 * Extract class base name from element
	 * @param element
	 * @return name
	 */
	String extractName(Element element);

	/**
	 * Extract persistence collection name from @Peristed annotated elements
	 * @param element
	 * @return collectionName
	 */
	String extractCollectionName(Element element);

	/**
	 * Generate interface name that the persistence handler implements
	 * @return persistenceHandlerInterfaceName
	 */
	String getPersistenceHandlerInterfaceName();

}
