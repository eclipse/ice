package org.eclipse.ice.dev.annotations.processors;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Helper class describing all the fields included in @DataElement by default.
 *
 */
public class DefaultFields {

	private static Field privateId = Field.builder()
		.name("privateId")
		.type(UUID.class)
		.defaultValue(Field.raw("UUID.randomUUID()"))
		.match(false)
		.build();

	private static Field id = Field.builder()
		.name("id")
		.type(long.class)
		.defaultValue(0L)
		.primitive(true)
		.build();

	private static Field name = Field.builder()
		.name("name")
		.type(String.class)
		.defaultValue("name")
		.build();

	private static Field description = Field.builder()
		.name("description")
		.type(String.class)
		.defaultValue("description")
		.build();

	private static Field comment = Field.builder()
		.name("comment")
		.type(String.class)
		.defaultValue("no comment")
		.build();

	private static Field context = Field.builder()
		.name("context")
		.type(String.class)
		.defaultValue("default")
		.build();

	private static Field required = Field.builder()
		.name("required")
		.type(boolean.class)
		.defaultValue(false)
		.primitive(true)
		.build();

	private static Field secret = Field.builder()
		.name("secret")
		.type(boolean.class)
		.defaultValue(false)
		.primitive(true)
		.build();

	private static Field validator = Field.builder()
		.name("validator")
		.type(Field.raw("JavascriptValidator<$class>"))
		.nullable(true)
		.build();

	public static List<Field> get() {
		return Arrays.asList(
			privateId, id, name, description, comment,
			context, required, secret, validator
		);
	}
}
