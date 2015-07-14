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

/**
 * A class representing a feature for an {@link IParaViewProxy}. It contains the
 * minimal information required to draw it using an associated web client.
 * 
 * @author Jordan Deyton
 *
 */
public class ProxyFeature {

	/**
	 * The name of the feature, or its category.
	 */
	public final String name;
	/**
	 * Whether or not the visualization can be colored based on this feature. If
	 * false, then the coloring of the visualization is essentially unset when
	 * this feature's value is applied.
	 */
	public final boolean canColorBy;
	/**
	 * The mode for coloring. The default is {@link ColorByMode#SOLID}.
	 */
	public final ColorByMode colorByMode;
	/**
	 * The "location" for coloring. The default is
	 * {@link ColorByLocation#POINTS}.
	 */
	public final ColorByLocation colorByLocation;

	/**
	 * The default constructor.
	 * 
	 * @param name
	 *            The name of the feature type (or its category).
	 * @param colorByMode
	 *            The mode for coloring. If {@code null}, it defaults to
	 *            {@link ColorByMode#SOLID}.
	 * @param colorByLocation
	 *            The "location" for coloring. If {@code null}, it defaults to
	 *            {@link ColorByLocation#POINTS}.
	 */
	public ProxyFeature(String name, ColorByMode colorByMode,
			ColorByLocation colorByLocation) {
		this.name = name;
		this.canColorBy = colorByLocation != null;
		this.colorByLocation = (colorByLocation != null ? colorByLocation
				: ColorByLocation.POINTS);
		this.colorByMode = (colorByMode != null ? colorByMode
				: ColorByMode.SOLID);
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
