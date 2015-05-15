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
import java.security.InvalidParameterException;

/**
 * <p>
 * Represents a lattice of rods for housing fuel rods of a reactor. For the
 * purpose of drawing, this is just a specialized pipe.
 * </p>
 * 
 * @author w5q
 */
public class Subchannel extends Pipe {
	/**
	 * <p>
	 * Number of fuel rods contained within.
	 * </p>
	 * 
	 */
	private int numRods;

	/**
	 * <p>
	 * Diameter of the subchannel fuel rods (this assumes uniform rod sizes).
	 * </p>
	 * 
	 */
	private double rodDiameter;

	/**
	 * <p>
	 * Pitch of the fuel rod bundle (distance between adjacent rod centers).
	 * </p>
	 * 
	 */
	private double pitch;

	/**
	 * <p>
	 * Nullary constructor.
	 * </p>
	 * 
	 */
	public Subchannel() {

		// Set the name, description and ID.
		setName("Subchannel 1");
		setDescription("A subchannel plant component for reactors");
		setId(1);

		// Set the default number of rods, rod diameter and pitch.
		setNumRods(1);
		setRodDiameter(1.0);
		setPitch(1.5);
		// Note: Pitch must always be set after diameter, as setPitch method
		// checks that pitch >= rodDiameter.

		return;
	}

	/**
	 * <p>
	 * Parameterized constructor.
	 * </p>
	 * 
	 * @param numRods
	 *            <p>
	 *            Number of rods contained.
	 *            </p>
	 * @param rodDiameter
	 *            <p>
	 *            Diameter of the (uniformly-sized) fuel rods.
	 *            </p>
	 * @param pitch
	 *            <p>
	 *            Pitch of the fuel rods.
	 *            </p>
	 */
	public Subchannel(int numRods, double rodDiameter, double pitch) {

		// Set the name, description and ID.
		setName("Subchannel 1");
		setDescription("A subchannel plant component for reactors");
		setId(1);

		// Set the default number of rods, rod diameter and pitch.
		setNumRods(numRods);
		setRodDiameter(rodDiameter);
		setPitch(pitch);

		return;
	}

	/**
	 * @return the numRods
	 */
	public int getNumRods() {
		return numRods;
	}

	/**
	 * @param numRods
	 *            the numRods to set
	 */
	public void setNumRods(int numRods) {

		// Check the input is valid.
		if (numRods >= 1) {
			this.numRods = numRods;
		} else {
			throw new InvalidParameterException("Subchannel error: The number "
					+ "of rods must greater than or equal to 1.");
		}
		return;
	}

	/**
	 * @return the rodDiameter
	 */
	public double getRodDiameter() {
		return rodDiameter;
	}

	/**
	 * @param rodDiameter
	 *            the rodDiameter to set
	 */
	public void setRodDiameter(double rodDiameter) {

		// Check the input is valid.
		if (rodDiameter > 0) {
			this.rodDiameter = rodDiameter;
		} else {
			throw new InvalidParameterException("Subchannel error: The rod "
					+ "diameter must be non-negative.");
		}
		return;
	}

	/**
	 * @return the pitch
	 */
	public double getPitch() {
		return pitch;
	}

	/**
	 * @param pitch
	 *            the pitch to set
	 */
	public void setPitch(double pitch) {

		// Check that the input is valid.
		if (pitch >= rodDiameter) {
			this.pitch = pitch;
		} else {
			throw new InvalidParameterException("Subchannel error: The rod "
					+ "pitch cannot be smaller than the rod diameter.");
		}
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

		// By default, the objects are not equivalent.
		boolean equals = false;

		// Check the reference.
		if (this == otherObject) {
			equals = true;
		}
		// Check the information stored in the other object.
		else if (otherObject != null && otherObject instanceof Subchannel) {

			// Cast the other object.
			Subchannel component = (Subchannel) otherObject;

			// Compare all the variables. (Save the biggest for last; Java
			// should
			// short-circuit the logical operators as soon as a mismatch is
			// found)
			equals = (super.equals(component) && numRods == component.numRods
					&& rodDiameter == component.rodDiameter && pitch == component.pitch);
		}

		return equals;
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

		// Initialize a new object.
		Subchannel object = new Subchannel();

		// Copy the contents from this one.
		object.copy(this);

		// Return the newly instantiated object.
		return object;
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
	public void copy(Subchannel otherObject) {

		// Check the otherObject is valid.
		if (otherObject == null) {
			return;
		}
		// Copy the other object.
		super.copy(otherObject);
		numRods = otherObject.numRods;
		rodDiameter = otherObject.rodDiameter;
		pitch = otherObject.pitch;

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

		// Call the super's hashCode.
		int hash = super.hashCode();

		// Add local hashes.
		hash = 31 * hash + numRods;
		hash = 31 * hash + new Double(rodDiameter).hashCode();
		hash = 31 * hash + new Double(pitch).hashCode();

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

		// Only accept valid visitors.
		if (visitor != null) {
			visitor.visit(this);
		}
		return;
	}
}