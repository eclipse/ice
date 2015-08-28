/*******************************************************************************
 * Copyright (c) 2012, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Scott Forest Hull II - Initial API and implementation and/or initial documentation
 *   Jordan Deyton - bug 474744
 *******************************************************************************/
package org.eclipse.ice.reactor;

/**
 * The MaterialType enumeration describes each type of material phase.
 * 
 * @author Scott Forest Hull II
 */
public enum MaterialType {
	/**
	 * This literal indicates a gas material phase.
	 */
	GAS("Gas"),

	/**
	 * This literal indicates a liquid material phase.
	 */
	LIQUID("Liquid"),

	/**
	 * This literal indicates a solid material phase.
	 */
	SOLID("Solid");

	/**
	 * Human readable string associated with enumerated value.
	 */
	private final String name;

	/**
	 * The private constructor.
	 * 
	 * @param name
	 *            The name associated with the enumerated value.
	 */
	private MaterialType(String name) {
		this.name = name;
	}

	/**
	 * Returns the type of enumeration keyed on name. Returns null if invalid
	 * name.
	 * 
	 * @param name
	 *            The name associated with the enumerated value.
	 * @return The type of enumeration.
	 */
	public static MaterialType toType(String name) {

		// Cycle over all types
		for (MaterialType p : values()) {

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
	 * Returns the string name of the enumerated value.
	 * 
	 * @return The name of the enumerated value.
	 */
	@Override
	public String toString() {
		return name;
	}
}