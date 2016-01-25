/*******************************************************************************
 * Copyright (c) 2012, 2014, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Alex McCaskey
 *******************************************************************************/
package org.eclipse.ice.datastructures.entry;

import org.eclipse.ice.datastructures.ICEObject.IUpdateable;

/**
 * 
 * @author Alex McCaskey
 *
 */
public class MultiValueEntry extends StringEntry {

	private String[] values;
	
	@Override
	public Object clone() {
		MultiValueEntry entry = new MultiValueEntry();
		entry.copy(this);
		return entry;
	}

	@Override
	public boolean setValue(String... values) {
		return false;
	}

	@Override
	public void update(IUpdateable component) {
		// TODO Auto-generated method stub

	}

	@Override
	public String[] getValues() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void accept(IEntryVisitor visitor) {
		visitor.visit(this);
	}

}
