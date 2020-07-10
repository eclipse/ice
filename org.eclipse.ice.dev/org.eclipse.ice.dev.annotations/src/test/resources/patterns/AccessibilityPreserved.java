@Data
@NoArgsConstructor
public class TestImplementation implements Test, Serializable {
	public int shouldBePublic;
	protected int shouldBeProtected;
	private int shouldBePrivate;
	int shouldBePackage;
}