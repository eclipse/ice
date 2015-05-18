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
 * A simple wet well model of non-LOCA BWR simulations.
 * </p>
 * 
 * @author w5q
 */
public class WetWell extends Junction {
	/**
	 * <p>
	 * The total well height.
	 * </p>
	 * 
	 */
	private double height;

	/**
	 * <p>
	 * The vertical location (z-coordinate) of the steam injection line end
	 * relative to the well bottom.
	 * </p>
	 * 
	 */
	private double zIn;

	/**
	 * <p>
	 * The vertical location (z-coordinate) of the water line end relative to
	 * the well bottom.
	 * </p>
	 * 
	 */
	private double zOut;

	/**
	 * <p>
	 * Nullary constructor.
	 * </p>
	 * 
	 */
	public WetWell() {
		super();
		height = 0.0;
		zIn = 0.0;
		zOut = 0.0;
	}

	/**
	 * <p>
	 * Parameterized constructor.
	 * </p>
	 * 
	 * @param height
	 *            <p>
	 *            Total height of the well.
	 *            </p>
	 */
	public WetWell(Double height) {
		super();
		this.height = height;
		zIn = 0.0;
		zOut = 0.0;
	}

	/**
	 * @return the height
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public void setHeight(double height) {
		this.height = height;
	}

	/**
	 * @return the zIn
	 */
	public double getzIn() {
		return zIn;
	}

	/**
	 * @param zIn
	 *            the zIn to set
	 */
	public void setzIn(double zIn) {
		this.zIn = zIn;
	}

	/**
	 * @return the zOut
	 */
	public double getzOut() {
		return zOut;
	}

	/**
	 * @param zOut
	 *            the zOut to set
	 */
	public void setzOut(double zOut) {
		this.zOut = zOut;
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
		if (!(otherObject instanceof WetWell)) {
			return false;
		}
		// Cast the Object as a Junction
		WetWell other = (WetWell) otherObject;

		// Verify PlantComponent and ICEObject data are equal
		// and the inputs and outputs are the same
		return super.equals(other) && height == other.height
				&& zIn == other.zIn && zOut == other.zOut;
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
		WetWell temp = new WetWell();
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
	public void copy(WetWell otherObject) {

		// Make sure the other is not null
		if (otherObject == null) {
			return;
		}
		// Copy Junction and ICEObject data
		super.copy(otherObject);

		height = otherObject.height;
		zIn = otherObject.zIn;
		zOut = otherObject.zOut;

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
		hash = 31 * hash + (int) height;
		hash = 31 * hash + (int) zIn;
		hash = 31 * hash + (int) zOut;
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