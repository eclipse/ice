/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
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
package org.eclipse.eavp.viz;

import java.util.ArrayList;

import org.eclipse.eavp.viz.service.datastructures.IVizEntryContentProvider;
import org.eclipse.eavp.viz.service.datastructures.VizAllowedValueType;

/**
 * This class provides an IEntryContentProvider for plots, which are stored as
 * Entries for an ICEResource wrapping a VisIt-compatible file.<br>
 * <br>
 * The Entry's parent should be the category or group of plots, e.g., "Meshes",
 * "Scalars", "Vectors", or "Materials".<br>
 * <br>
 * The Entry's allow values are "true" or "false", which correspond to being
 * plotted or not plotted, respectively.
 * 
 * @author Jordan H. Deyton
 */
public class PlotEntryContentProvider implements IVizEntryContentProvider {

	/**
	 * The parent entry's name.
	 */
	private final String parent;

	/**
	 * The default constructor.
	 * 
	 * @param parent
	 *            A String representing the parent group of plots.
	 */
	public PlotEntryContentProvider(String parent) {
		this.parent = (parent != null ? parent : "orphan");
	}

	/**
	 * This operation returns a list of allowed values. In this case, those
	 * values are "true" and "false".
	 * 
	 * @return The list of Strings of allowed values
	 */
	@Override
	public ArrayList<String> getAllowedValues() {
		ArrayList<String> values = new ArrayList<String>(2);
		values.add("false");
		values.add("true");
		return values;
	}

	@Override
	public void setAllowedValues(ArrayList<String> allowedValues) {
		// Does nothing.
	}

	/**
	 * This operation retrieves the default String for the value of instances of
	 * the Entry class.
	 */
	@Override
	public String getDefaultValue() {
		return "false";
	}

	/**
	 * This operation retrieves the description of the types of values that are
	 * stored in an instance of the Entry class.
	 */
	@Override
	public VizAllowedValueType getAllowedValueType() {
		return VizAllowedValueType.Discrete;
	}

	@Override
	public void setAllowedValueType(VizAllowedValueType type) {
		// Does nothing.
	}

	@Override
	public void setParent(String parentName) {
		// Does nothing.
	}

	@Override
	public String getParent() {
		return parent;
	}

	/**
	 * This operation checks to see if this PlotEntryContentProvider is equal to
	 * an input IEntryContentProvider.
	 * 
	 * @param otherProvider
	 *            The IEntryContentProvider to check if this is equal to.
	 */
	@Override
	public boolean equals(Object otherProvider) {
		boolean equal = false;

		if (otherProvider != null
				&& otherProvider instanceof PlotEntryContentProvider) {
			PlotEntryContentProvider other = (PlotEntryContentProvider) otherProvider;
			equal = (super.equals(otherProvider) && parent.equals(other.parent));
		}

		return equal;
	}

	/**
	 * Compute and return the hash code for instances of this object
	 */
	@Override
	public int hashCode() {

		// Call Object#hashCode()
		int hash = super.hashCode();

		// Compute the hash code for this object's data
		hash = 31 * hash + (null == parent ? 0 : parent.hashCode());

		// Return the computed hash code
		return hash;
	}

	@Override
	public String getTag() {
		return null;
	}

	@Override
	public void setTag(String tagValue) {
		// Does nothing.
	}

	@Override
	public void setDefaultValue(String defaultValue) {
		// Does nothing.
	}

	/**
	 * Returns a clone of this PlotEntryContentProvider.
	 */
	@Override
	public Object clone() {
		return new PlotEntryContentProvider(parent);
	}
}
