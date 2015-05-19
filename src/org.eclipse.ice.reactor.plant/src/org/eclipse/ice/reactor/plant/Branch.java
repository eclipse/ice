/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.reactor.plant;

import java.io.InputStream;

/**
 * <p>
 * Flow branch.
 * </p>
 * 
 * @author Anna Wojtowicz
 */
public class Branch extends Junction {
	/**
	 * <p>
	 * Nullary constructor (calls superconstructor).
	 * </p>
	 * 
	 */
	public Branch() {
		super();
	}

	/**
	 * <p>
	 * Performs an equality check between two Objects.
	 * </p>
	 * 
	 * @param otherObject
	 *            <p>
	 *            The other Object to compare against.
	 *            </p>
	 * @return <p>
	 *         Returns true if the two objects are equal, otherwise false.
	 *         </p>
	 */
	public boolean equals(Object otherObject) {
		// Ensure the incoming object is not null
		if (otherObject == null) {
			return false;
		}
		// If same object in memory, then equal
		if (this == otherObject) {
			return true;
		}
		// Make sure this is an actual Branch
		if (!(otherObject instanceof Branch)) {
			return false;
		}
		// Verify Junction and ICEObject data are equal
		// and the inputs and outputs are the same
		return super.equals(otherObject);
	}

	/**
	 * <p>
	 * Performs a deep copy and returns a newly instantiated Object.
	 * </p>
	 * 
	 * @return <p>
	 *         The newly instantiated Object.
	 *         </p>
	 */
	public Object clone() {
		Branch temp = new Branch();
		temp.copy(this);
		return temp;
	}

	/**
	 * <p>
	 * Deep copies the contents of otherObject.
	 * </p>
	 * 
	 * @param otherObject
	 *            <p>
	 *            The other object to copy the contents from.
	 *            </p>
	 */
	public void copy(Branch otherObject) {
		// Make sure other is not null
		if (otherObject == null) {
			return;
		}
		// Copy the Junction and ICEObject data
		super.copy(otherObject);

		return;
	}

	/**
	 * <p>
	 * Returns the hashCode of the object.
	 * </p>
	 * 
	 * @return <p>
	 *         The hashCode of the Object.
	 *         </p>
	 */
	public int hashCode() {
		return super.hashCode();
	}

	/**
	 * <p>
	 * Accepts PlantComponentVisitors to reveal the type of a PlantComponent.
	 * </p>
	 * 
	 * @param visitor
	 *            <p>
	 *            The PlantComponent's visitor.
	 *            </p>
	 */
	public void accept(IPlantComponentVisitor visitor) {
		if (visitor != null) {
			visitor.visit(this);
		}
		return;
	}
}