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
package org.eclipse.ice.client.widgets.reactoreditor;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

/**
 * This interface is used for situation where a color needs to be derived based
 * on a double value normalized between 0 and 1. Implementations may also ignore
 * the normalized value and return a pre-determined color. Colors are
 * represented by a hex triplet (3 bytes: red, green, and blue), although the
 * interface provides a convenient method for creating an SWT color for the hex
 * color.
 * 
 * @author Jordan Deyton
 *
 */
public interface IColorFactory {

	/**
	 * Creates a color based on the specified hexadecimal value.
	 * 
	 * @param display
	 *            The display used to create the color. If {@code null}, the
	 *            default display will be found.
	 * @param hex
	 *            The hex triplet (3 bytes) for the color, usually found by
	 *            calling {@link #findColor(double)}. Any sign or bits above the
	 *            bits corresponding to {@literal #ffffff} are ignored.
	 * @return A new color for the hex value.
	 */
	public Color createColor(Display display, int hex);

	/**
	 * Gets an integer value corresponding to a hexadecimal triplet for a color.
	 * Thus, a returned value will be in the range {@literal #000000} to
	 * {@literal #ffffff}. The red, green, and blue components are the first,
	 * second, and third bytes. For example, if the returned value is
	 * {@literal #aabbcc}, the red component is aa, the green bb, and blue cc.
	 * 
	 * @param value
	 *            The normalized value from which a color will be derived, if
	 *            applicable to the factory's implementation. If unknown, this
	 *            value may be left as {@code 0.0}. Values outside the range
	 *            {@literal [0.0, 1.0]} will be clamped to the range.
	 * @return The integer value corresponding to the hex representation of the
	 *         color, based on the input value.
	 */
	public int findColor(double value);

}
