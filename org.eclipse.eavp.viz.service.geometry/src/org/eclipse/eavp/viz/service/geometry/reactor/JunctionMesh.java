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
package org.eclipse.eavp.viz.service.geometry.reactor;

import org.eclipse.eavp.viz.service.modeling.ShapeMesh;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The internal representation of a Junction part.
 * 
 * @author Robert Smith
 *
 */
public class JunctionMesh extends ShapeMesh {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(JunctionMesh.class);

	/**
	 * The default constructor.
	 */
	public JunctionMesh() {
		super();
	}

	/**
	 * Convenience getter method for the junction's height
	 * 
	 * @return The tube's height
	 */
	public double getHeight() {
		return Double.parseDouble(properties.get("Height"));
	}

	/**
	 * Convenience getter method for the junction's intake's z coordinate
	 * 
	 * @return The tube's intake's z coordinate
	 */
	public double getZIn() {
		return Double.parseDouble(properties.get("Z Intake"));
	}

	/**
	 * Convenience getter method for the junction's output's z coordinate
	 * 
	 * @return The tube's output's z coordinate
	 */
	public double getZOut() {
		return Double.parseDouble(properties.get("Z Output"));
	}

	/**
	 * Convenience setter method for the tube's height
	 * 
	 * @param length
	 *            The tube's new height
	 */
	public void setHeight(double height) {
		setProperty("Height", Double.toString(height));
	}

	/**
	 * Convenience setter method for the tube's intake's z coordinate
	 * 
	 * @param length
	 *            The tube's new intake z coordinate
	 */
	public void setZIn(double ZIn) {
		setProperty("Z Intake", Double.toString(ZIn));
	}

	/**
	 * Convenience setter method for the tube's output's z coordinate
	 * 
	 * @param length
	 *            The tube's new output z coordinate
	 */
	public void setZOut(double ZOut) {
		setProperty("Z Output", Double.toString(ZOut));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {

		// Create a new component, and make it a copy of this one.
		JunctionMesh clone = new JunctionMesh();
		clone.copy(this);
		return clone;
	}
}
