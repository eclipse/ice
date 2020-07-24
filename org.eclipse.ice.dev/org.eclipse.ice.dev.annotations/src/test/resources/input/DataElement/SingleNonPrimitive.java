import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;

@DataElement(name = "Test")
public class SingleNonPrimitive {
	@DataField public java.util.UUID testUuid;
}