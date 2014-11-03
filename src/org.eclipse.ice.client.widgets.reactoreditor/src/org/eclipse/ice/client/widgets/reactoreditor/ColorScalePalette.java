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
package org.eclipse.ice.client.widgets.reactoreditor;

/**
 * An enum containing pre-configured {@link ColorScale}s. To get a color scale
 * from a literal, use {@link #getColorScale()}.
 * 
 * @author Jordan
 * 
 */
public enum ColorScalePalette {
	Rainbow1("Rainbow 1", 0.80, 0.60, 0.20, 0.50, 0.40, 0.30), Rainbow2(
			"Rainbow 2", 0.44, 0.20, 1.00, 0.25, 0.32, 0.37), PurpleHaze(
			"Purple Haze", 1.00, 0.00, 1.00, 0.84, 0.00, 0.84), Fire("Fire",
			1.00, 0.97, 0.00, 0.71, 0.33, 0.00), Grayscale("Grayscale", 1.00,
			1.00, 1.00, 1.00, 1.00, 1.00);

	/**
	 * A pretty-printed name for the palette.
	 */
	public final String name;

	/**
	 * The positions and widths of the defined ColorScale's RGB color bands.
	 */
	private final double x0R, x0G, x0B, aR, aG, aB;

	/**
	 * The default private constructor for the enum.
	 * 
	 * @param name
	 *            A pretty-printed name for the palette.
	 * @param x0R
	 *            The position of the Red color band.
	 * @param x0G
	 *            The position of the Green color band.
	 * @param x0B
	 *            The position of the Blue color band.
	 * @param aR
	 *            The width of the Red color band.
	 * @param aG
	 *            The width of the Green color band.
	 * @param aB
	 *            The width of the Blue color band.
	 */
	private ColorScalePalette(String name, double x0R, double x0G, double x0B,
			double aR, double aG, double aB) {
		this.name = name;
		this.x0R = x0R;
		this.x0G = x0G;
		this.x0B = x0B;
		this.aR = aR;
		this.aG = aG;
		this.aB = aB;
	}

	/**
	 * Constructs a new color scale based on the palette's RGB and alpha values.
	 * 
	 * @return A new {@link ColorScale} object.
	 */
	public ColorScale getColorScale() {
		return new ColorScale(x0R, x0G, x0B, aR, aG, aB);
	}
}
