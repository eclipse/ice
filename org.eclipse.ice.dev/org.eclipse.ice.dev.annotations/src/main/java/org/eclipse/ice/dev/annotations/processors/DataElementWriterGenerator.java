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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Uses metadata extracted from spec classes annotated with @DataElement to
 * generate the interface, implementation, and persistence handler.
 *
 */
public class DataElementWriterGenerator
	implements WriterGenerator<AnnotationExtractionResponse>
{

	@Override
	public List<GeneratedFileWriter> generate(AnnotationExtractionResponse response) {
		List<GeneratedFileWriter> writers = new ArrayList<>();
		writers.add(InterfaceWriter.fromContext(response.getClassMetadata()));
		writers.add(ImplementationWriter.fromContext(response.getClassMetadata()));
		writers.add(TypeScriptWriter.fromContext(response.getClassMetadata()));
		// TODO This check should be more graceful or happen elsewhere
		if (response.getClassMetadata().get(PersistenceHandlerTemplateProperty.COLLECTION) != null) {
			writers.add(PersistenceHandlerWriter.fromContext(response.getClassMetadata()));
		}
		return writers
			.stream()
			.filter(Objects::nonNull)
			.collect(Collectors.toList());
	}
}
