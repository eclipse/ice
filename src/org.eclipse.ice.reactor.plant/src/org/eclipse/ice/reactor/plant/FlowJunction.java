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


/**
 * <p>
 * Joint for flow.
 * </p>
 * 
 * @author Anna Wojtowicz
 */
public class FlowJunction extends Junction {
	/**
	 * <p>
	 * Nullary constructor (calls superconstructor).
	 * </p>
	 * 
	 */
	public FlowJunction() {
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
	@Override
	public boolean equals(Object otherObject) {
		// Ensure the incoming object is not null
		if (otherObject == null) {
			return false;
		}
		// If same object in memory, then equal
		if (this == otherObject) {
			return true;
		}
		// Make sure this is an actual FlowJunction
		if (!(otherObject instanceof FlowJunction)) {
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
	@Override
	public Object clone() {
		FlowJunction temp = new FlowJunction();
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
	public void copy(FlowJunction otherObject) {
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
	@Override
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
	@Override
	public void accept(IPlantComponentVisitor visitor) {
		if (visitor != null) {
			visitor.visit(this);
		}

		return;
	}
}