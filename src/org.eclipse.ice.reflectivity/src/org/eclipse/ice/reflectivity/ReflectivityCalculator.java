/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.reflectivity;

/**
 * This class performs all of the operations necessary to calculate the
 * reflectivity of a stack of materials. It follows the code originally
 * developed by John Ankner at Oak Ridge National Laboratory.
 * 
 * @author Jay Jay Billings, John Ankner
 *
 */
public class ReflectivityCalculator {

	/**
	 * The constructor
	 */
	public ReflectivityCalculator() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * This operation returns the value of the squared modulus of the specular
	 * reflectivity for a single wave vector Q.
	 * 
	 * @param waveVectorQ
	 *            the value of the wave vector
	 * @param wavelength
	 *            the wavelength of the incident neutrons
	 * @param tiles
	 *            the list of TIles that contains the physical parameters needed
	 *            for the calculation, including the scattering densities,
	 *            absorption parameters and thicknesses.
	 * @return the squared modulus of the specular reflectivity
	 */
	public double getSpecRefSqrdMod(double waveVectorQ, double wavelength,
			Tile[] tiles) {
		return 0.0;
	}

}
