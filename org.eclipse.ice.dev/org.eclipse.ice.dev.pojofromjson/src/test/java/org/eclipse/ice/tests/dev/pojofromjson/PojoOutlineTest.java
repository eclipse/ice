package org.eclipse.ice.tests.dev.pojofromjson;

import org.eclipse.ice.dev.annotations.processors.Field;
import org.eclipse.ice.dev.pojofromjson.PojoOutline;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PojoOutlineTest {

	@Test
	public void testSerialization() throws JsonProcessingException {
		PojoOutline pojo = PojoOutline.builder()
			.packageName("testpackage")
			.element("TestElement")
			.field(
				Field.builder()
					.name("test")
					.type(String.class)
					.docString("Test docs.")
					.match(false)
					.primitive(true)
					.nullable(true)
					.alias(Field.builder().name("another").getter(true).build())
					.build()
			).build();

		ObjectMapper mapper = new ObjectMapper();
		String pojoJson = mapper.writeValueAsString(pojo);
		System.out.println(pojoJson);
		PojoOutline rehydratedPojo = mapper.readValue(pojoJson, PojoOutline.class);
		assertEquals(pojo, rehydratedPojo);
	}

	@Test
	public void testImplementation() {
		PojoOutline pojo = PojoOutline.builder()
			.packageName("testpackage")
			.element("TestElement")
			.field(
				Field.builder()
					.name("test")
					.type(String.class)
					.docString("Test docs.")
					.match(false)
					.primitive(true)
					.nullable(true)
					.getter(false)
					.setter(false)
					.alias(Field.builder().name("another").getter(true).build())
					.build()
			).build();

		assertEquals("TestElementImplementation", pojo.getImplementation());

		pojo = PojoOutline.builder()
			.packageName("testpackage")
			.element("TestElement")
			.implementation("TestElementImpl")
			.field(
				Field.builder()
					.name("test")
					.type(String.class)
					.docString("Test docs.")
					.match(false)
					.primitive(true)
					.nullable(true)
					.getter(false)
					.setter(false)
					.alias(Field.builder().name("another").getter(true).build())
					.build()
			).build();
		assertEquals("TestElementImpl", pojo.getImplementation());
	}
}