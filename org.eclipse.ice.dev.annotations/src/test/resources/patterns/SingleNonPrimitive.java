import org.eclipse.ice.dev.annotations.IDataElement;
public interface Test extends IDataElement<Test> {
	public java.util.UUID getTestUuid();
	public void setTestUuid(java.util.UUID testUuid);
}