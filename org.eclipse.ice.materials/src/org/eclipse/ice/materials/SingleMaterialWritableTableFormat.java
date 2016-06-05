/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation -
 *   Kasper Gammeltoft
 *******************************************************************************/
package org.eclipse.ice.materials;

import org.eclipse.january.form.Material;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.odell.glazedlists.gui.WritableTableFormat;

/**
 * This class implements the WritableTableFormat interface for use with a single
 * material. This should show all of the material's properties for access in a
 * table format.
 *
 * @author Kasper Gammeltoft
 *
 */
public class SingleMaterialWritableTableFormat implements
		WritableTableFormat<String> {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(SingleMaterialWritableTableFormat.class);

	/**
	 * The material to display, needed to access the material's specific
	 * properties.
	 */
	private Material material;

	/**
	 * Constructor
	 *
	 * @param material
	 *            The material to keep up with. Must call setMaterial(Material
	 *            material) each time the material changes!)
	 */
	public SingleMaterialWritableTableFormat(Material material) {
		this.material = material;

	}

	/**
	 * Sets a new material to display information with.
	 *
	 * @param material
	 *            The new material for this table format to display
	 */
	public void setMaterial(Material material) {
		this.material = material;
	}

	/**
	 * Gets the material that this table format is currently using.
	 *
	 * @return The material that this table format is currently using.
	 */
	public Material getMaterial() {
		return material;
	}

	/**
	 * Returns the total number of columns, just 2 in this case as the material
	 * only has properties and values.
	 *
	 * @return The column count
	 */
	@Override
	public int getColumnCount() {
		return 2;
	}

	/**
	 * Gets the column name, the first column is properties, the second is
	 * values.
	 *
	 * @param col
	 *            The column.
	 * @return The name of the specified column, as a String
	 */
	@Override
	public String getColumnName(int col) {
		String name;
		if (col == 0) {
			name = "Property";
		} else {
			name = "Value";
		}
		return name;
	}

	/**
	 * Gets the value for a specific property. If the col==0, then that is the
	 * column holding the property strings and the returned object will be that
	 * string. If not, then it returns the value of
	 * Material.getProperty(property) for the current material.
	 *
	 * @param property
	 *            The property (row) to retrieve
	 * @param col
	 *            The column to retrieve. If equal to 0, retrieves the name of
	 *            the property (the same as the property parameter). Otherwise,
	 *            will return the value of that property.
	 * @return Returns the value of the property specified for the current
	 *         material.
	 */
	@Override
	public Object getColumnValue(String property, int col) {
		Object colVal = new Object();

		if (col == 0) {
			colVal = property;
		} else {
			colVal = material.getProperty(property);
		}
		return colVal;
	}

	/**
	 * Should always return true.
	 *
	 */
	@Override
	public boolean isEditable(String arg0, int arg1) {
		return true;
	}

	/**
	 * Sets a new column value. Note- cannot change the value of a property
	 * name, so the col parameter is not used here.
	 *
	 * @param property
	 *            The property name to change.
	 * @param newVal
	 *            The new value for the material's property
	 * @param col
	 *            The column to change. Because the material's property names
	 *            are not editable, this parameter is not used as it is assumed
	 *            that the value is what is to be changed.
	 */
	@Override
	public String setColumnValue(String property, Object newVal, int col) {
		double val = material.getProperty(property);
		if (newVal instanceof String) {
			try {
				val = Double.parseDouble((String) newVal);
			} catch (Exception e) {
				logger.error(getClass().getName() + " Exception!",e);
			}
		} else {
			try {
				val = (Double) newVal;
			} catch (Exception e) {
				logger.error(getClass().getName() + " Exception!",e);
			}
		}
		material.setProperty(property, val);
		return null;
	}

}