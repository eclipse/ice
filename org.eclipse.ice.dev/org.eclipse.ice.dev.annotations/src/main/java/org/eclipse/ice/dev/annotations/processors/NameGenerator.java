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
 */
public interface NameGenerator {

	String getImplName(String name);

	String getQualifiedImplName(String fullyQualifiedName);

	String getQualifiedPersistenceHandlerName(String fullyQualifiedName);

	String getPersistenceHandlerName(String name);

	String extractName(Element element);

	String extractCollectionName(Element element);

	String getPersistenceHandlerInterfaceName();

}
