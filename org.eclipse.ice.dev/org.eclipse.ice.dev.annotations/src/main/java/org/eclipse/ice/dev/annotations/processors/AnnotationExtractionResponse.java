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

import java.util.Map;

import org.eclipse.ice.dev.annotations.processors.AnnotationExtractionRequest.AnnotationExtractionRequestBuilder;

import lombok.Builder;
import lombok.Data;

/**
 * Typical response from the flavors of ICEAnnotationExtractionService
 *
 */
@Data
@Builder
public class AnnotationExtractionResponse {
	/**
	 * Pojo containing metadata about fields to be included in Velocity generated
	 * classes
	 */
	private Fields fields;
	/**
	 * Map containing metadata surrounding the class types to be generated, e.g.
	 * package name
	 */
	private Map<TemplateProperty, Object> classMetadata;
}
