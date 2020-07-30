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
	@NonNull public java.util.UUID testUuid;
	@NonNull public java.lang.String testString;
	@NonNull public java.util.Date testDate;
}