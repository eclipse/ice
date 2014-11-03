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

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

/**
 * <p>
 * The Class ColorScale creates a color scale based on the 3 overlapping
 * Gaussian functions representing bands of red, green and blue.
 * </p>
 * 
 * @author Eric J. Lingerfelt and Mike Guidry
 */
public class ColorScale {

	/**
	 * <p>
	 * The position of the Red color band.
	 * </p>
	 */
	private double x0R;

	/**
	 * <p>
	 * The position of the Green color band.
	 * </p>
	 */
	private double x0G;

	/**
	 * <p>
	 * The position of the Blue color band.
	 * </p>
	 */
	private double x0B;

	/**
	 * <p>
	 * The width of the Red color band.
	 * </p>
	 */
	private double aR;

	/**
	 * <p>
	 * The width of the Green color band.
	 * </p>
	 */
	private double aG;

	/**
	 * <p>
	 * The width of the Blue color band.
	 * </p>
	 */
	private double aB;

	/**
	 * <p>
	 * The Constructor.
	 * </p>
	 * 
	 * @param x0R
	 *            the position of the Red color band
	 * @param x0G
	 *            the position of the Green color band
	 * @param x0B
	 *            the position of the Blue color band
	 * @param aR
	 *            the width of the Red color band
	 * @param aG
	 *            the width of the Green color band
	 * @param aB
	 *            the width of the Blue color band
	 */
	public ColorScale(double x0R, double x0G, double x0B, double aR, double aG,
			double aB) {

		this.x0R = x0R;
		this.x0G = x0G;
		this.x0B = x0B;
		this.aR = aR;
		this.aG = aG;
		this.aB = aB;

	}

	/**
	 * <p>
	 * Returns a color for the provided value between 0 and 1. If the provided
	 * value is less than 0, then it will be set to 0. If the value is greater
	 * than 1, then it will be set to 1.
	 * </p>
	 * 
	 * @param x
	 *            a value between 0 and 1
	 * @return the color
	 */
	public Color getColor(double x) {

		if (x >= 1.0) {
			x = 1.0;
		}
		if (x <= 0.0) {
			x = 0.0;
		}

		int red = (int) (255 * Math.exp(-(x - x0R) * (x - x0R) / aR / aR));
		int green = (int) (255 * Math.exp(-(x - x0G) * (x - x0G) / aG / aG));
		int blue = (int) (255 * Math.exp(-(x - x0B) * (x - x0B) / aB / aB));

		return new Color(Display.getCurrent(), red, green, blue);
	}

	/**
	 * <p>
	 * Gets the position of the Red color band.
	 * </p>
	 * 
	 * @return the position of the Red color band
	 */
	public double getX0R() {
		return this.x0R;
	}

	/**
	 * <p>
	 * Gets the position of the Green color band.
	 * </p>
	 * 
	 * @return the position of the Green color band
	 */
	public double getX0G() {
		return this.x0G;
	}

	/**
	 * <p>
	 * Gets the position of the Blue color band.
	 * </p>
	 * 
	 * @return the position of the Blue color band
	 */
	public double getX0B() {
		return this.x0B;
	}

	/**
	 * <p>
	 * Gets the width of the Red color band.
	 * </p>
	 * 
	 * @return the width of the Red color band
	 */
	public double getAR() {
		return this.aR;
	}

	/**
	 * <p>
	 * Gets the width of the Green color band.
	 * </p>
	 * 
	 * @return the width of the Green color band
	 */
	public double getAG() {
		return this.aG;
	}

	/**
	 * <p>
	 * Gets the width of the Blue color band.
	 * </p>
	 * 
	 * @return the width of the Blue color band
	 */
	public double getAB() {
		return this.aB;
	}

}
