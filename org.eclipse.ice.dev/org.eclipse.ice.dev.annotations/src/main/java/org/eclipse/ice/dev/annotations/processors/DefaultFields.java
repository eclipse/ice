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
package org.eclipse.ice.dev.annotations.processors;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.eclipse.ice.data.JavascriptValidator;

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
		.name("UUID")
		.varName("privateId")
		.type(UUID.class)
		.docString("The private UUID of this element. This field is left out of matches().")
		.defaultValue("UUID.randomUUID()")
		.match(false)
		.getter(true)
		.setter(false)
		.defaultField(true)
		.unique(true)
		.build();

	/**
	 * A unique identifier for this element.
	 */
	private static Field id = Field.builder()
		.name("id")
		.type(long.class)
		.docString("A unique identifier for this element.")
		.defaultValue(javaSource(0L))
		.defaultField(true)
		.build();

	/**
	 * A simple name for the data.
	 */
	private static Field name = Field.builder()
		.name("name")
		.type(String.class)
		.docString("A simple name for the data.")
		.defaultValue(javaSource("name"))
		.defaultField(true)
		.build();

	/**
	 * A simple description of the data.
	 */
	private static Field description = Field.builder()
		.name("description")
		.type(String.class)
		.docString("A simple description of the data")
		.defaultValue(javaSource("description"))
		.defaultField(true)
		.build();

	/**
	 * A comment that annotates the data in a meaningful way.
	 */
	private static Field comment = Field.builder()
		.name("comment")
		.type(String.class)
		.docString("A comment that annotates the data in a meaningful way.")
		.defaultValue(javaSource("no comment"))
		.defaultField(true)
		.build();

	/**
	 * The context (a tag) in which the data should be considered.
	 */
	private static Field context = Field.builder()
		.name("context")
		.type(String.class)
		.docString("The context (a tag) in which the data should be considered.")
		.defaultValue(javaSource("default"))
		.defaultField(true)
		.build();

	/**
	 * This value is true if the element should be regarded as a client as required.
	 */
	private static Field required = Field.builder()
		.name("required")
		.type(boolean.class)
		.docString("This value is true if the element should be regarded by the client as required.")
		.defaultValue(javaSource(false))
		.defaultField(true)
		.build();

	/**
	 * This value is true if the element should be regarded as a secret by a client,
	 * such as for passwords.
	 */
	private static Field secret = Field.builder()
		.name("secret")
		.type(boolean.class)
		.docString("This value is true if the element should be regarded as a secret by the client, such as for passwords.")
		.defaultValue(javaSource(false))
		.defaultField(true)
		.build();

	/**
	 * The validator used to check the correctness of the data.
	 */
	private static Field validator = Field.builder()
		.name("validator")
		.type(JavascriptValidator.class.getCanonicalName() + "<$interface>")
		.docString("The validator used to check the correctness of the data.")
		.nullable(true)
		.defaultField(true)
		.searchable(false)
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

	/**
	 * Format long as String for use as default value initializer.
	 * @param value the value to be formatted.
	 * @return String
	 */
	private static String javaSource(long value) {
		return Long.toString(value) + "L";
	}

	/**
	 * Format String as escaped String for use as default value initializer.
	 * @param value the value to be formatted.
	 * @return String
	 */
	private static String javaSource(String value) {
		return "\"" + value + "\"";
	}

	/**
	 * Format boolean as String for use as default value initializer.
	 * @param value the value to be formatted.
	 * @return String
	 */
	private static String javaSource(boolean value) {
		return Boolean.toString(value);
	}
}
