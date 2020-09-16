import org.eclipse.ice.data.IDataElement;
public interface Test extends IDataElement<Test> {
	public UUID getTestUuid();
	public void setTestUuid(UUID testUuid);
	public String getTestString();
	public void setTestString(String testString);
	public Date getTestDate();
	public void setTestDate(Date testDate);
}