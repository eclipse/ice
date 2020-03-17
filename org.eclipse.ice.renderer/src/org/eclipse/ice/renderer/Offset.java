/*******************************************************************************
 * Copyright (c) 2020- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
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
