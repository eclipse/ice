import org.eclipse.ice.dev.annotations.IDataElement;
public interface Test extends IDataElement<Test> {
	public int getTestInt();
	public void setTestInt(int testInt);
}