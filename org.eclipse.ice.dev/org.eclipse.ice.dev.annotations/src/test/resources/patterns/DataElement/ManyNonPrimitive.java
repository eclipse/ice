import org.eclipse.ice.data.IDataElement;
public interface Test extends IDataElement<Test> {
	public java.util.UUID getTestUuid();
	public void setTestUuid(java.util.UUID testUuid);
	public java.lang.String getTestString();
	public void setTestString(java.lang.String testString);
	public java.util.Date getTestDate();
	public void setTestDate(java.util.Date testDate);
}