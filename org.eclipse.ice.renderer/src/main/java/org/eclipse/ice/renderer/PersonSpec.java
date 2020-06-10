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
	@DataField public int age;

	/**
	 * The person's first name.
	 */
	@DataField public String firstName;

	/**
	 * The person's last name.
	 */
	@DataField public String lastName;
}
