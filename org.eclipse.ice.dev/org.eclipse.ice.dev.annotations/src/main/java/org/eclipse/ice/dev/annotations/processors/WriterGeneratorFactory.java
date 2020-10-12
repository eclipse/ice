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

import java.util.Optional;
import java.util.Set;

/**
 * Factory for WriterGenerators. Create method parameters represent dependencies
 * of a set of generators.
 * @author Daniel Bluhm
 */
public class WriterGeneratorFactory {
	private WriterGeneratorFactory() {}

	/**
	 * Create WriterGenerators that depend on only DataElementMetadata.
	 * @param dataElement DataElementMetadata extracted from element.
	 * @return set of writer generators for passed data.
	 */
	public static Set<WriterGenerator> create(
		DataElementMetadata dataElement
	) {
		return Set.of(new DataElementWriterGenerator(dataElement));
	}

	/**
	 * Create WriterGenerators that depend on DataElementMetadata and
	 * potentially PersistenceMetadata.
	 * @param dataElement DataElementMetadata extracted from element.
	 * @param persistence PersisteneMetadata extracted from element.
	 * @return set of writer generators for passed data.
	 */
	public static Set<WriterGenerator> create(
		DataElementMetadata dataElement,
		Optional<PersistenceMetadata> persistence
	) {
		Set<WriterGenerator> value = null;
		if (persistence.isEmpty()) {
			value = create(dataElement);
		} else {
			value = Set.of(
				new DataElementWriterGenerator(dataElement),
				new PersistenceWriterGenerator(dataElement, persistence.get())
			);
		}
		return value;
	}
}