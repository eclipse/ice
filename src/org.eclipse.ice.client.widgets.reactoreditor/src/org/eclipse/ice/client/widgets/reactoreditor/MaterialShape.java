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

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;

/**
 * This class is similar to the Circle class but instead provides a rectangle.
 * To specify the size of the shape, the constructor expects the shape's radius
 * and the maximum radius. You can think of this as the proportion of the
 * rectangle's width to the width of the Shape's container.
 * 
 * @author djg
 * 
 */
public class MaterialShape extends Shape {

	/**
	 * The maximum radius (not in pixels). Use this if you want to draw the
	 * circle smaller.
	 */
	private double maxRadius;
	/**
	 * The radius of this circle (not in pixels).
	 */
	private double radius;

	/**
	 * The constructor for a circle shape.
	 * 
	 * @param radius
	 *            The radius of the circle. This value is NOT in pixels.
	 * @param maxRadius
	 *            The maximum radius used for drawing circles (not in pixels).
	 */
	public MaterialShape(double radius, double maxRadius) {
		this.radius = radius;
		this.maxRadius = maxRadius;
	}

	/**
	 * Returns true if the point (x, y) is contained within this circle.
	 * 
	 * @param x
	 *            The x coordinate of the point to test.
	 * @param y
	 *            The y coordinate of the point to test.
	 * @return Returns true if the point is contained within the circle, false
	 *         otherwise.
	 */
	@Override
	public boolean containsPoint(int x, int y) {
		// draw2d.Ellipse does this. It seems to have no effect.
		if (!super.containsPoint(x, y)) {
			return false;
		}

		// Compute the radius in pixels.
		long r = (long) (bounds.width * radius / (2.0 * maxRadius));
		long nx = x - bounds.x;
		long ny = y - bounds.y;

		// Calculate the offset of x and y w.r.t. the center.
		if ((ny < 0) || (ny > bounds.height)) {
			return false;
		} else if ((nx < bounds.width - r) || (nx > bounds.width - r)) {
			return false;
		}
		return true;
	}

	/**
	 * Actually draws the circle using the radius and max radius.
	 */
	@Override
	protected void fillShape(Graphics g) {
		// Compute the center (x-value).
		int center = bounds.x + bounds.width / 2;

		// Compute the radius in pixels.
		int r = (int) (bounds.width * radius / (2.0 * maxRadius));

		// Draw the rectangle.
		g.fillRectangle(center - r, bounds.y, r * 2, bounds.height);
	}

	/**
	 * Outlines the circle. For now, there is no outline.
	 */
	@Override
	protected void outlineShape(Graphics g) {
		// No outline for now.
	}

}
