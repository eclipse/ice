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

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.eclipse.ice.dev.annotations.processors.Field;
import org.eclipse.ice.dev.annotations.processors.Types;
import org.junit.jupiter.api.Test;

class TypesTest {

	@Test
	void testGetShortenedType() {
		assertEquals("boolean", Types.getShortenedType("boolean"));
		assertEquals("String", Types.getShortenedType("java.lang.String"));
		assertEquals("UUID", Types.getShortenedType("java.util.UUID"));
		assertEquals("Entry", Types.getShortenedType("java.util.Map.Entry"));
		assertEquals(
			"Map.Entry<String, Object>",
			Types.getShortenedType("java.util.Map$Entry<String, Object>")
		);
		assertEquals(
			"List<String>",
			Types.getShortenedType("java.util.List<java.lang.String>")
		);
	}

	@Test
	void testGetImports() {
		Types types = new Types(List.of(
			Field.builder()
				.name("test")
				.type(UUID.class)
				.build()
		));
		assertTrue(types.getImports().contains("java.util.UUID"));
		types = new Types(List.of(
			Field.builder()
				.name("test")
				.type(String.class)
				.build()
		));
		Set<String> imports = types.getImports();
		assertFalse(imports.contains("java.lang.String"));
		assertTrue(imports.isEmpty());
		types = new Types(List.of(
			Field.builder()
				.name("test")
				.type("java.util.Map.Entry")
				.build()
		));
		assertTrue(types.getImports().contains("java.util.Map.Entry"));
		types = new Types(List.of(
			Field.builder()
				.name("test")
				.type("java.util.Map.Entry<java.lang.String, java.lang.Object>")
				.build()
		));
		imports = types.getImports();
		assertTrue(imports.contains("java.util.Map.Entry"));
		assertFalse(imports.contains("java.lang.String"));
		assertFalse(imports.contains("java.lang.Object"));
		types = new Types(List.of(
			Field.builder()
				.name("test")
				.type("java.util.List<java.lang.String>")
				.build()
		));
		imports = types.getImports();
		assertTrue(imports.contains("java.util.List"));
		assertFalse(imports.contains("java.lang.String"));
		types = new Types(List.of(
			Field.builder()
				.name("test")
				.type("java.util.Map<java.lang.String, java.util.List<java.util.Map.Entry<java.lang.String, java.lang.String>>>")
				.build()
		));
		imports = types.getImports();
		assertTrue(imports.contains("java.util.Map"));
		assertTrue(imports.contains("java.util.List"));
		assertFalse(imports.contains("java.lang.String"));
	}

	@Test
	void testResolve() {
		Types types = new Types(List.of(
			Field.builder()
				.name("test")
				.type("java.lang.String")
				.build(),
			Field.builder()
				.name("test")
				.type("int")
				.build(),
			Field.builder()
				.name("test")
				.type("java.util.UUID")
				.build(),
			Field.builder()
				.name("test")
				.type("java.util.List<java.lang.String>")
				.build()
		));
		assertEquals("String", types.resolve("java.lang.String"));
		assertEquals("int", types.resolve("int"));
		assertEquals("UUID", types.resolve("java.util.UUID"));
		assertEquals("List<String>", types.resolve("java.util.List<java.lang.String>"));
	}

	@Test
	void testCollisionsHandledCorrectly() {
		Types types = new Types(List.of(
			Field.builder()
				.name("test")
				.type("java.lang.String")
				.build(),
			Field.builder()
				.name("test")
				.type("com.example.String")
				.build()
		));
		Set<String> imports = types.getImports();
		assertFalse(imports.contains("com.example.String"));
		assertEquals("java.lang.String", types.resolve("java.lang.String"));
		assertEquals("com.example.String", types.resolve("com.example.String"));
	}
}