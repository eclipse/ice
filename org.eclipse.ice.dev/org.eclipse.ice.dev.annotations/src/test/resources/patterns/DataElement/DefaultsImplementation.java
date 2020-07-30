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
	protected java.util.UUID privateId = java.util.UUID.randomUUID();
	protected long id = 0L;
	@NonNull protected java.lang.String name = "name";
	@NonNull protected java.lang.String description = "description";
	@NonNull protected java.lang.String comment = "no comment";
	@NonNull protected java.lang.String context = "default";
	protected boolean required = false;
	protected boolean secret = false;
	protected org.eclipse.ice.data.JavascriptValidator<Test> validator;
}