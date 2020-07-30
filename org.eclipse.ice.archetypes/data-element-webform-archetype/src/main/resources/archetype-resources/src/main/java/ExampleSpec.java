package $package;

import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;
import org.eclipse.ice.dev.annotations.Persisted;

@DataElement(name = "Example")
@Persisted(collection = "examples")
public class ExampleSpec {
	
	@DataField public int exampleInt;
	
	@DataField.Default(value = "This is a string!", isString = true)
	@DataField public String exampleString;
	
}