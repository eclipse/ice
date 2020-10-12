@Data
@NoArgsConstructor
@JsonAutoDetect(
	fieldVisibility = Visibility.ANY,
	getterVisibility = Visibility.NONE,
	isGetterVisibility = Visibility.NONE,
	setterVisibility = Visibility.NONE
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestImplementation implements Test, Serializable {
	public int shouldBePublic;
	protected int shouldBeProtected;
	private int shouldBePrivate;
	int shouldBePackage;
}