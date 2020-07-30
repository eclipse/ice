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
	@NonNull public java.lang.String test = "A String Value";
}