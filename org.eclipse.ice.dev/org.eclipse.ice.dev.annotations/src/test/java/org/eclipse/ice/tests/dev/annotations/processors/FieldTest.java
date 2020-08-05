/**
 *
 */
package org.eclipse.ice.tests.dev.annotations.processors;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.eclipse.ice.dev.annotations.processors.Field;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test Field class.
 * @author Daniel Bluhm
 */
class FieldTest {

	private final ObjectMapper mapper = new ObjectMapper();

	/**
	 * Test serialization and deserialization of Fields.
	 * @throws JsonProcessingException
	 */
	@Test
	void testSerialization() throws JsonProcessingException {
		Field f = Field.builder()
			.name("test")
			.defaultValue("test value")
			.type(String.class)
			.docString("Test docs.")
			.match(false)
			.primitive(true)
			.nullable(true)
			.getter(false)
			.setter(false)
			.build();

		String fJson = mapper.writeValueAsString(f);
		Field f2 = mapper.readValue(fJson, Field.class);

		assertEquals(f, f2);
	}

	/**
	 * Test getGetterName.
	 */
	@Test
	void testGetterName() {
		Field f = Field.builder()
			.name("test")
			.type(String.class)
			.build();
		assertEquals("getTest", f.getGetterName());
		f = Field.builder()
			.name("test")
			.type(boolean.class)
			.build();
		assertEquals("isTest", f.getGetterName());
		f = Field.builder()
			.name("TEST")
			.type(String.class)
			.build();
		assertEquals("getTEST", f.getGetterName());
	}

	/**
	 * Test our handling of differences between the field name and the var name.
	 */
	@Test
	void testNameAndVarDifferences() {
		Field f = Field.builder()
			.name("UUID")
			.type(UUID.class)
			.varName("privateId")
			.build();
		assertTrue(f.isVarNameDifferent());
		assertEquals("privateId", f.getVarName());
		f = Field.builder()
			.name("test")
			.getter(false)
			.build();
		assertEquals("test", f.getVarName());
		assertFalse(f.isVarNameDifferent());
	}

	/**
	 * Test getShortType.
	 */
	@Test
	void testGetShortType() {
		Field f = Field.builder()
			.name("test")
			.type(String.class)
			.build();
		assertEquals("String", f.getShortType());
		f = Field.builder()
			.name("test")
			.type(boolean.class)
			.build();
		assertEquals("boolean", f.getShortType());
		f = Field.builder()
			.name("test")
			.type(UUID.class)
			.build();
		assertEquals("UUID", f.getShortType());
		f = Field.builder()
			.name("test")
			.type(Map.Entry.class)
			.build();
		assertEquals("Map.Entry", f.getShortType());
		f = Field.builder()
			.name("test")
			.type("java.util.Map$Entry<String, Object>")
			.build();
		assertEquals("Map.Entry<String,Object>", f.getShortType());
	}

	/**
	 * Test requiresImport.
	 */
	@Test
	void testRequiresImport() {
		Field f = Field.builder()
			.name("test")
			.type(String.class)
			.build();
		assertFalse(f.requiresImports());
		f = Field.builder()
			.name("test")
			.type(boolean.class)
			.build();
		assertFalse(f.requiresImports());
		f = Field.builder()
			.name("test")
			.type(UUID.class)
			.build();
		assertTrue(f.requiresImports());
	}

	/**
	 * Test getImports.
	 */
	@Test
	void testGetImports() {
		Field f = Field.builder()
			.name("test")
			.type(UUID.class)
			.build();
		assertTrue(f.getImports().contains("java.util.UUID"));
		f = Field.builder()
			.name("test")
			.type(String.class)
			.build();
		Set<String> imports = f.getImports();
		assertFalse(imports.contains("java.lang.String"));
		assertTrue(imports.isEmpty());
		f = Field.builder()
			.name("test")
			.type(Map.Entry.class)
			.build();
		assertTrue(f.getImports().contains("java.util.Map"));
		f = Field.builder()
			.name("test")
			.type("java.util.Map$Entry<java.lang.String, java.lang.Object>")
			.build();
		imports = f.getImports();
		assertTrue(imports.contains("java.util.Map"));
		assertFalse(imports.contains("java.lang.String"));
		assertFalse(imports.contains("java.lang.Object"));
		f = Field.builder()
			.name("test")
			.type("java.util.List<java.lang.String>")
			.build();
		imports = f.getImports();
		assertTrue(f.getImports().contains("java.util.List"));
		assertFalse(f.getImports().contains("java.lang.String"));
		f = Field.builder()
			.name("test")
			.type("java.util.Map<java.lang.String, java.util.List<java.util.Map$Entry<$interface, java.lang.String>>>")
			.build();
		imports = f.getImports();
		assertTrue(f.getImports().contains("java.util.Map"));
		assertTrue(f.getImports().contains("java.util.List"));
		assertFalse(f.getImports().contains("java.lang.String"));
		assertFalse(f.getImports().contains("$Entry"));
		assertFalse(f.getImports().contains("$interface"));
	}
}
