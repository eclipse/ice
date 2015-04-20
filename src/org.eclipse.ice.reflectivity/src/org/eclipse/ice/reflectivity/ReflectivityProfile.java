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
 * This class stores the wave vector and reflectivity for a reflectivity
 * calculation.
 * 
 * It extends ScatteringDensityProfile so that all the data from the calculation
 * can be stored in one place.
 * 
 * While the design of these two classes is sufficiently simple to perform well,
 * they leave much to be desired in terms of maintainability and reuse. FIXME!
 * 
 * @author Jay Jay Billings
 *
 */
public class ReflectivityProfile extends ScatteringDensityProfile {

	// The array of points that define the wave vector values at which the
	// reflectivity is calculated.
	public double[] waveVector;

	// The reflectivity at each point in the waveVector array.
	public double[] reflectivity;

}
