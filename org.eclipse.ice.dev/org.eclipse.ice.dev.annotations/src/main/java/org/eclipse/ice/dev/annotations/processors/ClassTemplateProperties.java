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
import lombok.Getter;

/**
 * Class metadata property names used in Velocity templating and the ICEExtractionService
 *
 */
class ClassTemplateProperties {

	@AllArgsConstructor
	public enum Meta {
		PACKAGE("package"),
		FIELDS("fields"),
		INTERFACE("interface"),
		CLASS("class"),
		QUALIFIEDIMPL("fullyQualifiedImplementationName"),
		QUALIFIED("fullyQualifiedName");
		@Getter private String key;
	}
	
	@AllArgsConstructor
	public enum PersistenceHandler {
		ELEMENT_INTERFACE("elementInterface"),
		COLLECTION("collection"),
		CLASS("persistenceClassName"),
		IMPLEMENTATION("implementation"),
		QUALIFIED("fullyQualifiedPersistenceHandler"),
		INTERFACE("persistenceInterfaceName");
		
		@Getter private String key;
	}
	
}
