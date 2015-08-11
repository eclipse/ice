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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.ice.client.widgets.reactoreditor.IColorFactory;
import org.eclipse.ice.client.widgets.reactoreditor.PaletteColorFactory;
import org.eclipse.swt.graphics.Color;
import org.junit.Test;

/**
 * Tests the {@link PaletteColorFactory}'s implementation of
 * {@link IColorFactory} as well as its custom getters and setters.
 * 
 * @author Jordan Deyton
 *
 */
public class PaletteColorFactoryTester {

	/**
	 * The factory that will be tested.
	 */
	private PaletteColorFactory factory = new PaletteColorFactory();

	/**
	 * Checks the contents of the factory's palette as well as that it cycles.
	 */
	@Test
	public void checkPalette() {

		// Create a list containing all expected colors.
		List<Color> colors = new ArrayList<Color>();
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

		// Loop over the list and check that each one's hex and color values are
		// returned by the factory.
		boolean useGetter = false;
		for (Color color : colors) {
			int expectedHex = color.getRed() << 16 | color.getGreen() << 8
					| color.getBlue();

			// Check either the custom getter or the default IColorFactory find
			// method.
			if (useGetter) {
				assertEquals(expectedHex, factory.getNextColor());
			} else {
				assertEquals(expectedHex, factory.findColor(0.0));
			}
			useGetter = !useGetter;

			// Check that the Color is not instantiated.
			assertSame(color, factory.createColor(null, expectedHex));
		}

		// Check that it cycles back around.
		for (int i = 0; i < 2; i++) {
			Color color = colors.get(i);

			int expectedHex = color.getRed() << 16 | color.getGreen() << 8
					| color.getBlue();

			// Check either the custom getter or the default IColorFactory find
			// method.
			if (useGetter) {
				assertEquals(expectedHex, factory.getNextColor());
			} else {
				assertEquals(expectedHex, factory.findColor(0.0));
			}
			useGetter = !useGetter;

			// Check that the Color is not instantiated.
			assertSame(color, factory.createColor(null, expectedHex));
		}

		return;
	}

	/**
	 * Checks that the reset operations resets the colors returned by the
	 * factory.
	 */
	@Test
	public void checkReset() {

		// Get the hex values for the first and second colors.
		int firstColor = 0x000000; // black
		Color color = ColorConstants.blue;
		int secondColor = color.getRed() << 16 | color.getGreen() << 8
				| color.getBlue();

		// Grab the first two colors.
		assertEquals(firstColor, factory.getNextColor());
		assertEquals(secondColor, factory.getNextColor());

		// Reset.
		factory.reset();

		// Getting the next colors should start over with the first two colors.
		assertEquals(firstColor, factory.getNextColor());
		assertEquals(secondColor, factory.getNextColor());

		return;
	}

}
