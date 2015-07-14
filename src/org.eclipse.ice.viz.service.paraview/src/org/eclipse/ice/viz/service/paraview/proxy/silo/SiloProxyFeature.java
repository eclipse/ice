/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation
 *   
 *******************************************************************************/
package org.eclipse.ice.viz.service.paraview.proxy.silo;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.ice.viz.service.paraview.proxy.ProxyFeature;
import org.eclipse.ice.viz.service.paraview.proxy.ProxyFeature.ColorByLocation;
import org.eclipse.ice.viz.service.paraview.proxy.ProxyFeature.ColorByMode;

/**
 * A sub-class of {@link ProxyFeature} specifically for Silo files. It can be
 * used to maintain the currently selected values for a particular feature or
 * category.
 * <p>
 * <b>Note:</b> The name of the feature corresponds to the name of the
 * associated object in the file proxy's "ui" JsonArray.
 * </p>
 * 
 * @author Jordan Deyton
 *
 */
public class SiloProxyFeature extends ProxyFeature {

	/**
	 * The index of the feature in the file proxy's "ui" and "properties"
	 * JsonArrays.
	 */
	public final int index;
	/**
	 * The name of the object in the file proxy's "properties" JsonArray.
	 */
	public final String propertyName;
	/**
	 * The currently selected values in the "value" JsonArray of the associated
	 * object in the file proxy's "properties" JsonArray.
	 */
	public final Set<String> selectedValues;

	/**
	 * The default constructor. Initializes a feature that cannot be used to
	 * "color by" the visualization.
	 * 
	 * @param index
	 *            The index of the feature in the file proxy's "ui" and
	 *            "properties" JsonArrays.
	 * @param uiName
	 *            The name of the object in the file proxy's "ui" JsonArray.
	 *            This is stored as the feature's {@link ProxyFeature#name name}
	 *            .
	 * @param propertyName
	 *            The name of the object in the file proxy's "properties"
	 *            JsonArray.
	 */
	public SiloProxyFeature(int index, String uiName, String propertyName) {
		this(index, uiName, propertyName, null, null);
	}

	/**
	 * The full constructor.
	 * 
	 * @param index
	 *            The index of the feature in the file proxy's "ui" and
	 *            "properties" JsonArrays.
	 * @param uiName
	 *            The name of the object in the file proxy's "ui" JsonArray.
	 *            This is stored as the feature's {@link ProxyFeature#name name}
	 *            .
	 * @param propertyName
	 *            The name of the object in the file proxy's "properties"
	 *            JsonArray.
	 * @param colorByMode
	 *            The mode for coloring. If {@code null}, it defaults to
	 *            {@link ColorByMode#SOLID}.
	 * @param colorByLocation
	 *            The "location" for coloring. If {@code null}, it defaults to
	 *            {@link ColorByLocation#POINTS}.
	 */
	public SiloProxyFeature(int index, String uiName, String propertyName,
			ColorByLocation colorByType, ColorByMode colorByMode) {
		super(uiName, colorByMode, colorByType);

		this.index = index;
		this.propertyName = propertyName;

		// Initialize the map of selected values.
		selectedValues = new HashSet<String>();
	}
}
