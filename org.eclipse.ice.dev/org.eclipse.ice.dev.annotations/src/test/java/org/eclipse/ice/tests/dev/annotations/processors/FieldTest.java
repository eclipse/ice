/**
 *
 */
package org.eclipse.ice.tests.dev.annotations.processors;

import static org.junit.jupiter.api.Assertions.*;

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
			.alias(Field.builder().name("another").getter(true).build())
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
		Field fBool = Field.builder()
			.name("test")
			.type(boolean.class)
			.build();
		assertEquals("isTest", fBool.getGetterName());
	}

	/**
	 * Test hasGetter.
	 */
	@Test
	void testHasGetter() {
		Field f = Field.builder()
			.name("test")
			.type(String.class)
			.getter(true)
			.build();
		assertTrue(f.hasGetter());
		f = Field.builder()
			.name("test")
			.type(String.class)
			.getter(false)
			.alias(Field.builder().name("another").getter(true).build())
			.build();
		assertTrue(f.hasGetter());
	}

	/**
	 * Test getAnyGetter.
	 */
	@Test
	void testAnyGetter() {
		Field f = Field.builder()
			.name("test")
			.type(String.class)
			.getter(true)
			.build();
		assertEquals("getTest", f.getAnyGetter());
		f = Field.builder()
			.name("test")
			.type(String.class)
			.getter(false)
			.alias(Field.builder().name("another").getter(true).build())
			.build();
		assertEquals("getAnother", f.getAnyGetter());
	}
}
