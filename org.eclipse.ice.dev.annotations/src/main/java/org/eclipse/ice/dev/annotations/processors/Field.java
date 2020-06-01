package org.eclipse.ice.dev.annotations.processors;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.ClassUtils;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;

/**
 * Container for Field information, taken from DataField Annotations, in
 * simplified form for use by Velocity template.
 *
 * @author Daniel Bluhm
 */
@Data
@Builder
@JsonDeserialize(builder = Field.FieldBuilder.class)
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
	 * Comment to add to the field declaration.
	 */
	String docString;

	/**
	 * Whether or not this field can be null.
	 *
	 * This value affects the kind of checks generated in IDataElement.matches().
	 */
	boolean nullable;

	/**
	 * Whether or not the type of this field is a primitive type.
	 *
	 * This value affects the kind of checks generated in IDataElement.matches().
	 * This is inferred from the Field's type.
	 */
	boolean primitive;

	/**
	 * Whether or not this field should be included in IDataElement.matches().
	 */
	@Builder.Default boolean match = true;

	/**
	 * Generate a getter for this field.
	 */
	@Builder.Default boolean getter = true;

	/**
	 * Generate a setter for this field.
	 */
	@Builder.Default boolean setter = true;

	/**
	 * A list of alternate names for this field.
	 */
	@Singular("alias") List<FieldAlias> aliases;

	/**
	 * Marker class used to mark a string value as one that should not be
	 * manipulated when building a field.
	 */
	@Data public static class Raw {
		@NonNull String value;
	}

	/**
	 * Convenience method for creating Raw value.
	 * @param value
	 * @return new Raw object
	 */
	public static Raw raw(String value) {
		return new Raw(value);
	}

	/**
	 * Determine if a class for the given string can be found.
	 * @param cls
	 * @return if class found
	 */
	private static boolean classFound(String cls) {
		try {
			ClassUtils.getClass(cls);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	/**
	 * Instruct Jackson how to deserialize aliases.
	 */
	private interface FieldBuilderMeta {
		@JsonDeserialize(contentAs = FieldAlias.class) FieldBuilder aliases(Collection<? extends FieldAlias> aliases);
	}

	/**
	 * Builder class for Field. This class must be a static inner class of Field in
	 * order to take advantage of Lombok's @Builder annotation. The methods defined
	 * here replace the defaults generated by Lombok and provide a multiple-dispatch
	 * based mechanism for customizing output based on the passed type.
	 *
	 * The methods prefixed with "json" are used for deserializing values from
	 * JSON using Jackson.
	 */
	@JsonPOJOBuilder(withPrefix = "json")
	public static class FieldBuilder implements FieldBuilderMeta {
		private Class<?> actualType;

		/**
		 * Format long as String for use as default value initializer.
		 * @param value the value to be formatted.
		 * @return FieldBuilder
		 */
		public FieldBuilder defaultValue(long value) {
			this.defaultValue = Long.toString(value) + "L";
			return this;
		}

		/**
		 * Format String as escaped String for use as default value initializer.
		 * @param value the value to be formatted.
		 * @return FieldBuilder
		 */
		public FieldBuilder defaultValue(String value) {
			this.defaultValue = "\"" + value + "\"";
			return this;
		}

		/**
		 * Format boolean as String for use as default value initializer.
		 * @param value the value to be formatted.
		 * @return FieldBuilder
		 */
		public FieldBuilder defaultValue(boolean value) {
			this.defaultValue = Boolean.toString(value);
			return this;
		}

		/**
		 * Take a Raw string value and pass through without manipulating for use as
		 * default value initializer.
		 * @param value the value to be formatted.
		 * @return FieldBuilder
		 */
		public FieldBuilder defaultValue(Raw value) {
			this.defaultValue = value.getValue();
			return this;
		}

		/**
		 * Format type as String.
		 * @param type the type to be formatted.
		 * @return
		 */
		public FieldBuilder type(Class<?> type) {
			this.type = type.getName().toString();
			this.actualType = type;
			this.primitive = type.isPrimitive();
			if (this.defaultValue != null && this.actualType.equals(String.class)) {
				this.defaultValue(defaultValue);
			}
			return this;
		}

		/**
		 * Take a raw string value and pass through without manipulating for use as
		 * type.
		 * @param type the type
		 * @return
		 */
		public FieldBuilder type(Raw type) {
			this.type = type.getValue();
			return this;
		}

		/**
		 * Name builder for use in Deserialization.
		 * @param name
		 * @return builder
		 */
		@JsonAlias({"fieldName"})
		public FieldBuilder jsonName(String name) {
			return this.name(name);
		}

		/**
		 * Type builder for use in Deserialization. For convenience, the type is also
		 * checked to determine whether it is a primitive type, setting the value of
		 * primitive appropriately.
		 * @param type
		 * @return builder
		 */
		@JsonAlias({"fieldType"})
		public FieldBuilder jsonType(String type) {
			try {
				if (classFound(type)) {
					return this.type(ClassUtils.getClass(type));
				} else if (classFound("java.lang." + type)) {
					return this.type(ClassUtils.getClass("java.lang." + type));
				}
				return this.type(raw(type));
			} catch (ClassNotFoundException e) {
				return this.type(raw(type));
			}
		}

		/**
		 * Default value builder for use in Deserialization.
		 *
		 * Values are passed through as is, similar to the behavior of the Raw
		 * defaultValue builder.
		 * @param defaultValue
		 * @return builder
		 */
		public FieldBuilder jsonDefaultValue(String defaultValue) {
			if (this.actualType != null && this.actualType.equals(String.class)) {
				return this.defaultValue(defaultValue);
			}
			return this.defaultValue(raw(defaultValue));
		}

		/**
		 * Nullable builder for use in Deserialization.
		 * @param nullable
		 * @return builder
		 */
		public FieldBuilder jsonNullable(boolean nullable) {
			return this.nullable(nullable);
		}

		/**
		 * Primitive builder for use in Deserialization.
		 * @param primitive
		 * @return builder
		 */
		public FieldBuilder jsonPrimitive(boolean primitive) {
			return this.primitive(primitive);
		}

		/**
		 * Match builder for use in Deserialization.
		 *
		 * @param match
		 * @return builder
		 */
		public FieldBuilder jsonMatch(boolean match) {
			return this.match(match);
		}

		/**
		 * DocString builder for use in Deserialization.
		 * @param docString
		 * @return builder
		 */
		public FieldBuilder jsonDocString(String docString) {
			return this.docString(docString);
		}

		/**
		 * Getter builder for use in Deserialization.
		 * @param getter
		 * @return
		 */
		public FieldBuilder jsonGetter(boolean getter) {
			return this.getter(getter);
		}

		/**
		 * Setter builder for use in Deserialization.
		 * @param setter
		 * @return
		 */
		public FieldBuilder jsonSetter(boolean setter) {
			return this.setter(setter);
		}
	}
}
