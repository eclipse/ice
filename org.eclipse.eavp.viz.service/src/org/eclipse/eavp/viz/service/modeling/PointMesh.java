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
package org.eclipse.eavp.viz.service.modeling;

import java.util.HashMap;
import java.util.Set;

import org.eclipse.eavp.viz.service.datastructures.VizObject.SubscriptionType;

/**
 * A mesh component representing a point in three dimensional space.
 * 
 * @Author Robert Smith
 */
public class PointMesh extends AbstractMesh {

	/**
	 * The point's x coordinate
	 */
	protected double x;

	/**
	 * The point's y coordinate
	 */
	protected double y;

	/**
	 * The point's z coordinate
	 */
	protected double z;

	/**
	 * The basic constructor
	 */
	public PointMesh() {
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
	public PointMesh(double x, double y, double z) {
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

		SubscriptionType[] eventTypes = { SubscriptionType.PROPERTY };
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

		SubscriptionType[] eventTypes = { SubscriptionType.PROPERTY };
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

		SubscriptionType[] eventTypes = { SubscriptionType.PROPERTY };
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

		// Queue the individual updates for each coordinate
		updateManager.enqueue();

		// Set each of the new coordinates
		setX(x);
		setY(y);
		setZ(z);

		// Send all updates
		updateManager.flushQueue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {

		// Create a new component, and make it a copy of this one.
		PointMesh clone = new PointMesh();
		clone.copy(this);
		return clone;
	}

	/**
	 * Copies the contents of another AbstractMeshComponent into this one.
	 * 
	 * @param otherObject
	 *            The object which will be copied into this.
	 */
	@Override
	public void copy(AbstractMesh otherObject) {

		// Copy each of the other component's data members
		type = otherObject.type;
		properties = new HashMap<String, String>(otherObject.properties);

		// Copy the coordinates
		x = ((PointMesh) otherObject).getX();
		y = ((PointMesh) otherObject).getY();
		z = ((PointMesh) otherObject).getZ();

		// Clone each child entity
		for (String category : otherObject.entities.keySet()) {
			for (AbstractController entity : otherObject
					.getEntitiesByCategory(category)) {
				addEntityByCategory((AbstractController) entity.clone(),
						category);
			}
		}

		// Notify listeners of the change
		SubscriptionType[] eventTypes = { SubscriptionType.PROPERTY };
		updateManager.notifyListeners(eventTypes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.AbstractMeshComponent#equals(java.
	 * lang.Object)
	 */
	@Override
	public boolean equals(Object otherObject) {

		// Check if the objects are the same
		if (this == otherObject) {
			return true;
		}

		// Check if the other object is an AbstractMeshComponent and cast it
		if (!(otherObject instanceof PointMesh)) {
			return false;
		}

		PointMesh castObject = (PointMesh) otherObject;

		// Check the types and properties for equality
		if (type != castObject.type
				|| !properties.equals(castObject.properties)) {
			return false;
		}

		// Get the categories of the entities map, disregarding edges
		Set<String> categories = entities.keySet();
		categories.remove("Edges");
		Set<String> newCategories = entities.keySet();
		newCategories.remove("Edges");

		// If the vertices have different categories, they are not equal
		if (!categories.equals(newCategories)) {
			return false;
		}

		// Check that the positions are equal
		if (x != castObject.getX() || y != castObject.getY()
				|| z != castObject.getZ()) {
			return false;
		}

		// For each category, check that the two objects' lists of child
		// entities in that category are equal.
		for (String category : categories) {
			if (!entities.get(category)
					.containsAll(castObject.entities.get(category))
					|| !castObject.entities.get(category)
							.containsAll(entities.get(category))) {
				return false;
			}
		}

		// All checks passed, so the objects are equal
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 9;
		hash += 31 * type.hashCode();
		for (String category : entities.keySet()) {

			// Ignore the Edges to prevent circular hashing
			if ("Edges".equals(category)) {
				continue;
			}

			for (AbstractController entity : getEntitiesByCategory(category)) {
				hash += 31 * entity.hashCode();
			}
		}
		hash += 31 * properties.hashCode();
		hash += 31 * x;
		hash += 31 * y;
		hash += 31 * z;
		return hash;
	}
}
