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
package org.eclipse.eavp.viz.service.paraview.proxy;

/**
 * This class provides a proxy property for "features", or properties that
 * correspond to the names of data that can be rendered with the ParaView web
 * client.
 * 
 * @author Jordan Deyton
 *
 */
public abstract class ProxyFeature extends ProxyProperty {

	/**
	 * An enumeration for allowed values for the "location" of the visualization
	 * data for this feature.
	 * 
	 * @author Jordan Deyton
	 *
	 */
	public enum ColorByLocation {
		POINTS, CELLS;
	}
	/**
	 * An enumeration for allowed values for the mode to use when coloring the
	 * visualization with a feature.
	 * 
	 * @author Jordan Deyton
	 *
	 */
	public enum ColorByMode {
		SOLID, ARRAY;
	}
	/**
	 * True if the view can be colored by the feature, false otherwise (in which
	 * case a solid color should be used).
	 */
	protected final boolean canColorBy;

	/**
	 * The mode to use when coloring the visualization with this feature.
	 */
	protected final ColorByMode colorByMode;

	/**
	 * The "location" of the visualization data for this feature.
	 */
	protected final ColorByLocation colorByLocation;

	/**
	 * The default constructor. A feature constructed with this method cannot be
	 * visualized with colors.
	 * 
	 * @param name
	 *            The name of the feature. This corresponds to the "name" value
	 *            in the corresponding array element in the "ui" array.
	 * @param index
	 *            The index of the feature in its parent proxy's "ui" and
	 *            "properties" JsonArrays.
	 */
	public ProxyFeature(String name, int index) {
		this(name, index, null, null, null);
	}

	/**
	 * The most commonly-used constructor. If either the mode or the location
	 * are {@code null} , the feature cannot be visualized with colors.
	 * 
	 * @param name
	 *            The name of the feature. This corresponds to the "name" value
	 *            in the corresponding array element in the "ui" array.
	 * @param index
	 *            The index of the feature in its parent proxy's "ui" and
	 *            "properties" JsonArrays.
	 * @param mode
	 *            The mode to use when coloring the visualization with this
	 *            feature.
	 * @param location
	 *            The "location" of the visualization data for this feature.
	 */
	public ProxyFeature(String name, int index, ColorByMode mode,
			ColorByLocation location) {
		this(name, index, null, mode, location);
	}

	/**
	 * The full constructor. If the property type is null, it defauls to
	 * discrete with multi-select enabled. If either the mode or the location
	 * are {@code null} , the feature cannot be visualized with colors.
	 * 
	 * @param name
	 *            The name of the feature. This corresponds to the "name" value
	 *            in the corresponding array element in the "ui" array.
	 * @param index
	 *            The index of the feature in its parent proxy's "ui" and
	 *            "properties" JsonArrays.
	 * @param type
	 *            The type of property. This dictates the format of the "values"
	 *            and "value" JsonElements in its "ui" and "properties" entries,
	 *            respectively.
	 * @param mode
	 *            The mode to use when coloring the visualization with this
	 *            feature.
	 * @param location
	 *            The "location" of the visualization data for this feature.
	 */
	public ProxyFeature(String name, int index, PropertyType type,
			ColorByMode mode, ColorByLocation location) {
		super(name, index, type != null ? type : PropertyType.DISCRETE_MULTI);

		canColorBy = (mode != null && location != null);
		colorByMode = (mode != null ? mode : ColorByMode.SOLID);
		colorByLocation = (location != null ? location
				: ColorByLocation.POINTS);
	}

}
