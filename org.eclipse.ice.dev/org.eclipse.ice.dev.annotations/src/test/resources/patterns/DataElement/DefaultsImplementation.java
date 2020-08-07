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
	@NonNull
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	protected UUID privateId = UUID.randomUUID();
	protected long id = 0L;
	@NonNull protected String name = "name";
	@NonNull protected String description = "description";
	@NonNull protected String comment = "no comment";
	@NonNull protected String context = "default";
	protected boolean required = false;
	protected boolean secret = false;
	protected JavascriptValidator validator;
}