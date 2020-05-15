package org.eclipse.ice.dev.annotations.processors;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Aggregation of fields generated and included in <code>@DataElement</code>
 * by default.
 */
public class DefaultFields {

	/**
	 * The private UUID of this element.
	 *
	 * This field is left out of the <code>DataElement.matches</code> checks.
	 */
	private static Field privateId = Field.builder()
		.name("privateId")
		.type(UUID.class)
		.defaultValue(Field.raw("UUID.randomUUID()"))
		.match(false)
		.build();

	/**
	 * A unique identifier for this element.
	 */
	private static Field id = Field.builder()
		.name("id")
		.type(long.class)
		.defaultValue(0L)
		.primitive(true)
		.build();

	/**
	 * A simple name for the data.
	 */
	private static Field name = Field.builder()
		.name("name")
		.type(String.class)
		.defaultValue("name")
		.build();

	/**
	 * A simple description of the data.
	 */
	private static Field description = Field.builder()
		.name("description")
		.type(String.class)
		.defaultValue("description")
		.build();

	/**
	 * A comment that annotates the data in a meaningful way.
	 */
	private static Field comment = Field.builder()
		.name("comment")
		.type(String.class)
		.defaultValue("no comment")
		.build();

	/**
	 * The context (a tag) in which the data should be considered.
	 */
	private static Field context = Field.builder()
		.name("context")
		.type(String.class)
		.defaultValue("default")
		.build();

	/**
	 * This value is true if the element should be regarded as a client as required.
	 */
	private static Field required = Field.builder()
		.name("required")
		.type(boolean.class)
		.defaultValue(false)
		.primitive(true)
		.build();

	/**
	 * This value is true if the element should be regarded as a secret by a client,
	 * such as for passwords.
	 */
	private static Field secret = Field.builder()
		.name("secret")
		.type(boolean.class)
		.defaultValue(false)
		.primitive(true)
		.build();

	/**
	 * The validator used to check the correctness of the data.
	 */
	private static Field validator = Field.builder()
		.name("validator")
		.type(Field.raw("JavascriptValidator<$class>"))
		.nullable(true)
		.build();

	/**
	 * Get the statically defined default fields.
	 * @return list of Fields
	 */
	public static List<Field> get() {
		return Arrays.asList(
			privateId, id, name, description, comment,
			context, required, secret, validator
		);
	}
}
