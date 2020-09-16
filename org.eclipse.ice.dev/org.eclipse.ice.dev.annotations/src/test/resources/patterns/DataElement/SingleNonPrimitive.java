import org.eclipse.ice.data.IDataElement;
public interface Test extends IDataElement<Test> {
	public UUID getTestUuid();
	public void setTestUuid(UUID testUuid);
}