package org.eclipse.ice.dev.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface DataModel{
	/**
	 * Name of the DataModel to generate
	 * @return name annotation value
	 */
	String name();
}
