@Data
@NoArgsConstructor
@JsonAutoDetect(
	fieldVisibility = Visibility.ANY,
	getterVisibility = Visibility.NONE,
	isGetterVisibility = Visibility.NONE,
	setterVisibility = Visibility.NONE
)
public class TestImplementation implements Test, Serializable {
	public byte testByte;
	public short testShort;
	public int testInt;
	public long testLong;
	public float testFloat;
	public double testDouble;
	public boolean testBoolean;
	public char testChar;
}