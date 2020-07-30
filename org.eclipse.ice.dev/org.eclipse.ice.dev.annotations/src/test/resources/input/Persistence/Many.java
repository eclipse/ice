import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;
import org.eclipse.ice.dev.annotations.Persisted;

@DataElement(name = "Test")
@Persisted(collection = "test")
public class Many {
	@DataField public byte testByte;
	@DataField public short testShort;
	@DataField public int testInt;
	@DataField public long testLong;
	@DataField public float testFloat;
	@DataField public double testDouble;
	@DataField public boolean testBoolean;
	@DataField public char testChar;
}
