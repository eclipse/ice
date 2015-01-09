/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.materials;

import java.util.List;

import org.eclipse.ice.datastructures.form.Material;

import ca.odell.glazedlists.gui.WritableTableFormat;

/**
 * This class implements the WritableTableFormat interface to make it possible
 * to read and write GlazedLists of Materials.
 * 
 * It must be instantiated with a list of properties that represent its columns
 * and from which it can acquire the number of columns. It will also use this
 * list as the keys when pulling properties from the Materials passed to it.
 * 
 * Instead of assuming that updated values passed to it are doubles, it calls
 * Object.toString() and interprets that value as a double.
 * 
 * It always reports values as editable.
 * 
 * @author Jay Jay Billings
 *
 */
public class MaterialWritableTableFormat implements
		WritableTableFormat<Material> {

	/**
	 * The list of properties that provides the keys needed to read the Material
	 * properties of the List with which this format is associated.
	 */
	private List<String> properties;

	/**
	 * The constructor
	 * 
	 * @param propertiesList
	 *            The list of properties that provides the keys needed to read
	 *            the Material properties of the List with which this format is
	 *            associated.
	 */
	public MaterialWritableTableFormat(List<String> propertiesList) {
		properties = propertiesList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ca.odell.glazedlists.gui.TableFormat#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return properties.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ca.odell.glazedlists.gui.TableFormat#getColumnName(int)
	 */
	@Override
	public String getColumnName(int column) {
		return properties.get(column);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ca.odell.glazedlists.gui.TableFormat#getColumnValue(java.lang.Object,
	 * int)
	 */
	@Override
	public Object getColumnValue(Material baseObject, int column) {
		// Get the property off of the base object
		return baseObject.getProperty(properties.get(column));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ca.odell.glazedlists.gui.WritableTableFormat#isEditable(java.lang.Object,
	 * int)
	 */
	@Override
	public boolean isEditable(Material baseObject, int column) {
		// These should always be editable
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ca.odell.glazedlists.gui.WritableTableFormat#setColumnValue(java.lang
	 * .Object, java.lang.Object, int)
	 */
	@Override
	public Material setColumnValue(Material baseObject, Object editedValue,
			int column) {
		// Set the property
		baseObject.setProperty(properties.get(column),
				Double.valueOf(editedValue.toString()));
		// Just return the material
		return baseObject;
	}

}
