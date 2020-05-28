package org.eclipse.ice.renderer;

import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;

@DataElement
@DataField(fieldName = "age", fieldType = int.class, docString = "The age of the person.")
@DataField(fieldName = "firstName", fieldType = String.class, docString = "The first name of the person.")
@DataField(fieldName = "lastName", fieldType = String.class, docString = "The last name of the Person.")
public interface Person {
	//void foo();
}
