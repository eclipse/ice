/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Kasper Gammeltoft
 *******************************************************************************/

package org.eclipse.ice.reflectivity;

import org.eclipse.ice.datastructures.form.Material;

/**
 * This class represents a selected cell in the table for the reflectivity
 * model. It has a material (the row) and a property name (the column).
 * 
 * @author Kasper Gammeltoft
 *
 */
public class MaterialSelection {

	/**
	 * The mateiral for this selection (the row)
	 */
	private Material material;

	/**
	 * The selected propety name (the column)
	 */
	private String selectedProperty;

	/**
	 * Constructor, sets the material and property
	 * 
	 * @param material
	 *            The material of the selection
	 * @param property
	 *            The property of the selection
	 */
	public MaterialSelection(Material material, String property) {

		// Set the variables
		this.material = material;
		selectedProperty = property;

	}

	/**
	 * Gets the selected material.
	 * 
	 * @return The selected material
	 */
	public Material getMaterial() {
		return material;
	}

	/**
	 * Gets the selected property name for the selected material.
	 * 
	 * @return The selected property name
	 */
	public String getSelectedProperty() {
		return selectedProperty;
	}

	/**
	 * Determines if this material selection is equal to another one
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		boolean equal = false;
		if (other != null && other instanceof MaterialSelection) {
			if (this == other) {
				equal = true;
			} else {
				MaterialSelection selection = (MaterialSelection) other;
				equal = this.material.equals(selection.material)
						&& this.selectedProperty
								.equals(selection.selectedProperty);
			}
		}
		return equal;
	}

}
