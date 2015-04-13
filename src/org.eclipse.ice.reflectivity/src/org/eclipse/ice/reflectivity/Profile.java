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
 * This class stores the depth and neutron scattering density for a reflectivity
 * calculation.
 * 
 * @author Jay Jay Billings
 *
 */
public class Profile {

	// The array of points that define the depth of the material and the where
	// the neutron scattering density is calculated.
	public double[] depth;

	// The scattering density at each point in the depth array.
	public double[] scatteringDensity;

}
