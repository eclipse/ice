import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.Persisted;

@DataElement(name = "Test")
@Persisted(collection = "test")
public class NoDataFields { }