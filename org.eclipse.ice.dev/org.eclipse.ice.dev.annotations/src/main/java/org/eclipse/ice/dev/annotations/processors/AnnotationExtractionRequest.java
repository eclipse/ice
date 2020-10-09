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

import lombok.Builder;
import lombok.Data;

/**
 * Input POJO for the flavors of the ICEAnnotationExtractionService
 *
 * @author Michael Walsh
 */
@Data
@Builder
public class AnnotationExtractionRequest {
	/**
	 * Element to be extracted from
	 */
	private Element element;

	/**
	 * Included list of static default fields in addition to fields specified in
	 * element
	 */
	@Builder.Default
	private boolean includeDefaults = true;
	/**
	 * Base name for the generated classes
	 */
	private String className;
}
