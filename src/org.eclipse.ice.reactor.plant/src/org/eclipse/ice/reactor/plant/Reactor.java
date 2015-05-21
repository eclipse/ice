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

import java.util.ArrayList;

/**
 * <p>
 * Describes reactor parameters.
 * </p>
 * 
 * @author Anna Wojtowicz
 */
public class Reactor extends PlantComponent {
	/**
	 * <p>
	 * Core channel(s) contained in the reactor.
	 * </p>
	 * 
	 */
	private ArrayList<CoreChannel> coreChannels;

	/**
	 * Nullary constructor.
	 */
	public Reactor() {
		super();

		coreChannels = new ArrayList<CoreChannel>();

		return;
	}

	/**
	 * The core channel(s) contained in the reactor.
	 * 
	 * @param channels
	 */
	public Reactor(ArrayList<CoreChannel> channels) {
		super();

		if (channels != null) {
			coreChannels = channels;
		} else {
			coreChannels = new ArrayList<CoreChannel>();
		}
		return;
	}

	/**
	 * @return the coreChannels
	 */
	public ArrayList<CoreChannel> getCoreChannels() {
		return coreChannels;
	}

	/**
	 * @param coreChannels
	 *            the coreChannels to set
	 */
	public void setCoreChannels(ArrayList<CoreChannel> coreChannels) {
		this.coreChannels = coreChannels;

		// Notify listeners of the change.
		notifyListeners();
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
		// Make sure this is an actual Junction
		if (!(otherObject instanceof Reactor)) {
			return false;
		}
		// Cast the Object as a Junction
		Reactor other = (Reactor) otherObject;

		// Verify PlantComponent and ICEObject data are equal
		// and the inputs and outputs are the same
		return super.equals(other) && other.coreChannels.equals(coreChannels);

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
		Reactor temp = new Reactor();
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
	public void copy(Reactor otherObject) {
		// Make sure other is not null
		if (otherObject == null) {
			return;
		}
		// Copy the PlantComponent and ICEObject data
		super.copy(otherObject);

		// Copy the input and output data
		coreChannels = otherObject.coreChannels;

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
		int hash = super.hashCode();
		hash = 31 * hash + coreChannels.hashCode();
		return hash;
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