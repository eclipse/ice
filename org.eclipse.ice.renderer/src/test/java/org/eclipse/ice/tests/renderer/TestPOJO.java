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
package org.eclipse.ice.tests.renderer;

import java.io.Serializable;

/**
 * This is a test POJO for the DataElement test.
 * @author Jay Jay Billings
 *
 */
public class TestPOJO implements Serializable {

	private String value = "foo";
	private double doubleValue = 2118.0;
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public double getDoubleValue() {
		return doubleValue;
	}
	
	public void setDoubleValue(double dValue) {
		this.doubleValue = dValue;
	}
	
	@Override
	public boolean equals(Object otherPojo) {
		TestPOJO tp2 = (TestPOJO) otherPojo;
		boolean dMatch = (this.doubleValue - tp2.doubleValue)/this.doubleValue < 1.0e-15;
		boolean vMatch = this.value.equals(tp2.value);
		return dMatch && vMatch;
	}
	
}
