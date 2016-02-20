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
		properties.put("Type", "Tube");
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
		return Integer.parseInt(properties.get("Num Elements"));
	}

	/**
	 * Convenience getter method for the tube's length
	 * 
	 * @return The tube's length
	 */
	public double getLength() {
		return Double.parseDouble(properties.get("Length"));
	}

	/**
	 * Convenience getter method for the tube's radius
	 * 
	 * @return The tube's radius
	 */
	public double getRadius() {
		return Double.parseDouble(properties.get("Radius"));
	}

	/**
	 * Convenience getter method for the radius of the tube's hole.
	 * 
	 * @return The hole's radius
	 */
	public double getInnerRadius() {
		return Double.parseDouble(properties.get("Inner Radius"));
	}

	/**
	 * Convenience setter method for the tube's axial samples
	 * 
	 * @param axialSamples
	 *            The tube's new axial samples
	 */
	public void setAxialSamples(int axialSamples) {
		setProperty("Num Elements", Integer.toString(axialSamples));
	}

	/**
	 * Convenience setter method for the tube's length
	 * 
	 * @param length
	 *            The tube's new length
	 */
	public void setLength(double length) {
		setProperty("Length", Double.toString(length));
	}

	/**
	 * Convenience setter method for the tube's radius
	 * 
	 * @param radius
	 *            The tube's new radius
	 */
	public void setRadius(double radius) {
		setProperty("Radius", Double.toString(radius));
	}

	/**
	 * Convenience setter method for the radius of the tube's hole.
	 * 
	 * @param radius
	 *            The hole's new radius
	 */
	public void setInnerRadius(double radius) {
		setProperty("Inner Radius", Double.toString(radius));
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
