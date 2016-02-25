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

/**
 * The internal representation of a Tube part.
 * 
 * @author Robert Smith
 *
 */
public class TubeMesh extends ShapeMesh {

	/**
	 * The default constructor.
	 */
	public TubeMesh() {
		super();
		properties.put(MeshProperty.TYPE, "Tube");
	}

	/**
	 * A constructor which initializes a tube's length and radius.
	 * 
	 * @param length
	 *            The tube's length
	 * @param radius
	 *            The tube's radius
	 */
	public TubeMesh(double length, double radius) {
		this();
		setLength(length);
		setRadius(radius);
	}

	/**
	 * Convenience getter method for the tube's axial samples
	 * 
	 * @return The tube's length
	 */
	public int getAxialSamples() {

		// If the property is not set, return 0
		String axialSamples = properties.get(MeshProperty.RESOLUTION);
		if (axialSamples == null) {
			return 0;
		}
		return Integer.parseInt(axialSamples);
	}

	/**
	 * Convenience getter method for the tube's length
	 * 
	 * @return The tube's length
	 */
	public double getLength() {

		// If the property is not set, return 0
		String length = properties.get(MeshProperty.LENGTH);
		if (length == null) {
			return 0;
		}
		return Double.parseDouble(length);
	}

	/**
	 * Convenience getter method for the tube's radius
	 * 
	 * @return The tube's radius
	 */
	public double getRadius() {

		// If the property is not set, return 0
		String radius = properties.get(MeshProperty.RADIUS);
		if (radius == null) {
			return 0;
		}
		return Double.parseDouble(radius);
	}

	/**
	 * Convenience getter method for the radius of the tube's hole.
	 * 
	 * @return The hole's radius
	 */
	public double getInnerRadius() {
		// If the property is not set, return 0
		String radius = properties.get(MeshProperty.INNER_RADIUS);
		if (radius == null) {
			return 0;
		}
		return Double.parseDouble(radius);
	}

	/**
	 * Convenience setter method for the tube's axial samples
	 * 
	 * @param axialSamples
	 *            The tube's new axial samples
	 */
	public void setAxialSamples(int axialSamples) {
		setProperty(MeshProperty.RESOLUTION, Integer.toString(axialSamples));
	}

	/**
	 * Convenience setter method for the tube's length
	 * 
	 * @param length
	 *            The tube's new length
	 */
	public void setLength(double length) {
		setProperty(MeshProperty.LENGTH, Double.toString(length));
	}

	/**
	 * Convenience setter method for the tube's radius
	 * 
	 * @param radius
	 *            The tube's new radius
	 */
	public void setRadius(double radius) {
		setProperty(MeshProperty.RADIUS, Double.toString(radius));
	}

	/**
	 * Convenience setter method for the radius of the tube's hole.
	 * 
	 * @param radius
	 *            The hole's new radius
	 */
	public void setInnerRadius(double radius) {
		setProperty(MeshProperty.INNER_RADIUS, Double.toString(radius));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.AbstractMeshComponent#clone()
	 */
	@Override
	public Object clone() {

		// Make a new shape component and copy the data into it
		TubeMesh clone = new TubeMesh();
		clone.copy(this);

		return clone;
	}

}
