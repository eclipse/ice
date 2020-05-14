package org.eclipse.ice.dev.annotations.processors;

import java.util.Arrays;

public class DefaultFields extends Fields {

	private static Field privateId = new Field("privateId", "UUID", "UUID.randomUUID()", false, false, false);
	private static Field id = new Field("id", "long", "0L", false, true);
	private static Field name = new Field("name", "String", "\"name\"", false, false);
	private static Field description = new Field("description", "String", "\"description\"", false, false);
	private static Field comment = new Field("comment", "String", "\"no comment\"", false, false);
	private static Field context = new Field("context", "String", "\"default\"", false, false);
	private static Field required = new Field("required", "boolean", "false", false, true);
	private static Field secret = new Field("secret", "boolean", "false", false, true);
	private static Field validator = new Field("validator", "JavascriptValidator<${class}>", null, true, false);

	public DefaultFields() {
		this.fields = Arrays.asList(privateId, id, name, description, comment, context, required, secret, validator);
	}
}
