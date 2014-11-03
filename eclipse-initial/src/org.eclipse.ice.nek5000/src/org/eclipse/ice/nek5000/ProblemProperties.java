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
package org.eclipse.ice.nek5000;

/**
 * This class is responsible for handling values defined in a Nek problem
 * (number of dimensions, number of thermal elements, number of fluid elements,
 * number of passive scalars, and Nek version) in a manner that makes it easy to 
 * pass around. The NekReader is responsible for constructing this object which 
 * the NekWriter later uses as input.
 * 
 * @author w5q
 *
 */
public class ProblemProperties {

	/**
	 * Number of dimensions the problem is based in, defined by the NDIM
	 * parameter in a .rea file, located on the line before the MESH COMPONENT
	 * header.
	 */
	private int numDimensions;
	
	/**
	 * Number of thermal elements/quads contained in the mesh.
	 */
	private int numThermalElements;
	
	/**
	 * Number of elements/quads which specifically house coolant/fluids. The 
	 * number of fluid elements can be less than or equal to the number of 
	 * thermal elements, but not greater.
	 */
	private int numFluidElements;
	
	/**
	 * Number of passive scalar boundary condition sets, as defined by the NPSCAL
	 * value in the PARAMETERS section of a .rea file. Can be equal to or greater
	 * than 0.
	 */
	private int numPassiveScalars;
	
	private String nekVersion = "2.610000"; // FIXME figure out how to retrieve this later
	
	/*
	 * Parameterized constructor. No parameters passed in can be negative, and
	 * number of dimensions also cannot be zero.
	 */
	public ProblemProperties(int numDimensions, int numThermalElements,
			int numFluidElements, int numPassiveScalars) {
		if (numDimensions > 0 &&
				numThermalElements >= 0 &&
				numFluidElements >= 0 &&
				numPassiveScalars >= 0) {
			
			this.numDimensions = numDimensions;
			this.numThermalElements = numThermalElements;
			this.numFluidElements = numFluidElements;
			this.numPassiveScalars = numPassiveScalars;
		}
		
		return;
	}
	
	/*
	 * Returns the number of dimensions of the problem.
	 */
	public int getNumDimensions() {
		return numDimensions;
	}
	
	/*
	 * Returns the number of thermal elements of the problem.
	 */
	public int getNumThermalElements() {
		return numThermalElements;
	}
	
	/*
	 * Returns the number of fluid elements of the problem.
	 */
	public int getNumFluidElements() {
		return numFluidElements;
	}
	
	/*
	 * Returns the number of passive scalar sets of the problem.
	 */
	public int getNumPassiveScalars() {
		return numPassiveScalars;
	}
	
	/*
	 * Returns the Nek version.
	 */
	public String getNekVersion() {
		return nekVersion;
	}
	
}
