import java.util.Map;
import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;

@DataElement(name = "Test")
public class InnerClassTypes {
	@DataField public Map.Entry<String, Object> testMapEntry;
}