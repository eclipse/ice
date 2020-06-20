/**
 *
 */
package org.eclipse.ice.dev.annotations;

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

}
