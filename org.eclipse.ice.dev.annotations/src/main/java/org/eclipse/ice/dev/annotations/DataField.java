package org.eclipse.ice.dev.annotations;

import java.lang.annotation.Repeatable;

@Repeatable(DataFields.class)
public @interface DataField {
	String fieldName();
	Class<?> fieldType() default String.class;
}
