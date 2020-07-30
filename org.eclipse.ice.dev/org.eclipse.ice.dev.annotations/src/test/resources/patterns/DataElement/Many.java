import org.eclipse.ice.data.IDataElement;
public interface Test extends IDataElement<Test> {
	public byte getTestByte();
	public void setTestByte(byte testByte);
	public short getTestShort();
	public void setTestShort(short testShort);
	public int getTestInt();
	public void setTestInt(int testInt);
	public long getTestLong();
	public void setTestLong(long testLong);
	public float getTestFloat();
	public void setTestFloat(float testFloat);
	public double getTestDouble();
	public void setTestDouble(double testDouble);
	public boolean isTestBoolean();
	public void setTestBoolean(boolean testBoolean);
	public char getTestChar();
	public void setTestChar(char testChar);
}