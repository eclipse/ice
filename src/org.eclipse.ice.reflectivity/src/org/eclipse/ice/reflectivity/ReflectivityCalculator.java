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
	 * The maximum number of points used by the convolution routine.
	 */
	public static final int maxPoints = 2000;

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

	/**
	 * This operation convolutes the data in refFit with a Gaussian resolution
	 * function in q, calculated from theta, delThe, and delLamOLam.
	 * 
	 * @param q
	 *            the wave vector (Q) plus additional space for the convolution.
	 *            This array should have length = numPoints + numLowPoints.
	 * @param delQ0
	 *            the zeroth order term of a Taylor expansion of the
	 *            reflectometer resolution function dQ = dQ_0 + (dQ/Q)_1 x Q +
	 *            ...
	 * @param delQ1oQ
	 *            the zeroth order term of the Q resolution Taylor expansion
	 * @param wavelength
	 *            the wavelength of the incident neutrons
	 * @param numPoints
	 *            the number of points in the wave vector
	 * @param numLowPoints
	 *            the number of points in the low-Q extension to q used for
	 *            convolution of the data with the resolution function. Returned
	 *            by ExtResFixedLambda.
	 * @param numHighPoints
	 *            the number of points in the high-Q extension to q used for
	 *            convolution of the data with the resolution function. Returned
	 *            by ExtResFixedLambda.
	 * @param refFit
	 *            OUTPUT - the specular reflectivity values for each Q in q
	 *            convoluted with instrumental resolution.
	 */
	public void convolute(double[] waveVector, double delQ0, double delQ1oQ,
			double wavelength, int numPoints, int numLowPoints,
			int numHighPoints, double[] refFit) {

		double ln2 = Math.log(2.0);
		double qEff = 0.0, qRes = 0.0, rExp = 0.0, rNorm = 0.0;
		double[] refTemp = new double[maxPoints];
		int nStep = 0;
		boolean lFinish = false, hFinish = false;

		// Perform convolution over nPnts between nLow and nHigh extensions
		for (int i = numLowPoints; i < numLowPoints + numPoints; i++) {
			// Calculate resolution width and initialize resolution loop
			if (waveVector[i] < 1.0e-10) {
				qEff = 1.0e-10;
			} else {
				qEff = waveVector[i];
			}
			double qDel = delQ0 + qEff * delQ1oQ;
			double twSgSq = 2.0 * qDel * qDel / (8.0 * ln2);
			if (twSgSq < 1.0e-10) {
				twSgSq = 1.0e-10;
			}
			rNorm = 1.0;
			refTemp[i - numLowPoints] = refFit[i];
			nStep = 1;
			// Check if exponent term becomes < 0.001 and loop until it does so
			lFinish = false;
			hFinish = false;
			while (!lFinish && !hFinish) {
				// Evaluate the low-q side
				if (lFinish) {
					qRes = 1.0e20;
				} else {
					qRes = waveVector[i - nStep] - waveVector[i];
				}
				if (qRes * qRes / twSgSq < 6.908) {
					// Continue evaluating convolution
					rExp = Math.exp(-qRes * qRes / twSgSq);
					rNorm = rNorm + rExp;
					refTemp[i - numLowPoints] = refTemp[i - numLowPoints]
							+ rExp * refFit[i - nStep];
				} else {
					lFinish = true;
				}
				// Evaluate high-q side
				if (hFinish) {
					qRes = 1.0e20;
				} else {
					qRes = waveVector[i + nStep] - waveVector[i];
				}
				if (qRes * qRes / twSgSq < 6.908) {
					// Continue evaluating convolution
					rExp = Math.exp(-qRes * qRes / twSgSq);
					rNorm = rNorm + rExp;
					refTemp[i - numLowPoints] = refTemp[i - numLowPoints]
							+ rExp * refFit[i + nStep];
				} else {
					hFinish = true;
				}
				nStep++;
			}
			// Normalize convoluted value to integrated intensity of resolution
			// function
			refTemp[i - numLowPoints] = refTemp[i - numLowPoints] / rNorm;
		}
		// Transfer convoluted values from refTemp to refFit
		for (int i = 0; i < 10; i++) {
			refFit[i] = refTemp[i];
		}

		return;
	}

	/**
	 * This operation calculates the length of the low-Q extension of the data
	 * to be convoluted with the delt-Q full-width half-maximum Gaussian
	 * resolution function.
	 * 
	 * @param q
	 *            the wave vector (Q) plus additional space for the convolution.
	 *            This array should have length = numPoints + numLowPoints.
	 * @param delQ0
	 *            the zeroth order term of a Taylor expansion of the
	 *            reflectometer resolution function dQ = dQ_0 + (dQ/Q)_1 x Q +
	 *            ...
	 * @param delQ1oQ
	 *            the zeroth order term of the Q resolution Taylor expansion
	 * @param numPoints
	 *            the number of points in the wave vector
	 * @return numLowPoints the number of points in the low-Q extension to q
	 *         used for convolution of the data with the resolution function.
	 *         Returned by ExtResFixedLambda.
	 */
	public int getLowExtensionLength(double[] waveVector, double delQ0,
			double delQ1oQ, int numPoints) {

		double ln2 = Math.log(2.0);

		// Determine the loq-Q extension
		double qDel = delQ0 + waveVector[0] * delQ1oQ;
		double qStep = waveVector[1] - waveVector[0];
		double twSgSq = Math.max(2.0 * qDel * qDel / (8.0 * ln2), 1.0e-10);
		int numLowPoints = 0;
		double qR = 0.0;
		while (qR * qR / twSgSq <= 6.908) {
			numLowPoints++;
			qR = qR + qStep;
		}

		return numLowPoints;
	}

	/**
	 * This operation calculates the length of the high-Q extension of the data
	 * to be convoluted with the delt-Q full-width half-maximum Gaussian
	 * resolution function.
	 * 
	 * @param q
	 *            the wave vector (Q) plus additional space for the convolution.
	 *            This array should have length = numPoints + numLowPoints.
	 * @param delQ0
	 *            the zeroth order term of a Taylor expansion of the
	 *            reflectometer resolution function dQ = dQ_0 + (dQ/Q)_1 x Q +
	 *            ...
	 * @param delQ1oQ
	 *            the zeroth order term of the Q resolution Taylor expansion
	 * @param numPoints
	 *            the number of points in the wave vector
	 * @return numHighPoints the number of points in the high-Q extension to q
	 *         used for convolution of the data with the resolution function.
	 *         Returned by ExtResFixedLambda.
	 */
	public int getHighExtensionLength(double[] waveVector, double delQ0,
			double delQ1oQ, int numPoints) {

		double ln2 = Math.log(2.0);

		// Determine the high-Q extension
		double qDel = delQ0 + waveVector[numPoints - 1] * delQ1oQ;
		double qStep = waveVector[numPoints - 1] - waveVector[numPoints - 2];
		double twSgSq = 2.0 * qDel * qDel / (8.0 * ln2);
		int numHighPoints = 0;
		double qR = 0.0;
		while (qR * qR / twSgSq <= 6.908) {
			numHighPoints++;
			qR = qR + qStep;
		}

		return numHighPoints;
	}
}
