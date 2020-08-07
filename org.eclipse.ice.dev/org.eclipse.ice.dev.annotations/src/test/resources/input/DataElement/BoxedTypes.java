import java.util.List;
import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;

@DataElement(name = "Test")
public class BoxedTypes {
	@DataField public List<String> stringList;
}