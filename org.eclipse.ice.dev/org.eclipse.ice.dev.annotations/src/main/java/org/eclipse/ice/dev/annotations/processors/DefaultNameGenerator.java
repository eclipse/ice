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

import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.Persisted;

/**
 * Class that encompasses the initial naming schema if generated files
 * 
 * @author Michael Walsh
 *
 */
public class DefaultNameGenerator implements NameGenerator {

	/**
	 * The value appended to DataElement implementation class names.
	 */
	private static final String IMPL_SUFFIX = "Implementation";

	/**
	 * The value appended to DataElement Persistence Handler class names.
	 */
	private static final String PERSISTENCE_SUFFIX = "PersistenceHandler";

	/**
	 * Used for pulling data from annotations on {@link Element}s
	 */
	private SpecExtractionHelper specExtractionHelper = new SpecExtractionHelper();

	/**
	 * Return the element name as extracted from the DataElement annotation.
	 * 
	 * @return the extracted name
	 */
	@Override
	public String extractName(Element element) {
		return specExtractionHelper.getAnnotation(element, DataElement.class).map(DataElement::name).orElse(null);
	}

	/**
	 * Return the collection name as extracted from the Persisted annotation.
	 * 
	 * @return the extracted collection name
	 */
	@Override
	public String extractCollectionName(Element element) {
		return specExtractionHelper.getAnnotation(element, Persisted.class).map(Persisted::collection).orElse(null);
	}

	/**
	 * Get the name of the Implementation to be generated.
	 * 
	 * @return implementation name
	 */
	@Override
	public String getImplName(String name) {
		return name + IMPL_SUFFIX;
	}

	/**
	 * Get the fully qualified name of the Implementation to be generated.
	 * 
	 * @return fully qualified implementation name
	 */
	@Override
	public String getQualifiedImplName(String fullyQualifiedName) {
		return fullyQualifiedName + IMPL_SUFFIX;
	}

	/**
	 * Get the name of the Persistence Handler to be generated.
	 * 
	 * @return persistence handler name
	 */
	@Override
	public String getPersistenceHandlerName(String name) {
		return name + PERSISTENCE_SUFFIX;
	}

	/**
	 * Generate the fully qualified name for a persistence handler
	 * 
	 * @param fullyQualifiedName
	 * @return fullyQualifiedName for a persistence handler
	 */
	@Override
	public String getQualifiedPersistenceHandlerName(String fullyQualifiedName) {
		return fullyQualifiedName + PERSISTENCE_SUFFIX;
	}

	/**
	 * Get the interface name of the persistence handler.
	 *
	 * Right now, this is simply the IPersistenceHandler interface. In the future
	 * this will be a data element specific interface.
	 * 
	 * @return Interface name of the Persistence Handler
	 */
	@Override
	public String getPersistenceHandlerInterfaceName() {
		return org.eclipse.ice.data.IPersistenceHandler.class.getSimpleName();
	}
}
