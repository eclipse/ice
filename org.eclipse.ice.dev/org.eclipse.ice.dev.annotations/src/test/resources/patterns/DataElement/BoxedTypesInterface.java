import org.eclipse.ice.data.IDataElement;
public interface Test extends IDataElement<Test> {
	public List<String> getStringList();
	public void setStringList(List<String> stringList);
}