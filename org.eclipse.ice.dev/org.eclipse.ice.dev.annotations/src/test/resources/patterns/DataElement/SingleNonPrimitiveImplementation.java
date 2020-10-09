@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect(
	fieldVisibility = Visibility.ANY,
	getterVisibility = Visibility.NONE,
	isGetterVisibility = Visibility.NONE,
	setterVisibility = Visibility.NONE
)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = TestImplementation.TestImplementationBuilder.class)
public class TestImplementation implements Test, Serializable {
	@NonNull public UUID testUuid;
}