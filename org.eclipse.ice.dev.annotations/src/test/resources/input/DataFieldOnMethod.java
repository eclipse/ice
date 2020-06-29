import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;

@DataElement(name = "Broken")
public class DataFieldOnMethod {
	@DataField private int test;
	@DataField
	public int testMethod() {
		return 0;
	}
}