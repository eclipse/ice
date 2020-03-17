/**
 * 
 */
package org.eclipse.ice.renderer;

/**
 * @author Jay Jay Billings
 *
 */
public class Offset {
	private boolean inUse;
	private double offsetValue;
	
	public Offset() {}
	
	public double getValue() {
		return offsetValue;
	}
	
	public void setValue(double value) {
		offsetValue = value;
	}
	
	public boolean getUsed() {
		return inUse;
	}
	
	public void setUsed(boolean value) {
		inUse = value;
	}
	
	@Override
	public String toString() {
		return inUse + ", " + offsetValue; 
	}
	
}
