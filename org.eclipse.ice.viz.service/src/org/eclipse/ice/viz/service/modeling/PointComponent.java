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

import org.eclipse.ice.viz.service.datastructures.VizObject.UpdateableSubscription;

/**
 * A mesh component representing a point in three dimensional space.
 * 
 * @Author Robert Smith
 */
public class PointComponent extends AbstractMeshComponent {

	/**
	 * The point's x coordinate
	 */
	private double x;

	/**
	 * The point's y coordinate
	 */
	private double y;

	/**
	 * The point's z coordinate
	 */
	private double z;

	/**
	 * The basic constructor
	 */
	public PointComponent() {
		super();

		// Initialize the location to the origin.
		x = 0;
		y = 0;
		z = 0;
	}

	/**
	 * A constructor for specifying the point's location
	 * 
	 * @param x
	 *            The point's x coordinate
	 * @param y
	 *            The point's y coordinate
	 * @param z
	 *            The point's z coordinate
	 */
	public PointComponent(double x, double y, double z) {
		super();

		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Getter for the x coordinate.
	 * 
	 * @return The x coordinate
	 */
	public double getX() {
		return x;
	}

	/**
	 * Setter for the x coordinate
	 * 
	 * @param x
	 *            The point's new x coordinate
	 */
	public void setX(double x) {
		this.x = x;

		UpdateableSubscription[] eventTypes = {UpdateableSubscription.Location};
		updateManager.notifyListeners(eventTypes);
	}

	/**
	 * Getter for the y coordinate
	 * 
	 * @return The y coordinate
	 */
	public double getY() {
		return y;
	}

	/**
	 * Setter for the y coordinate
	 * 
	 * @param y
	 *            The new y coordinate
	 */
	public void setY(double y) {
		this.y = y;

		UpdateableSubscription[] eventTypes = {UpdateableSubscription.Location};
		updateManager.notifyListeners(eventTypes);
	}

	/**
	 * Getter for the z coordinate
	 * 
	 * @return The z coordinate
	 */
	public double getZ() {
		return z;
	}

	/**
	 * Setter for the z coordinate
	 * 
	 * @param z
	 *            The new z coordinate
	 */
	public void setZ(double z) {
		this.z = z;

		UpdateableSubscription[] eventTypes = {UpdateableSubscription.Location};
		updateManager.notifyListeners(eventTypes);
	}

	/**
	 * Returns a vector describing the point's location in three dimensional
	 * space
	 */
	public double[] getLocation() {

		// Create a list of the x, y, and z coordinates.
		double location[] = { x, y, z };
		return location;
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

		// Set each of the new coordinates
		setX(x);
		setY(y);
		setZ(z);
	}

}
