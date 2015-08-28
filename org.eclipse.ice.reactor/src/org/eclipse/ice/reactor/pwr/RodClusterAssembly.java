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
package org.eclipse.ice.reactor.pwr;

import org.eclipse.ice.reactor.HDF5LWRTagType;
import org.eclipse.ice.reactor.ILWRComponentVisitor;

/**
 * <p>
 * The RodClusterAssembly class is a PWRAssembly associated with a particular
 * FuelAssembly object.
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public class RodClusterAssembly extends PWRAssembly {

	public RodClusterAssembly(int size) {
		// Call super constructor
		super(size);

		// Set default names and sizes
		this.name = "RodClusterAssembly";
		this.description = "RodClusterAssembly's Description";
		this.id = 1;

		// Setup the LWRComponent to the correct type
		this.HDF5LWRTag = HDF5LWRTagType.ROD_CLUSTER_ASSEMBLY;

	}

	/**
	 * <p>
	 * A parameterized Constructor.
	 * </p>
	 * 
	 * @param name
	 *            <p>
	 *            The name of this RodClusterAssembly.
	 *            </p>
	 * @param size
	 *            <p>
	 *            The size of either dimension of this RodClusterAssembly.
	 *            </p>
	 */
	public RodClusterAssembly(String name, int size) {
		// Call this constructor
		this(size);

		// Check name
		this.setName(name);

	}

	/*
	 * Overrides a method from PWRAssembly.
	 */
	@Override
	public boolean equals(Object otherObject) {

		// Call super, since no attributes here

		return super.equals(otherObject);

	}

	/*
	 * Overrides a method from PWRAssembly.
	 */
	@Override
	public int hashCode() {

		// Call super, since no attributes here
		return super.hashCode();

	}

	/**
	 * <p>
	 * Deep copies the contents of the object.
	 * </p>
	 * 
	 * @param otherObject
	 *            <p>
	 *            The object to be copied.
	 *            </p>
	 */
	public void copy(RodClusterAssembly otherObject) {

		// Call super, since no attributes here
		super.copy(otherObject);

	}

	/*
	 * Overrides a method from PWRAssembly.
	 */
	@Override
	public Object clone() {

		// Local Declarations
		RodClusterAssembly assembly = new RodClusterAssembly(0);

		// copy contents
		assembly.copy(this);

		// Return newly instantiated object
		return assembly;

	}

	/*
	 * Overrides a method from LWRComponent.
	 */
	@Override
	public void accept(ILWRComponentVisitor visitor) {
		visitor.visit(this);
	}
}