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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.graphics.Color;

/**
 * A ColorFactory can be used to get the next available color from a pre-defined
 * palette. Currently, this is intended to be used for trace colors in SWT
 * XYGraph, since there does not appear to be a way to dynamically reset the
 * palette used by XYGraph instances.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class ColorFactory {

	/**
	 * The index for the current color in the palette.
	 */
	private int index;

	/**
	 * The color palette itself.
	 */
	private List<Color> colors;

	/**
	 * The default constructor. Initializes the default color palette.
	 */
	public ColorFactory() {
		// Set the initial index.
		index = 0;

		// Create an ArrayList for colors.
		colors = new ArrayList<Color>();

		// Populate the list with some colors.
		colors.add(ColorConstants.black);
		colors.add(ColorConstants.blue);
		colors.add(ColorConstants.red);
		colors.add(ColorConstants.darkGreen);
		colors.add(ColorConstants.darkBlue);
		colors.add(ColorConstants.orange);
		colors.add(ColorConstants.yellow);
		colors.add(ColorConstants.cyan);
		colors.add(ColorConstants.green);
		colors.add(ColorConstants.darkGray);
		colors.add(ColorConstants.lightGreen);
		colors.add(ColorConstants.lightBlue);
	}

	/**
	 * Resets the color palette so that the next color will be the first in the
	 * palette.
	 */
	public void reset() {
		index = 0;
	}

	/**
	 * Gets the next color from the palette.
	 * 
	 * @return An SWT color.
	 */
	public Color getNextColor() {
		index %= colors.size();
		return colors.get(index++);
	}
}
