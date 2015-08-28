/*******************************************************************************
 * Copyright (c) 2012, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Scot Forest Hull II - Initial API and implementation and/or initial documentation
 *   Jordan Deyton - bug 474744
 *******************************************************************************/
package org.eclipse.ice.reactor;

/**
 * The TubeType enumeration describes the types of Tubes that can be created.
 * 
 * @author Scott Forest Hull II
 */
public enum TubeType {
	/**
	 * This literal indicates a guide tube.
	 */
	GUIDE("Guide"),

	/**
	 * This literal indicates an instrument tube.
	 */
	INSTRUMENT("Instrument");

	/**
	 * Human readable string associated with enumerated value.
	 */
	private String name;

	/**
	 * The constructor.
	 * 
	 * @param name
	 *            The name of the enumeration value.
	 */
	private TubeType(String name) {
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
	public static TubeType toType(String name) {

		TubeType type = null;

		// Cycle over all types to find the correct one.
		for (TubeType p : values()) {
			if (p.name.equals(name)) {
				type = p;
				break;
			}
		}

		// Return the found type, or null if not found.
		return type;
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