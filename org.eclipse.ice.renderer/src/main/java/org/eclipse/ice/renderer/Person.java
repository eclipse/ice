package org.eclipse.ice.renderer;

import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;
import org.eclipse.ice.dev.annotations.IDataElement;
import org.eclipse.ice.dev.annotations.Persisted;

@DataElement(name = "Person")
@DataField(fieldName = "age", fieldType = int.class, docString = "The age of the person.")
@DataField(fieldName = "firstName", fieldType = String.class, docString = "The first name of the person.")
@DataField(fieldName = "lastName", fieldType = String.class, docString = "The last name of the Person.")
@Persisted(collection = "people")
public interface Person extends IDataElement {
	public int getAge();
	public void setAge(int age);
	public String getFirstName();
	public void setFirstName(String firstName);
	public String getLastName();
	public void setLastName(String lastName);
}
