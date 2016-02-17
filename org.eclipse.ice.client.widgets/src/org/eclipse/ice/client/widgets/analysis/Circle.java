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
package org.eclipse.ice.client.widgets.analysis;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.swt.SWT;

/**
 * This class provides a strictly circular shape. draw2d only provides ellipses
 * by default, but this shape will always remain circular. To specify the size
 * of the shape, the constructor expects the shape's radius and the maximum
 * radius. You can think of this as the proportion of the circle's diameter to
 * the minimum diameter of the Shape's container.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class Circle extends Shape {

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
	public Circle(double radius, double maxRadius) {
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

		// A point is inside the circle if:
		// (point.x - center.x)^2 + (point.y - center.y)^2 < radius^2
		// (inside circle, use <; on it, use ==; outside it, use >)

		// Compute the radius in pixels.
		long r = (long) (Math.min(bounds.width, bounds.height) * radius
				/ (2.0 * maxRadius));

		// Calculate the offset of x and y w.r.t. the center.
		long nx = x - bounds.x - bounds.width / 2;
		long ny = y - bounds.y - bounds.height / 2;

		// Checks the above equation and returns true if valid.
		return ((nx * nx + ny * ny) << 10) / (r * r) <= 1024;
	}

	/**
	 * Fills the circle using a graphics object. This should not be called
	 * manually, but by the system.
	 */
	@Override
	protected void fillShape(Graphics g) {
		// Makes sure the circles drawn are pretty, not jagged.
		g.setAntialias(SWT.ON);

		// Compute the center point.
		int centerX = bounds.x + bounds.width / 2;
		int centerY = bounds.y + bounds.height / 2;

		// Compute the radius in pixels.
		int r = (int) (Math.min(bounds.width, bounds.height) * radius
				/ (2.0 * maxRadius));

		// Use the computed bounds to draw the circle.
		g.fillOval(centerX - r, centerY - r, r * 2, r * 2);
	}

	/**
	 * Draws the outline (or border) of the shape. This is currently
	 * unimplemented. If you want to disable the outline, use
	 * Circle.setOutline(false).
	 */
	@Override
	protected void outlineShape(Graphics g) {
		// FIXME Disabled for now.
		// // Makes sure the circles drawn are pretty, not jagged.
		// g.setAntialias(SWT.ON);
		//
		// // Compute the center point.
		// int centerX = bounds.x + bounds.width / 2;
		// int centerY = bounds.y + bounds.height / 2;
		//
		// // Compute the radius in pixels.
		// int r = (int) (Math.min(bounds.width, bounds.height) * radius / (2.0
		// * maxRadius));
		//
		// // Use the computed bounds to draw the circle.
		// g.drawOval(centerX - r, centerY - r, r * 2, r * 2);
	}

}
