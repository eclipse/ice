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

import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;

import org.eclipse.ice.dev.annotations.Persisted;

/**
 * Extractor for persistence metadata on Persisted annotated elements.
 * @author Daniel Bluhm
 */
public class PersistenceExtractor
	extends AbstractAnnotationExtractor<PersistenceMetadata> {

	public PersistenceExtractor(Elements elementUtils) {
		super(elementUtils);
	}

	@Override
	public PersistenceMetadata extract(Element element) throws InvalidElementException {
		Persisted persisted = element.getAnnotation(Persisted.class);
		if (persisted == null) {
			throw new InvalidElementException(
				"Persisted annotation not found on element."
			);
		}
		return PersistenceMetadata.builder()
			.collection(persisted.collection())
			.build();
	}
}
