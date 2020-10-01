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
	@NonNull
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	@Builder.Default protected UUID privateId = UUID.randomUUID();
	@Builder.Default protected long id = 0L;
	@NonNull @Builder.Default protected String name = "name";
	@NonNull @Builder.Default protected String description = "description";
	@NonNull @Builder.Default protected String comment = "no comment";
	@NonNull @Builder.Default protected String context = "default";
	@Builder.Default protected boolean required = false;
	@Builder.Default protected boolean secret = false;
	protected JavascriptValidator<Test> validator;
}