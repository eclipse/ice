package org.eclipse.ice.renderer;

import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;
import org.eclipse.ice.dev.annotations.Persisted;

@DataElement(name = "Person")
@Persisted(collection = "people")
public class PersonSpec {
	/**
	 * The person's age. 
	 * 
	 * This doc will be copied to the implementation.
	 */
	@DataField.Default("42")
	@DataField private int age;

	/**
	 * The person's first name.
	 * 
	 * This doc will be copied to the implementation.
	 */
	@DataField.Default(value = "Bob", isString = true)
	@DataField private String firstName;

	/**
	 * The person's last name.
	 * 
	 * This doc will be copied to the implementation.
	 */
	@DataField.Default(value = "Builder", isString = true)
	@DataField private String lastName;

	/**
	 * An example constant value. This one probably doesn't actually make sense.
	 * 
	 * This doc will be copied to the implementation.
	 */
	@DataField public static final String COLLECTION = "people";
	
	/**
	 * Basic enumeration example. Note that the fully qualified value name needs
	 * to be added and the first time the enumeration is used the build needs
	 * to be run.
	 */
	@DataField.Default(value="org.eclipse.ice.renderer.PersonEnum.TALL");
	@DataField public PersonEnum height;
}
