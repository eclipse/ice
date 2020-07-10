package org.eclipse.ice.dev.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Mark a DataElement for PersistenceHandler generation.
 *
 * The generated class will implement the IPersistenceHandler interface.
 * @author Daniel Bluhm
 */
@Retention(SOURCE)
@Target({TYPE, FIELD})
public @interface Persisted {
	/**
	 * The name of the collection to which the generated DataElement class will be
	 * stored.
	 */
	String collection();
}
