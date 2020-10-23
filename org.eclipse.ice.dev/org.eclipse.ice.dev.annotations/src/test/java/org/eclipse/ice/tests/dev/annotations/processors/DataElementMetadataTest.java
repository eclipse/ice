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

package org.eclipse.ice.tests.dev.annotations.processors;

import org.eclipse.ice.dev.annotations.processors.DataElementMetadata;
import org.eclipse.ice.dev.annotations.processors.Field;
import org.eclipse.ice.dev.annotations.processors.Fields;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

class DataElementMetadataTest {

	@Test
	void testSerialization() throws JsonProcessingException {
		DataElementMetadata pojo = DataElementMetadata.builder()
			.packageName("testpackage")
			.name("TestElement")
			.fields(new Fields(List.of(
				Field.builder()
					.name("test")
					.type(String.class)
					.docString("Test docs.")
					.match(false)
					.primitive(true)
					.nullable(true)
					.build()
			))).build();

		ObjectMapper mapper = new ObjectMapper();
		String pojoJson = mapper.writeValueAsString(pojo);
		System.out.println(pojoJson);
		DataElementMetadata rehydratedPojo = mapper.readValue(pojoJson, DataElementMetadata.class);
		assertEquals(pojo, rehydratedPojo);
	}

	@Test
	void testImplementation() {
		DataElementMetadata pojo = DataElementMetadata.builder()
			.packageName("testpackage")
			.name("TestElement")
			.fields(new Fields(List.of(
				Field.builder()
					.name("test")
					.type(String.class)
					.docString("Test docs.")
					.match(false)
					.primitive(true)
					.nullable(true)
					.getter(false)
					.setter(false)
					.build()
			))).build();

		assertEquals("TestElementImplementation", pojo.getImplementationName());
	}
}