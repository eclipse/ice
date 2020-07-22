import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;

@DataElement(name = "Test")
public class DefaultString {
	@DataField.Default(value = "A String Value", isString = true)
	@DataField public String test;
}
