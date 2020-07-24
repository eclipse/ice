package org.eclipse.ice.dev.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark a Field as a DataField.
 *
 * @see org.eclipse.ice.dev.annotations.DataElement
 * @see org.eclipse.ice.dev.annotations.DataField.Default
 * @author Daniel Bluhm
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface DataField {

	/**
	 * Flag whether this field should have an associated getter
	 * generated.
	 * @return getter annotation value
	 * @see org.eclipse.ice.dev.annotations.processors.Field#getter
	 */
	boolean getter() default true;

	/**
	 * Flag whether this field should have an associated setter
	 * generated.
	 * @return setter annotation value
	 * @see org.eclipse.ice.dev.annotations.processors.Field#setter
	 */
	boolean setter() default true;

	/**
	 * Flag whether this field should be included in
	 * {@link org.eclipse.ice.data.IDataElement#matches(Object)}.
	 * @return match annotation value
	 * @see org.eclipse.ice.dev.annotations.processors.Field#match
	 */
	boolean match() default true;

	/**
	 * Flag whether this field is considered unique in a collection.
	 * This causes persistence retrieval methods to return a single value
	 * rather than an iterable of values.
	 * @return unique annotation value
	 * @see org.eclipse.ice.dev.annotations.processors.Field#unique
	 */
	boolean unique() default false;

	/**
	 * Flag whether this field should be searchable in a collection.
	 * This causes persistence retrieval methods to be generated for this
	 * field.
	 * @return searchable annotation value
	 * @see org.eclipse.ice.dev.annotations.processors.Field#searchable
	 */
	boolean searchable() default true;

	/**
	 * Flag whether this field can have a value of null. This causes
	 * this field to be annotated with {@code @NonNull} from Lombok. Defaults
	 * to false.
	 * @return nullable annotation value
	 * @see org.eclipse.ice.dev.annotations.processors.Field#nullable
	 */
	boolean nullable() default false;

	/**
	 * Set a default value for this DataField.
	 *
	 * Unfortunately, the default values of class fields cannot be retrieved during
	 * annotation processing unless they are compiled constants and the field is set
	 * as final.
	 *
	 * To accommodate setting a default value, this annotation takes a String which
	 * is placed verbatim in the generated implementation class as the default value
	 * for that field. As a result of this, default values of type String must
	 * include quotation marks in the String itself along with double escaping
	 * escaped values. To assist in correctly formatting strings, {@code isString}
	 * is provided as a flag for {@code @DataField.Default} that will cause the
	 * string to be passed through
	 * {@link javax.lang.model.util.Elements#getConstantExpression(Object)} and properly
	 * escape the output string.
	 *
	 * This annotation has no effect if the {@code @DataField} annotation is not
	 * present.
	 *
	 *
	 * @see org.eclipse.ice.dev.annotations.DataElement
	 * @see org.eclipse.ice.dev.annotations.DataField
	 * @author Daniel Bluhm
	 */
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.SOURCE)
	public @interface Default {
		/**
		 * The default value of this field represented as a String.
		 * @return defaultValue annotation value
		 */
		String value();

		/**
		 * Flag whether the value is itself intended to be a String and
		 * should be escaped.
		 * @return isString annotation value
		 */
		boolean isString() default false;
	}
}