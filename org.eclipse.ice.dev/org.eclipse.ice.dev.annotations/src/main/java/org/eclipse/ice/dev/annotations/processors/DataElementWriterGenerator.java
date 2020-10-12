/*******************************************************************************
 * Copyright (c) 2020- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michael Walsh - Initial implementation
 *    Daniel Bluhm - Modifications
 *******************************************************************************/
package org.eclipse.ice.dev.annotations.processors;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.AllArgsConstructor;

/**
 * Uses metadata extracted from spec classes annotated with @DataElement to
 * generate the interface, implementation, and persistence handler.
 *
 */
@AllArgsConstructor
public class DataElementWriterGenerator implements WriterGenerator {
	
	/**
	 * Logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(DataElementWriterGenerator.class);

	/**
	 * Data from which FileWriters are generated.
	 */
	private DataElementMetadata data;

	@Override
	public List<GeneratedFileWriter> generate() {
		List<GeneratedFileWriter> writers = new ArrayList<>();
		Fields nonDefaults = data.getFields().getNonDefaultFields();
		writers.add(InterfaceWriter.builder()
			.packageName(data.getPackageName())
			.interfaceName(data.getName())
			.fields(nonDefaults)
			.types(nonDefaults.getTypes())
			.build());
		writers.add(ImplementationWriter.builder()
			.packageName(data.getPackageName())
			.interfaceName(data.getName())
			.className(data.getName() + "Implementation")
			.fields(data.getFields())
			.types(data.getFields().getTypes())
			.build());
		try {
			writers.add(TypeScriptWriter.builder()
				.name(data.getName())
				.fields(nonDefaults)
				.types(nonDefaults.getTypes())
				.build());
		} catch (UnsupportedOperationException e) {
			logger.warn("Failed to create typescript writer for element:", e);
		}

		return writers;
	}

}
