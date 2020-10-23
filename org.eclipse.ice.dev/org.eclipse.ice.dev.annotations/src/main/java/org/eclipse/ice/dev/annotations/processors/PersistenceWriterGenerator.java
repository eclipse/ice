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

import java.util.List;

import lombok.AllArgsConstructor;

/**
 * WriterGenerator for Persistence related generated file writers.
 * @author Daniel Bluhm
 */
@AllArgsConstructor
public class PersistenceWriterGenerator implements WriterGenerator {

	/**
	 * DataElement Metadata.
	 */
	private DataElementMetadata dataElement;

	/**
	 * Persistence Metadata.
	 */
	private PersistenceMetadata persistence;

	@Override
	public List<GeneratedFileWriter> generate() {
		List<GeneratedFileWriter> writers = List.of(
			new PersistenceHandlerWriter(dataElement, persistence)
		);
		return writers;
	}
}