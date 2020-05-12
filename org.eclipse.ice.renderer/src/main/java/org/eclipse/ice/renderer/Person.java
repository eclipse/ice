package org.eclipse.ice.renderer;

import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;

@DataElement
@DataField(fieldName = "age", fieldType = String.class)
@DataField(fieldName = "firstName", fieldType = String.class)
@DataField(fieldName = "lastName", fieldType = String.class)
public interface Person {
	//void foo();
}
