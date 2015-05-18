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
package org.eclipse.ice.reactor.bwr;

import org.eclipse.ice.reactor.HDF5LWRTagType;
import org.eclipse.ice.reactor.ILWRComponentVisitor;
import org.eclipse.ice.reactor.LWReactor;

/**
 * <p>
 * The BWReactor class represents any Boiling Water Reactor.
 * </p>
 * 
 * @author s4h
 */
public class BWReactor extends LWReactor {

	/**
	 * <p>
	 * Overrides the equals operation to check the attributes on this object
	 * with another object of the same type. Returns true if the objects are
	 * equal. False otherwise.
	 * </p>
	 * 
	 * @param otherObject
	 *            <p>
	 *            The object to be compared.
	 *            </p>
	 * @return <p>
	 *         True if otherObject is equal. False otherwise.
	 *         </p>
	 */
	public boolean equals(Object otherObject) {

		// No attributes to compare. Call super
		return super.equals(otherObject);

	}

	/**
	 * <p>
	 * Returns the hashCode of the object.
	 * </p>
	 * 
	 * @return <p>
	 *         The hash of the object.
	 *         </p>
	 */
	public int hashCode() {

		// No attributes to compare. Call super
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
	public void copy(BWReactor otherObject) {

		// Call super
		super.copy(otherObject);

	}

	/**
	 * <p>
	 * Deep copies and returns a newly instantiated object.
	 * </p>
	 * 
	 * @return <p>
	 *         The newly instantiated copied object.
	 *         </p>
	 */
	public Object clone() {

		// Local Declarations
		BWReactor reactor = new BWReactor(0);

		// Copy Contents
		reactor.copy(this);

		// Return newly instantiated object
		return reactor;

	}

	/**
	 * <p>
	 * The constructor.
	 * </p>
	 * 
	 * @param size
	 *            <p>
	 *            The size of the reactor.
	 *            </p>
	 */
	public BWReactor(int size) {

		// Call super constructor
		super(size);

		// Setup default attributes
		this.name = "BWReactor 1";
		this.description = "BWReactor 1's Description";
		this.HDF5LWRTag = HDF5LWRTag.BWREACTOR;
	}

	/**
	 * <p>
	 * This operation accepts an ILWRComponentVisitor that can be visit the
	 * LWRComponent to ascertain its type and perform various type-specific
	 * operations.
	 * </p>
	 * 
	 * @param visitor
	 *            <p>
	 *            The visitor
	 *            </p>
	 */
	public void accept(ILWRComponentVisitor visitor) {
		visitor.visit(this);
	}
}