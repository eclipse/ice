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
 * A tube part in a constructive solid geometry tree.
 * 
 * @author Robert Smith
 *
 */
public class TubeController extends ShapeController {

	/**
	 * The nullary constructor
	 */
	public TubeController() {
		super();
	}

	/**
	 * The default constructor.
	 * 
	 * @param model
	 *            The controller's model
	 * @param view
	 *            The controller's view
	 */
	public TubeController(TubeMesh model, AbstractView view) {
		super(model, view);
	}

	/**
	 * Getter method for the tube's axial samples
	 * 
	 * @return The tube's length
	 */
	public int getAxialSamples() {
		return ((TubeMesh) model).getAxialSamples();
	}

	/**
	 * Getter method for the tube's length
	 * 
	 * @return The tube's length
	 */
	public double getLength() {
		return ((TubeMesh) model).getLength();
	}

	/**
	 * Getter method for the tube's radius
	 * 
	 * @return The tube's radius
	 */
	public double getRadius() {
		return ((TubeMesh) model).getRadius();
	}

	/**
	 * Getter method for the radius of the tube's hole
	 * 
	 * @return The tube's radius
	 */
	public double getInnerRadius() {
		return ((TubeMesh) model).getInnerRadius();
	}

	/**
	 * Setter method for the tube's axial samples
	 * 
	 * @param axialSamples
	 *            The tube's new axial samples
	 */
	public void setAxialSamples(int axialSamples) {
		((TubeMesh) model).setAxialSamples(axialSamples);
	}

	/**
	 * Setter method for the tube's length
	 * 
	 * @param length
	 *            The tube's new length
	 */
	public void setLength(double length) {
		((TubeMesh) model).setLength(length);
	}

	/**
	 * Setter method for the tube's radius
	 * 
	 * @param length
	 *            The tube's new radius
	 */
	public void setRadius(double radius) {
		((TubeMesh) model).setRadius(radius);
	}

	/**
	 * Setter method for the radius of the tube's hole
	 * 
	 * @param length
	 *            The tube's new radius
	 */
	public void setInnerRadius(double radius) {
		((TubeMesh) model).setInnerRadius(radius);
	}

}
