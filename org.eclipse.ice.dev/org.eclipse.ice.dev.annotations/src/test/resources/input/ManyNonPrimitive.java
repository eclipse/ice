import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;

@DataElement(name = "Test")
public class ManyNonPrimitive {
	@DataField public java.util.UUID testUuid;
	@DataField public java.lang.String testString;
	@DataField public java.util.Date testDate;
}