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
package org.eclipse.ice.viz.service.geometry.reactor;

import org.eclipse.ice.viz.service.modeling.TubeMesh;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The internal representation of a Pipe part.
 * 
 * @author Robert Smith
 *
 */
public class PipeMesh extends TubeMesh {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(PipeMesh.class);

	/**
	 * The default constructor.
	 */
	public PipeMesh() {
		super();
	}

	/**
	 * A constructor which initializes a pipe's length and radius.
	 * 
	 * @param length
	 *            The pipe's length
	 * @param radius
	 *            The pipe's radius
	 */
	public PipeMesh(double length, double radius) {
		super(length, radius);
	}

	/**
	 * Convenience getter method for the number of rods.
	 * 
	 * @return The number of rods in a SubChannel pipe
	 */
	public int getNumRods() {
		return Integer.parseInt(properties.get("NumRods"));
	}

	/**
	 * Convenience getter method for the pitch
	 * 
	 * @return The pipe's pitch
	 */
	public double getPitch() {
		return Double.parseDouble(properties.get("Pitch"));
	}

	/**
	 * Convenience getter method for the rod diameter
	 * 
	 * @return The pipe's rod diameter, under the assumption that all rods are
	 *         of uniform size.
	 */
	public double getRodDiameter() {
		return Double.parseDouble(properties.get("Rod Diameter"));
	}

	/**
	 * Convenience setter method for a SubChannel Pipe's number of rods. Does
	 * nothing for non-SubChannel pipes.
	 * 
	 * @param numRods
	 *            The number of rods in the SubChannel
	 */
	public void setNumRods(int numRods) {
		setProperty("NumRods", Integer.toString(numRods));
	}

	/**
	 * Convenience setter method for the pipe's pitch
	 * 
	 * @param pitch
	 */
	public void setPitch(double pitch) {
		setProperty("Pitch", Double.toString(pitch));
	}

	/**
	 * Convenience setter method for the pipe's rod diameter
	 * 
	 * @param rodDiameter
	 *            The pipe's rod diameter
	 */
	public void setRodDiameter(double rodDiameter) {
		setProperty("Rod Diameter", Double.toString(rodDiameter));
	}

	@Override
	public void setProperty(String property, String value) {

		// Validate input
		if ("Inner Radius".equals(property)) {
			logger.error(
					"Pipes are specified as always having an inner radius equal to their outer radius. Inner radius cannot be set.");
			return;
		}

		super.setProperty(property, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.modeling.TubeComponent#getInnerRadius()
	 */
	@Override
	public double getInnerRadius() {

		// Pipes are always drawn with infinite thinness, so their inner and
		// outer radii are identical
		return getRadius();
	}
}
