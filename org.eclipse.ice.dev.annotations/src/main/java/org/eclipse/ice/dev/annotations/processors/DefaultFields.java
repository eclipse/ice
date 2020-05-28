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
		.comment("The private UUID of this element. This field is left out of matches().")
		.defaultValue(Field.raw("UUID.randomUUID()"))
		.match(false)
		.getter(false)
		.setter(false)
		.alias(
			FieldAlias.builder()
				.alias("UUID")
				.getter(true)
				.build()
		)
		.build();

	/**
	 * A unique identifier for this element.
	 */
	private static Field id = Field.builder()
		.name("id")
		.type(long.class)
		.comment("A unique identifier for this element.")
		.defaultValue(0L)
		.build();

	/**
	 * A simple name for the data.
	 */
	private static Field name = Field.builder()
		.name("name")
		.type(String.class)
		.comment("A simple name for the data.")
		.defaultValue("name")
		.build();

	/**
	 * A simple description of the data.
	 */
	private static Field description = Field.builder()
		.name("description")
		.type(String.class)
		.comment("A simple description of the data")
		.defaultValue("description")
		.build();

	/**
	 * A comment that annotates the data in a meaningful way.
	 */
	private static Field comment = Field.builder()
		.name("comment")
		.type(String.class)
		.comment("A comment that annotates the data in a meaningful way.")
		.defaultValue("no comment")
		.build();

	/**
	 * The context (a tag) in which the data should be considered.
	 */
	private static Field context = Field.builder()
		.name("context")
		.type(String.class)
		.comment("The context (a tag) in which the data should be considered.")
		.defaultValue("default")
		.build();

	/**
	 * This value is true if the element should be regarded as a client as required.
	 */
	private static Field required = Field.builder()
		.name("required")
		.type(boolean.class)
		.comment("This value is true if the element should be regarded by the client as required.")
		.defaultValue(false)
		.build();

	/**
	 * This value is true if the element should be regarded as a secret by a client,
	 * such as for passwords.
	 */
	private static Field secret = Field.builder()
		.name("secret")
		.type(boolean.class)
		.comment("This value is true if the element should be regarded as a secret by the client, such as for passwords.")
		.defaultValue(false)
		.build();

	/**
	 * The validator used to check the correctness of the data.
	 */
	private static Field validator = Field.builder()
		.name("validator")
		.type(Field.raw("JavascriptValidator<$class>"))
		.comment("The validator used to check the correctness of the data.")
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
