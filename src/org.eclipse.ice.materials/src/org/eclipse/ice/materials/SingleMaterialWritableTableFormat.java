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

import org.eclipse.ice.datastructures.form.Material;

import ca.odell.glazedlists.gui.WritableTableFormat;


/**
 * This class implements the WritableTableFormat interface for use with a single material.
 * This should show all of the material's properties for access in a table format. 
 * @author Kasper Gammeltoft
 *
 */
public class SingleMaterialWritableTableFormat implements WritableTableFormat<String>{

	/**
	 * The material to display, needed to access the material's specific properties.
	 */
	private Material material;
	
	/**
	 * Constructor
	 * @param material The material to keep up with. Must call setMaterial(Material material) each time the material changes!)
	 */
	public SingleMaterialWritableTableFormat(Material material) {
		this.material = material;
		
	}
	
	/**
	 * Sets a new material to display information with. 
	 * @param material The new material 
	 */
	public void setMaterial(Material material){
		this.material = material;
	}

	/**
	 * Returns the total number of columns, just 2 in this case as the material only has properties and values.
	 */
	@Override
	public int getColumnCount() {
		return 2;
	}

	/**
	 * Gets the column name, the first column is properties, the second is values. 
	 */
	@Override
	public String getColumnName(int arg0) {
		String name;
		if(arg0==0){
			name = "Property";
		} else {
			name = "Value";
		}
		return name;
	}

	/**
	 * Gets the value for a specific property. If the col==0, then that is the column holding the 
	 * property strings and the returned object will be that string. If not, then it returns the 
	 * value of Material.getProperty(property) for the current material. 
	 */
	@Override
	public Object getColumnValue(String property, int col) {
		Object colVal = new Object();
		
		if(col==0){
			colVal = property;
		} else {
			colVal = material.getProperty(property);
		}
		return colVal;
	}

	/**
	 * Should always return true. 
	 */
	@Override
	public boolean isEditable(String arg0, int arg1) {
		return true;
	}

	
	/**
	 * Sets a new column value. Note- cannot change the value of a property name, so the 
	 * col parameter is not used here. 
	 */
	@Override
	public String setColumnValue(String property, Object newVal, int col) {
		double val;
		if(newVal instanceof String){
			try{
				val = Double.parseDouble((String)newVal);
				material.setProperty(property, val);
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	
	
	
	
	
	
	
	

}