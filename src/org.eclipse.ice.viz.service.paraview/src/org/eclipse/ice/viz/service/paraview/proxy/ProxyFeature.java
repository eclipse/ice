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
package org.eclipse.ice.viz.service.paraview.proxy;

import java.util.HashSet;
import java.util.Set;

/**
 * A class representing a feature for an {@link IParaViewProxy}. It contains the
 * minimal information required to draw it using an associated web client.
 * 
 * @author Jordan Deyton
 *
 */
public class ProxyFeature {

	/**
	 * The set of allowed values for the feature.
	 */
	public final Set<String> allowedValues;
	/**
	 * Whether or not the visualization can be colored based on this feature. If
	 * false, then the coloring of the visualization is essentially unset when
	 * this feature's value is applied.
	 */
	public final boolean canColorBy;
	/**
	 * The "location" for coloring. The default is
	 * {@link ColorByLocation#POINTS}.
	 */
	public final ColorByLocation colorByLocation;
	/**
	 * The mode for coloring. The default is {@link ColorByMode#SOLID}.
	 */
	public final ColorByMode colorByMode;
	/**
	 * The index of the feature in the file proxy's "ui" and "properties"
	 * JsonArrays.
	 */
	public final int index;

	/**
	 * The name of the feature in the file proxy's "ui" JsonArray.
	 */
	public final String name;

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
	public ProxyFeature(int index, String uiName, String propertyName) {
		this(index, uiName, propertyName, null, null);
	}

	/**
	 * The full constructor.
	 * 
	 * @param index
	 *            The index of the feature in the file proxy's "ui" and
	 *            "properties" JsonArrays.
	 * @param uiName
	 *            The name of the feature in the file proxy's "ui" JsonArray.
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
	public ProxyFeature(int index, String uiName, String propertyName,
			ColorByMode colorByMode, ColorByLocation colorByLocation) {

		this.index = index;
		this.name = uiName;
		this.propertyName = propertyName;
		this.canColorBy = colorByLocation != null;
		this.colorByMode = (colorByMode != null ? colorByMode
				: ColorByMode.SOLID);
		this.colorByLocation = (colorByLocation != null ? colorByLocation
				: ColorByLocation.POINTS);

		// Initialize the maps allowed and selected values.
		allowedValues = new HashSet<String>();
		selectedValues = new HashSet<String>();

		return;
	}

	/**
	 * An enumeration for the allowed values of the "colorMode" parameter when
	 * telling the web client to color the visualization based on this feature.
	 * 
	 * @author Jordan Deyton
	 *
	 */
	public enum ColorByMode {
		SOLID, ARRAY;
	}

	/**
	 * An enumeration for the allowed values of the "arrayLocation" parameter
	 * when telling the web client to color the visualization based on this
	 * feature.
	 * 
	 * @author Jordan Deyton
	 *
	 */
	public enum ColorByLocation {
		POINTS, CELLS;
	}

}
