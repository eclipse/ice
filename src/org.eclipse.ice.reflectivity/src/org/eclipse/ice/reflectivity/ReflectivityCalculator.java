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

import org.apache.commons.math.complex.Complex;

/**
 * This class performs all of the operations necessary to calculate the
 * reflectivity of a stack of materials. It follows the code originally
 * developed by John Ankner at Oak Ridge National Laboratory that uses the
 * method described in Parratt, Phys. Rev. 95, 359(1954). It has been corrected
 * to incorporate incoherent and true absorption.
 * 
 * @author Jay Jay Billings, John Ankner
 *
 */
public class ReflectivityCalculator {

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
	public double getModSqrdSpecRef(double waveVectorQ, double wavelength,
			Tile[] tiles) {

		double modSqrdSpecRef = 0.0;

		if (wavelength > 0.0) {
			// Variables only needed if we are going to do the work, i.e. -
			// wavelength > 0.0.
			Tile tile;
			Complex aNm1Sq, fNm1N, rNm1N = new Complex(0.0, 0.0), one = new Complex(
					1.0, 0.0), qN = new Complex(0.0, 0.0), rNNp1 = new Complex(
					0.0, 0.0);
			// Get the bottom tile
			int nLayers = tiles.length;
			tile = tiles[nLayers - 1];
			// Starting point--no reflected beam in bottom-most (bulk) layer
			double qCSq = 16.0 * Math.PI * tile.scatteringLength;
			double muLAbs = tile.trueAbsLength;
			double mulInc = tile.incAbsLength;
			double thickness = tile.thickness;
			// Setup other values for the problem
			double betaNm1 = 4.0 * Math.PI * (muLAbs + mulInc / wavelength);
			Complex qNm1 = new Complex(waveVectorQ * waveVectorQ - qCSq, -2.0
					* betaNm1);
			qNm1 = qNm1.sqrt();
			// Loop through to calculate recursion formula described in Parratt.
			// Start at the bottom and work up.
			for (int i = nLayers - 1; i > 0; i--) {
				// Get the tile above tile[i] (started at the bottom
				tile = tiles[i - 1];
				// Calculate the normal component of Q for layer and layer-1
				qN = qNm1;
				qCSq = 16.0 * Math.PI * tile.scatteringLength;
				muLAbs = tile.trueAbsLength;
				mulInc = tile.incAbsLength;
				thickness = tile.thickness;
				betaNm1 = 4.0 * Math.PI * (muLAbs + mulInc / wavelength);
				qNm1 = new Complex(waveVectorQ * waveVectorQ - qCSq, -2.0
						* betaNm1);
				qNm1 = qNm1.sqrt();
				// Calculate phase factor, e^(-0.5*d*qNm1)
				aNm1Sq = (new Complex(qNm1.getImaginary(), qNm1.getReal())
						.multiply(-0.5 * thickness)).exp();
				// CDiv(qNm1-qN,qNm1+qN)
				fNm1N = qNm1.subtract(qN).divide(qNm1.add(qN));
				// Calculate the reflectivity amplitude.
				// CMult(aNm1Sq, CMult(aNm1Sq, CDiv(CAdd(rNNp1, fNm1N),
				// CAdd(CMult(rNNp1, fNm1N), CReal(1)))))
				Complex y = rNNp1.multiply(fNm1N).add(one);
				Complex z = rNNp1.add(fNm1N);
				rNm1N = aNm1Sq.multiply(aNm1Sq).multiply(z.divide((y)));
				// Carry over to the next iteration
				rNNp1 = rNm1N;
			}
			modSqrdSpecRef = rNm1N.getReal() * rNm1N.getReal()
					+ rNm1N.getImaginary() * rNm1N.getImaginary();
		}

		return modSqrdSpecRef;
	}
}
