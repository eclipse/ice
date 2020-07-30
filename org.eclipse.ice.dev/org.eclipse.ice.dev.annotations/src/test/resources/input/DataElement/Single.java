import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;

@DataElement(name = "Test")
public class Single {
	/**
	 * A UNIQUE STRING IN THE DOC STRING.
	 *
	 * AND ANOTHER ON A NEW LINE.
	 */
	@DataField public int testInt;
}
