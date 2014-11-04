/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.materials.ui;

import java.util.Map;
import java.util.Vector;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * This is a simple content provider for a Material's properties. The only
 * operation of consequence is the getElements operation.
 * 
 * @author Jay Jay Billings
 *
 */
public class MaterialPropertyContentProvider implements
		IStructuredContentProvider {

	/**
	 * 
	 */
	public MaterialPropertyContentProvider() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface
	 * .viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java
	 * .lang.Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {

		// The return value
		Object[] retVal = null;
		// Use a vector for storing the properties because it is synchronized.
		Vector<MaterialProperty> properties = new Vector<MaterialProperty>();

		if (inputElement instanceof Map<?, ?>) {
			// Cast to the proper Map type
			Map<String, Double> inputMap = (Map<String, Double>) inputElement;
			// Create the set of properties from the map
			for (String key : inputMap.keySet()) {
				// Create the simple wrapper
				MaterialProperty property = new MaterialProperty();
				property.key = key;
				property.value = inputMap.get(key);
				// Put it on the list
				properties.add(property);
			}
			retVal = properties.toArray();
		}

		return retVal;
	}

}
