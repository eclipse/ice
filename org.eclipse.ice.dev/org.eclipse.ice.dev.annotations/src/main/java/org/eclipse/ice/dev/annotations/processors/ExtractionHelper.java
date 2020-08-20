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

import java.lang.annotation.Annotation;
import java.util.Optional;

import javax.lang.model.element.Element;

/**
 * Base interface for the flavors of extraction helpers for pulling data from
 * Spec classes
 *
 */
public interface ExtractionHelper {
	/**
	 * Get the AnnotationMirror of a given type if present on the element.
	 * 
	 * @param <T>
	 * @param element
	 * @param cls
	 * @return
	 */
	public default <T extends Annotation> Optional<T> getAnnotation(Element element, Class<T> cls) {
		T value = element.getAnnotation(cls);
		if (value == null) {
			return Optional.empty();
		}
		return Optional.of(value);
	}

}
