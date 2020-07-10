import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;

@DataElement(name = "Test")
public class AccessibilityPreserved {
	@DataField public int shouldBePublic;
	@DataField protected int shouldBeProtected;
	@DataField private int shouldBePrivate;
	@DataField int shouldBePackage;
}