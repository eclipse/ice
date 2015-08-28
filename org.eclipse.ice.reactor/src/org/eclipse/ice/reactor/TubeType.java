/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.reactor;

/**
 * <p>
 * The TubeType enumeration describes the types of Tubes that can be created.
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public enum TubeType {
	/**
	 * <p>
	 * This literal indicates a guide tube.
	 * </p>
	 * 
	 */
	GUIDE("Guide"),
	/**
	 * <p>
	 * This literal indicates an instrument tube.
	 * </p>
	 * 
	 */
	INSTRUMENT("Instrument");

	/**
	 * <p>
	 * Human readable string associated with enumerated value.
	 * </p>
	 * 
	 */
	private String name;

	/**
	 * <p>
	 * The constructor.
	 * </p>
	 * 
	 * @param name
	 *            <p>
	 *            The name of the enumeration value.
	 *            </p>
	 */
	TubeType(String name) {

		this.name = name;

	}

	/**
	 * <p>
	 * Returns the type of enumeration keyed on name. Returns null if invalid
	 * name.
	 * </p>
	 * 
	 * @param name
	 *            <p>
	 *            The name associated with the enumerated value.
	 *            </p>
	 * @return <p>
	 *         The type of enumeration.
	 *         </p>
	 */
	public TubeType toType(String name) {

		// Cycle over all types
		for (TubeType p : values()) {

			// If this property's name equals name
			if (p.name.equals(name)) {

				// Return the property
				return p;
			}
		}

		// If not found return null
		return null;

	}

	/**
	 * <p>
	 * Returns the string name of the enumerated value.
	 * </p>
	 * 
	 * @return <p>
	 *         The name of the enumerated value.
	 *         </p>
	 */
	@Override
	public String toString() {

		return this.name;

	}
}