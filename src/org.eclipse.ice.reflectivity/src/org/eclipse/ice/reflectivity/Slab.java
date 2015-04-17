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
 * Slabs are subclasses of Tiles that represent collections of them and
 * therefore have an additional interfacial thickness parameter.
 * 
 * @author Jay Jay Billings, John Ankner
 *
 */
public class Slab extends Tile {

	/**
	 * The interfacial width of the slab.
	 */
	public double interfaceWidth = 0.0;
	
}
