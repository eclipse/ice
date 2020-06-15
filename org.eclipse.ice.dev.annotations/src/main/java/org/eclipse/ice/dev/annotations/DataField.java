package org.eclipse.ice.dev.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface DataField {

	boolean getter() default true;
	boolean setter() default true;
	boolean match() default true;
	boolean unique() default false;
	boolean search() default true;
	boolean nullable() default false;

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.SOURCE)
	public @interface Default {
		String value();
	}
}