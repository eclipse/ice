/*******************************************************************************
 * Copyright (c) 2013, 2015 UT-Battelle, LLC.
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

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the LWRComponentTypes (stack, material, etc) for the object.
 * 
 * @author Scott Forest Hull II
 */
public enum HDF5LWRTagType {
	/**
	 * Represents the ControlBank.
	 */
	CONTROL_BANK("Control Bank"),

	/**
	 * Represents the FuelAssembly.
	 */
	FUEL_ASSEMBLY("Fuel Assembly"),

	/**
	 * Represents the GridLabelProvider.
	 */
	GRID_LABEL_PROVIDER("Grid Label Provider"),

	/**
	 * Represents the IncoreInstrument.
	 */
	INCORE_INSTRUMENT("Incore Instrument"),

	/**
	 * Represents the LWRRod.
	 */
	LWRROD("LWRRod"),

	/**
	 * Represents the Material.
	 */
	MATERIAL("Material"),

	/**
	 * Represents the PWReactor.
	 */
	PWREACTOR("PWReactor"),

	/**
	 * Represents the Ring.
	 */
	RING("Ring"),

	/**
	 * Represents the Rod Cluster Assembly.
	 */
	ROD_CLUSTER_ASSEMBLY("Rod Cluster Assembly"),

	/**
	 * Represents the Tube.
	 */
	TUBE("Tube"),

	/**
	 * Represents a generic LWRComponent.
	 */
	LWRCOMPONENT("LWRComponent"),

	/**
	 * Represents a generic LWRComposite.
	 */
	LWRCOMPOSITE("LWRComposite"),

	/**
	 * Represents a LWReactor.
	 */
	LWREACTOR("LWReactor"),

	/**
	 * Represents a BWReactor.
	 */
	BWREACTOR("BWReactor"),

	/**
	 * Represents a PWRAssembly.
	 */
	PWRASSEMBLY("PWRAssembly"),

	/**
	 * Represents a LWRGridManager.
	 */
	LWRGRIDMANAGER("LWRGridManager"),

	/**
	 * Represents a MaterialBlock.
	 */
	MATERIALBLOCK("MaterialBlock");

	/**
	 * The name or tag for the type.
	 */
	private String name;

	/**
	 * A static map containing all types, keyed on their names or tags.
	 */
	private static Map<String, HDF5LWRTagType> TYPE_LOOKUP_MAP;

	/**
	 * The constructor
	 * 
	 * @param name
	 *            Human readable string associated with enumerated value.
	 */
	private HDF5LWRTagType(String name) {
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
	public static HDF5LWRTagType toType(String name) {
		// Create the type lookup map if necessary.
		if (TYPE_LOOKUP_MAP == null) {
			TYPE_LOOKUP_MAP = new HashMap<String, HDF5LWRTagType>();
			for (HDF5LWRTagType type : values()) {
				TYPE_LOOKUP_MAP.put(type.name, type);
			}
		}

		// Return the type based on the name. If the name is invalid and thus
		// not in the map, return null.
		return TYPE_LOOKUP_MAP.get(name);
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