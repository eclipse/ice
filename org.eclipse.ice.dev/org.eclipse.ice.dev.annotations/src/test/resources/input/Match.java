import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;

@DataElement(name = "Test")
public class Match {
	@DataField public int toBeMatched;
	@DataField(match = false)
	public int toNotBeMatched;
}
