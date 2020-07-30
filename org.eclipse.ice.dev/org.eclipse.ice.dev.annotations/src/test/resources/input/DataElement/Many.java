import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;

@DataElement(name = "Test")
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
