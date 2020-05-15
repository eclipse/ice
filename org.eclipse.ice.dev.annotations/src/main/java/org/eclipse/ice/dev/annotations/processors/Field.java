package org.eclipse.ice.dev.annotations.processors;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * Container for Field information, taken from DataField Annotations, in
 * simplified String form.
 */
@Data
@Builder
public class Field {
	/**
	 * Name of the field.
	 */
	String name;

	/**
	 * String representation of the field's type.
	 */
	String type;

	/**
	 * The default value of this field.
	 */
	String defaultValue;

	/**
	 * Whether or not this field can be null.
	 */
	boolean nullable;

	/**
	 * Whether or not the type of this field is a primitive type.
	 */
	boolean primitive;

	/**
	 * Whether or not this field should be included in the matches method checks.
	 */
	@Builder.Default boolean match = true;

	public static class FieldBuilder {
		public FieldBuilder defaultValue(long value) {
			this.defaultValue = Long.toString(value) + "L";
			return this;
		}

		public FieldBuilder defaultValue(String value) {
			this.defaultValue = "\"" + value + "\"";
			return this;
		}
		public FieldBuilder defaultValue(boolean value) {
			this.defaultValue = Boolean.toString(value);
			return this;
		}
		public FieldBuilder defaultValue(Raw value) {
			this.defaultValue = value.getValue();
			return this;
		}
		public FieldBuilder type(Class<?> type) {
			this.type = type.getName().toString();
			return this;
		}
		public FieldBuilder type(Raw type) {
			this.type = type.getValue();
			return this;
		}
	}

	@Data public static class Raw {
		@NonNull String value;
	}

	public static Raw raw(String value) {
		return new Raw(value);
	}
}