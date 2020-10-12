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
		return List.of(
			PersistenceHandlerWriter.builder()
				.packageName(dataElement.getPackageName())
				.elementInterface(dataElement.getName())
				.className(dataElement.getName() + "PersistenceHandler")
				// TODO Just move interface name into template (it's static)
				.interfaceName("IPersistenceHandler")
				.implementation(dataElement.getName()+ "Implementation")
				.collection(persistence.getCollection())
				.fields(dataElement.getFields())
				.types(dataElement.getFields().getTypes())
				.build()
		);
	}

}
