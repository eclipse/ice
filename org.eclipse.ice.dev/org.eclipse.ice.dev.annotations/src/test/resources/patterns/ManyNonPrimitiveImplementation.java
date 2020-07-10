@Data
@NoArgsConstructor
public class TestImplementation implements Test, Serializable {
	@NonNull public java.util.UUID testUuid;
	@NonNull public java.lang.String testString;
	@NonNull public java.util.Date testDate;
}