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
package org.eclipse.ice.client.widgets.reactoreditor.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ice.client.widgets.reactoreditor.IColorFactory;
import org.eclipse.ice.client.widgets.reactoreditor.LinearColorFactory;
import org.eclipse.ice.client.widgets.reactoreditor.LinearColorFactory.Theme;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link LinearColorFactory}'s implementation of
 * {@link IColorFactory} as well as its custom setters.
 * 
 * @author Jordan Deyton
 *
 */
public class LinearColorFactoryTester {

	/**
	 * The factory that will be tested.
	 */
	private LinearColorFactory factory;

	/**
	 * A list of mystery colors for testing purposes.
	 */
	private List<Integer> colors;

	/**
	 * Initializes the factory and color list.
	 */
	@Before
	public void beforeEachTest() {
		// Create the factory to test.
		factory = new LinearColorFactory();

		// Add some mystery colors to the array.
		colors = new ArrayList<Integer>();
		colors.add(0x112233); // mystery color
		colors.add(0xee1199); // mystery color
		colors.add(0xffeedd); // mystery color
	}

	/**
	 * Checks the default black-to-white color scheme and that normalized colors
	 * are found appropriately.
	 */
	@Test
	public void checkDefaultColors() {

		// Any normalized value <= 0.0 should return the color black.
		assertEquals(0x000000, factory.findColor(-1.0));
		assertEquals(0x000000, factory.findColor(0.0));

		// Any normalized value >= 1.0 should return the color white.
		assertEquals(0xffffff, factory.findColor(1.0));
		assertEquals(0xffffff, factory.findColor(1.0001));

		// Any normalized value between should return a proportional color.
		// These target values were calculated by hand combining the two colors
		// based on their respective percentages of each RGB byte.
		assertEquals(0x808080, factory.findColor(0.5));
		assertEquals(0x4d4d4d, factory.findColor(0.3));
	}

	/**
	 * Checks that the normalized color is correctly computed with more than 2
	 * colors in the scale.
	 */
	@Test
	public void checkMultipleColors() {
		factory.setColors(colors);

		// Any normalized value <= 0.0 should return the first color.
		assertEquals(0x112233, factory.findColor(-1.0));
		assertEquals(0x112233, factory.findColor(0.0));

		// Any normalized value >= 1.0 should return the last color.
		assertEquals(0xffeedd, factory.findColor(1.0));
		assertEquals(0xffeedd, factory.findColor(1.0001));

		// Any normalized value lying on a specific color index should return
		// that color.
		assertEquals(0xee1199, factory.findColor(0.5));

		// These target values were calculated by hand combining the two colors
		// based on their respective percentages of each RGB byte.
		assertEquals(0x961870, factory.findColor(0.3));
		assertEquals(0xf780bb, factory.findColor(0.75));

		return;
	}

	/**
	 * Checks that the color setter based on a list of integers works. It also
	 * checks that it ignores bad input (null list, list with nulls, list with
	 * less than 2 colors).
	 */
	@Test
	public void checkSetColorsWithList() {
		List<Integer> badColors;

		// Using a null list should change nothing.
		badColors = null;
		factory.setColors(badColors);
		// Check that the values are still the default.
		assertEquals(0x000000, factory.findColor(0.0));
		assertEquals(0xffffff, factory.findColor(1.0));

		// Using a list that is too small should change nothing.
		badColors = new ArrayList<Integer>();
		badColors.add(colors.get(0));
		factory.setColors(badColors);
		// Check that the values are still the default.
		assertEquals(0x000000, factory.findColor(0.0));
		assertEquals(0xffffff, factory.findColor(1.0));

		// Using a list that has nulls should change nothing.
		badColors = new ArrayList<Integer>();
		badColors.add(colors.get(0));
		badColors.add(null);
		badColors.add(colors.get(2));
		factory.setColors(badColors);
		// Check that the values are still the default.
		assertEquals(0x000000, factory.findColor(0.0));
		assertEquals(0xffffff, factory.findColor(1.0));

		// It should work with a valid list.
		// Make the middle color a large negative number. The sign and extra
		// bits should be ignored.
		colors.set(1, -((1 << 24) | colors.get(1)));
		factory.setColors(colors);
		colors.set(1, colors.get(1) & 0xffffff); // Restore the middle number.
		// Check that the values are new.
		assertEquals((int) colors.get(0), factory.findColor(0.0));
		assertEquals((int) colors.get(1), factory.findColor(0.5));
		assertEquals((int) colors.get(2), factory.findColor(1.0));

		return;
	}

	/**
	 * Checks that the color setter based on two integers works. It also checks
	 * that it ignores signs and extraneous bits in the integer values.
	 */
	@Test
	public void checkSetColorsWithIntegers() {
		// Negative signs and extra bits on the left are trimmed.
		factory.setColors(0xffffffff, -0xff000000);
		// Check that the values have been set (reversed from the default).
		assertEquals(0xffffff, factory.findColor(0.0));
		assertEquals(0x000000, factory.findColor(1.0));
	}

	/**
	 * Checks that the color setter based on the theme works (with and without
	 * the optional reverse boolean). It also checks that it refuses bad input
	 * arguments.
	 */
	@Test
	public void checkSetColorsWithTheme() {

		Theme theme;

		// Passing in a null theme should have no effect.
		theme = null;
		factory.setColors(theme);
		// Check that the values are still the default.
		assertEquals(0x000000, factory.findColor(0.0));
		assertEquals(0xffffff, factory.findColor(1.0));

		// Passing in a null theme (full setter) should have no effect.
		theme = null;
		factory.setColors(theme, false);
		factory.setColors(theme, true);
		// Check that the values are still the default.
		assertEquals(0x000000, factory.findColor(0.0));
		assertEquals(0xffffff, factory.findColor(1.0));

		// Passing in a valid theme should update the colors.
		theme = Theme.Rainbow;
		factory.setColors(theme);
		// Check that the values are new.
		assertEquals(0x0000ff, factory.findColor(0.0)); // blue
		assertEquals(0x00ff00, factory.findColor(0.5)); // green
		assertEquals(0xff0000, factory.findColor(1.0)); // red

		// Check that the reverse boolean works.
		theme = Theme.Rainbow;
		factory.setColors(theme, true);
		// Check that the values are reversed.
		assertEquals(0xff0000, factory.findColor(0.0)); // red
		assertEquals(0x00ff00, factory.findColor(0.5)); // green
		assertEquals(0x0000ff, factory.findColor(1.0)); // blue

		// Try the other option (not reversed).
		theme = Theme.BlackToWhite;
		factory.setColors(theme, false);
		// Check that the values are new.
		assertEquals(0x000000, factory.findColor(0.0));
		assertEquals(0xffffff, factory.findColor(1.0));

		return;
	}

}
