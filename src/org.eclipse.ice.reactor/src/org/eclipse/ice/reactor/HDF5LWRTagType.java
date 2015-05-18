/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
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
 * Represents the LWRComponentTypes (stack, material, etc) for the object.
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public enum HDF5LWRTagType {
	/**
	 * <p>
	 * Represents the ControlBank.
	 * </p>
	 * 
	 */
	CONTROL_BANK("Control Bank"),
	/**
	 * <p>
	 * Represents the FuelAssembly.
	 * </p>
	 * 
	 */
	FUEL_ASSEMBLY("Fuel Assembly"),
	/**
	 * <p>
	 * Represents the GridLabelProvider.
	 * </p>
	 * 
	 */
	GRID_LABEL_PROVIDER("Grid Label Provider"),
	/**
	 * <p>
	 * Represents the IncoreInstrument.
	 * </p>
	 * 
	 */
	INCORE_INSTRUMENT("Incore Instrument"),
	/**
	 * <p>
	 * Represents the LWRRod.
	 * </p>
	 * 
	 */
	LWRROD("LWRRod"),
	/**
	 * <p>
	 * Represents the Material.
	 * </p>
	 * 
	 */
	MATERIAL("Material"),
	/**
	 * <p>
	 * Represents the PWReactor.
	 * </p>
	 * 
	 */
	PWREACTOR("PWReactor"),
	/**
	 * <p>
	 * Represents the Ring.
	 * </p>
	 * 
	 */
	RING("Ring"),
	/**
	 * <p>
	 * Represents the Rod Cluster Assembly.
	 * </p>
	 * 
	 */
	ROD_CLUSTER_ASSEMBLY("Rod Cluster Assembly"),
	/**
	 * <p>
	 * Represents the Tube.
	 * </p>
	 * 
	 */
	TUBE("Tube"),
	/**
	 * <p>
	 * Represents a generic LWRComponent.
	 * </p>
	 * 
	 */
	LWRCOMPONENT("LWRComponent"),
	/**
	 * <p>
	 * Represents a generic LWRComposite.
	 * </p>
	 * 
	 */
	LWRCOMPOSITE("LWRComposite"),
	/**
	 * <p>
	 * Represents a LWReactor.
	 * </p>
	 * 
	 */
	LWREACTOR("LWReactor"),
	/**
	 * <p>
	 * Represents a BWReactor.
	 * </p>
	 * 
	 */
	BWREACTOR("BWReactor"),
	/**
	 * <p>
	 * Represents a PWRAssembly.
	 * </p>
	 * 
	 */
	PWRASSEMBLY("PWRAssembly"),
	/**
	 * <p>
	 * Represents a LWRGridManager.
	 * </p>
	 * 
	 */
	LWRGRIDMANAGER("LWRGridManager"),

	/**
	 * <p>
	 * Represents a MaterialBlock.
	 * </p>
	 * 
	 */
	MATERIALBLOCK("MaterialBlock");

	/**
	 * 
	 */
	private String name;

	/**
	 * <p>
	 * The constructor
	 * </p>
	 * 
	 * @param name
	 *            <p>
	 *            Human readable string associated with enumerated value.
	 *            </p>
	 */
	HDF5LWRTagType(String name) {

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
	public static HDF5LWRTagType toType(String name) {
		// Cycle over all types
		for (HDF5LWRTagType p : values()) {

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
	public String toString() {

		return name;
	}
}