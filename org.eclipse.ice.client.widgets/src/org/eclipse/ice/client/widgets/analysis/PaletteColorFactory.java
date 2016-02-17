/*******************************************************************************
 * Copyright (c) 2014, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation
 *   Jordan Deyton - bug 474742
 *   
 *******************************************************************************/
package org.eclipse.ice.client.widgets.analysis;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

/**
 * A this color factory can be used to get the next available color from a
 * pre-defined palette. Currently, this is intended to be used for trace colors
 * in SWT XYGraph, since there does not appear to be a way to dynamically reset
 * the palette used by XYGraph instances.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class PaletteColorFactory extends AbstractColorFactory {

	/**
	 * A map of known colors, keyed on their hex values.
	 */
	private final Map<Integer, Color> colorMap;

	/**
	 * An iterator for the current map of colors.
	 */
	private Iterator<Entry<Integer, Color>> iterator;

	/**
	 * The default constructor. Initializes the default color palette.
	 */
	public PaletteColorFactory() {

		// Create an ArrayList for colors.
		colorMap = new LinkedHashMap<Integer, Color>();

		// Add default colors.
		addColor(ColorConstants.black);
		addColor(ColorConstants.blue);
		addColor(ColorConstants.red);
		addColor(ColorConstants.darkGreen);
		addColor(ColorConstants.darkBlue);
		addColor(ColorConstants.orange);
		addColor(ColorConstants.yellow);
		addColor(ColorConstants.cyan);
		addColor(ColorConstants.green);
		addColor(ColorConstants.darkGray);
		addColor(ColorConstants.lightGreen);
		addColor(ColorConstants.lightBlue);

		// Set the initial iterator.
		reset();

		return;
	}

	/**
	 * Adds the specified color to the map of colors.
	 * 
	 * @param color
	 *            The color to add.
	 */
	private void addColor(Color color) {
		int hex = color.getRed() << 16 | color.getGreen() << 8
				| color.getBlue();
		colorMap.put(hex, color);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.reactoreditor.AbstractColorFactory#
	 * createColor(org.eclipse.swt.widgets.Display, int)
	 */
	@Override
	public Color createColor(Display display, int hex) {
		// Re-use the known colors if possible.
		Color color = colorMap.get(hex);
		return color != null ? color : super.createColor(display, hex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.reactoreditor.IColorFactory#findColor(
	 * double)
	 */
	@Override
	public int findColor(double value) {
		return getNextColor();
	}

	/**
	 * Get the next color's hex value.
	 * 
	 * @return The next color's hex value.
	 */
	public int getNextColor() {
		// If necessary, reset the iterator.
		if (!iterator.hasNext()) {
			reset();
		}
		return iterator.next().getKey();
	}

	/**
	 * Resets the color palette so that the next color will be the first in the
	 * palette.
	 */
	public void reset() {
		iterator = colorMap.entrySet().iterator();
	}

}
