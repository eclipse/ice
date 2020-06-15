package org.eclipse.ice.renderer;

import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;
import org.eclipse.ice.dev.annotations.Persisted;

@DataElement(name = "Person")
@Persisted(collection = "people")
public class PersonSpec {
	/**
	 * The person's age.
	 */
	@DataField.Default("42")
	@DataField private int age;

	/**
	 * The person's first name.
	 */
	@DataField.Default(value = "Bob", isString = true)
	@DataField private String firstName;

	/**
	 * The person's last name.
	 */
	@DataField.Default(value = "Builder", isString = true)
	@DataField private String lastName;
}
