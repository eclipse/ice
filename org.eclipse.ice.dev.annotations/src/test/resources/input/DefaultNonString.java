import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;

@DataElement(name = "Test")
public class DefaultNonString {
	@DataField.Default("42")
	@DataField public int testInt;
}
