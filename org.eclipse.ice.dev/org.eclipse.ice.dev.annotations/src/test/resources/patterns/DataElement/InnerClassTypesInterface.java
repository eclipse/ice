import org.eclipse.ice.data.IDataElement;
public interface Test extends IDataElement<Test> {
	public Entry<String, Object> getTestMapEntry();
	public void setTestMapEntry(Entry<String, Object> testMapEntry);
}