/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.ice.viz.service.modeling;

/**
 * A controller for a Point model part.
 * 
 * @author Robert Smith
 *
 */
public class Point extends AbstractController {

	/**
	 * The default constructor.
	 * 
	 * @param model
	 *            The controller's model
	 * @param view
	 *            The controller's view
	 */
	public Point(PointComponent model, AbstractView view) {
		super(model, view);
	}

	/**
	 * Getter for the x coordinate.
	 * 
	 * @return The x coordinate
	 */
	public double getX() {
		return ((PointComponent) model).getX();
	}

	/**
	 * Setter for the x coordinate
	 * 
	 * @param x
	 *            The point's new x coordinate
	 */
	public void setX(double x) {
		((PointComponent) model).setX(x);
	}

	/**
	 * Getter for the y coordinate
	 * 
	 * @return The y coordinate
	 */
	public double getY() {
		return ((PointComponent) model).getY();
	}

	/**
	 * Setter for the y coordinate
	 * 
	 * @param y
	 *            The new y coordinate
	 */
	public void setY(double y) {
		((PointComponent) model).setY(y);
	}

	/**
	 * Getter for the z coordinate
	 * 
	 * @return The z coordinate
	 */
	public double getZ() {
		return ((PointComponent) model).getZ();
	}

	/**
	 * Setter for the z coordinate
	 * 
	 * @param z
	 *            The new z coordinate
	 */
	public void setZ(double z) {
		((PointComponent) model).setZ(z);
	}

	/**
	 * Returns a vector describing the point's location in three dimensional
	 * space
	 */
	public double[] getLocation() {
		return ((PointComponent) model).getLocation();
	}

	/**
	 * Set the point's location. This a convenience method for setting the x, y,
	 * and z coordinates with a single function.
	 * 
	 * @param x
	 *            The new x coordinate
	 * @param y
	 *            The new y coordinate
	 * @param z
	 *            The new z coordinate
	 */
	public void updateLocation(double x, double y, double z) {
		((PointComponent) model).updateLocation(x, y, z);
	}
}
