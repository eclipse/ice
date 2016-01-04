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
package org.eclipse.ice.client.widgets.analysis;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides a linear-interpolation-based implementation of
 * {@link IColorFactory}. That is, given the normalized value, the resulting
 * color is based on a simple linear interpolation between two colors.
 * 
 * <p>
 * This factory also supports multi-colored scales, in which case the resulting
 * color depends on which two colors correspond to the normalized value's
 * position between 0 and 1. Several pre-defined themes are available in the
 * {@link Theme} enumeration.
 * </p>
 * 
 * @author Jordan Deyton
 * 
 */
public class LinearColorFactory extends AbstractColorFactory {

	/**
	 * An enum for built-in color themes supported by this factory.
	 * 
	 * @author Jordan Deyton
	 *
	 */
	public enum Theme {
		/**
		 * The default color scheme of black to white.
		 */
		BlackToWhite(0x000000, 0xffffff),

		/**
		 * A grayscale theme from dark gray ({@literal #f0f0f0}) to light gray (
		 * {@literal #636363}).
		 */
		GrayScale(0xf0f0f0, 0x636363),

		/**
		 * A red theme from very light ({@literal #fee0d2}) red to red (
		 * {@literal #de2d26}).
		 */
		Red(0xffffff, 0xde2d26),

		/**
		 * A green theme from very light green ({@literal #e5f5e0}) to green (
		 * {@literal #31a354}).
		 */
		Green(0xffffff, 0x31a354),

		/**
		 * A blue theme from very light blue ({@literal #deebf7}) to blue (
		 * {@literal #3182bd}).
		 */
		Blue(0xffffff, 0x3182bd),

		/**
		 * A purple theme from very light purple ({@literal #efedf5}) to purple
		 * ( {@literal #756bb1}).
		 */
		Purple(0xffffff, 0x756bb1),

		/**
		 * A theme that goes from blue ({@literal #3366ff}) to white (
		 * {@literal #ffffff}) to red ({@literal #e62e2e})
		 */
		BlueRed(0x3366ff, 0xffffff, 0xe62e2e),

		/**
		 * A rainbow color scheme. Not suggested, but available for consistency.
		 */
		Rainbow(0x0000ff, 0x00ffff, 0x00ff00, 0xffff00, 0xff0000),

		/**
		 * A multi-hue color scheme that goes from dark blue, to yellow, to dark
		 * orange.
		 */
		Rainbow2(0x000099, 0xffff00, 0xe76005);

		/**
		 * This theme's array of colors.
		 */
		private final int[] colors;

		/**
		 * The constructor for an enum value.
		 * 
		 * @param colors
		 *            The colors for the theme.
		 */
		private Theme(int... colors) {
			this.colors = colors;
		}

		/**
		 * Adds the colors for the theme to the specified list.
		 * 
		 * @param list
		 *            The list to add colors to.
		 */
		protected void addColorsToList(List<Integer> list) {
			addColorsToList(list, false);
		}

		/**
		 * Adds the colors for the theme to the specified list.
		 * 
		 * @param list
		 *            The list to add colors to.
		 * @param reversed
		 *            If true, the colors will be added in the reverse order.
		 */
		protected void addColorsToList(List<Integer> list, boolean reversed) {
			// If the colors should not be inverted, simple add them.
			if (!reversed) {
				for (int color : colors) {
					list.add(color);
				}
			}
			// Otherwise, we need to iterate over the color array in reverse.
			else {
				for (int i = colors.length - 1; i >= 0; i--) {
					list.add(colors[i]);
				}
			}
			return;
		}
	}

	/**
	 * The current list of colors used for the scale. Each color is stored as a
	 * hex triplet (3 bytes: red, green, and blue).
	 */
	private final ArrayList<Integer> colors;

	/**
	 * The number of sections of the color scale. This is equivalent to the size
	 * of the color array minus 1.
	 */
	private int n;

	/**
	 * The default constructor.
	 */
	public LinearColorFactory() {
		colors = new ArrayList<Integer>(2);

		// Set up the default color range.
		Theme.BlackToWhite.addColorsToList(colors);
		n = colors.size() - 1;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.client.widgets.reactoreditor.IColorFactory#findColor(double)
	 */
	@Override
	public int findColor(double value) {

		final int color;

		// Clamp negative values to the first color in the gradient.
		if (Double.compare(0.0, value) >= 0) {
			color = colors.get(0);
		}
		// Clamp numbers greater than 1.0 to the last color in the gradient.
		else if (Double.compare(1.0, value) <= 0) {
			color = colors.get(n);
		}
		// For any other numbers, we perform a linear interpolation based on the
		// two adjacent colors.
		else {
			// Find the segment (between i and i+1) into which the value falls.
			double xn = value * (double) n;
			double id = Math.floor(xn);
			int i = (int) id;

			// Compute the percentages of the previous and next color.
			double pi1 = xn - id; // next color percentage
			double pi = 1.0 - pi1; // previous color percentage

			// Get the previous and next colors.
			int colori = colors.get(i);
			int colori1 = colors.get(i + 1);

			// Compute the red, green, and blue bytes based on the percentages
			// of the previous and next colors.
			int red = (int) Math.round(pi * (double) (colori >> 16)
					+ pi1 * (double) (colori1 >> 16));
			int green = (int) Math.round(pi * (double) ((colori >> 8) & 0x00ff)
					+ pi1 * (double) ((colori1 >> 8) & 0x00ff));
			int blue = (int) Math.round(pi * (double) (colori & 0xff)
					+ pi1 * (double) (colori1 & 0xff));

			// Combine the red, green, and blue into the color.
			color = (red << 16) | (green << 8) | blue;
		}

		return color;
	}

	/**
	 * Sets the colors for the factory to the two specified colors. Note that
	 * any sign or bits above the bits corresponding to {@literal #ffffff} are
	 * ignored.
	 * 
	 * @param firstColor
	 *            The first color, for values normalized to 0.
	 * @param secondColor
	 *            The second color, for values normalized to 1.
	 */
	public void setColors(int firstColor, int secondColor) {
		colors.clear();
		colors.add(firstColor & 0xffffff);
		colors.add(secondColor & 0xffffff);
		n = 1;
	}

	/**
	 * Sets the colors for the factory to the specified colors in the list. Note
	 * that any sign or bits above the bits corresponding to {@literal #ffffff}
	 * are ignored.
	 * 
	 * @param colorList
	 *            The list of colors. {@code null} values are not accepted. The
	 *            list must also contain at least 2 colors.
	 */
	public void setColors(List<Integer> colorList) {
		// Make sure the specified list contains at least 2 colors. Null values
		// are prohibited.
		if (colorList != null && colorList.size() >= 2
				&& !colorList.contains(null)) {
			colors.clear();
			colors.ensureCapacity(colorList.size());
			for (Integer color : colorList) {
				colors.add(color & 0xffffff);
			}
			n = colors.size() - 1;
		}
		return;
	}

	/**
	 * Sets the factory's colors based on a pre-defined theme.
	 * 
	 * @param theme
	 *            The preferred theme. {@code null} values are ignored.
	 */
	public void setColors(Theme theme) {
		setColors(theme, false);
	}

	/**
	 * Sets the factory's colors based on a pre-defined theme.
	 * 
	 * @param theme
	 *            The preferred theme. {@code null} values are ignored.
	 * @param reversed
	 *            If true, the color theme will be reversed.
	 */
	public void setColors(Theme theme, boolean reversed) {
		if (theme != null) {
			colors.clear();
			theme.addColorsToList(colors, reversed);
			n = colors.size() - 1;
		}
	}

}
