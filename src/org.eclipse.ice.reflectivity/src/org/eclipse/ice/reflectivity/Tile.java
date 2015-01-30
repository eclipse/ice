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
 * This class represents a tile or layer of material of a particular type in the
 * reflectivity calculator.
 * 
 * It has no accessors and everything is public because it is made for a
 * calculation. It should not be used outside of its package.
 * 
 * @author Jay Jay Billings, John Ankner
 *
 */
public class Tile {

	/**
	 * The scattering length of the reflecting tile/layer.
	 */
	public double scatteringLength = 0.0;
	
	/**
	 * The true absorption length of the reflecting tile/layer.
	 */
	public double trueAbsLength = 0.0;
	
	/**
	 * The incoherent absorption length of the reflecting tile/layer.
	 */
	public double incAbsLength = 0.0;
	
	/**
	 * The thickness of this tile/layer.
	 */
	public double thickness = 0.0;

}
