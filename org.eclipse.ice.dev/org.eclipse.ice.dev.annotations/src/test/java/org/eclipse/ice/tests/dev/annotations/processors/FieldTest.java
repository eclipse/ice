/**
 *
 */
package org.eclipse.ice.tests.dev.annotations.processors;

import static org.junit.jupiter.api.Assertions.*;

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
			.var("privateId")
			.build();
		assertTrue(f.isVarDifferent());
		assertEquals("privateId", f.getVar());
		f = Field.builder()
			.name("test")
			.getter(false)
			.build();
		assertEquals("test", f.getVar());
		assertFalse(f.isVarDifferent());
	}
}
