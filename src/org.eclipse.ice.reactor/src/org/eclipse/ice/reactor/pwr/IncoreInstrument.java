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

import java.util.ArrayList;

import org.eclipse.ice.io.hdf.IHdfReadable;
import org.eclipse.ice.io.hdf.IHdfWriteable;
import org.eclipse.ice.reactor.HDF5LWRTagType;
import org.eclipse.ice.reactor.ILWRComponentVisitor;
import org.eclipse.ice.reactor.LWRComponent;
import org.eclipse.ice.reactor.Ring;

/**
 * <p>
 * The IncoreInstrument class represents instruments (or detectors) that are
 * used for power distribution monitoring inside of a PWReactor. This class
 * contains a ring of data designed to be the "thimble" for material composition
 * on this class.
 * </p>
 * 
 * @author s4h
 */
public class IncoreInstrument extends LWRComponent {
	/**
	 * <p>
	 * An empty thimble tube used a boundary between the detector and the
	 * reactor.
	 * </p>
	 * 
	 */
	private Ring thimble;

	/**
	 * <p>
	 * The nullary Constructor.
	 * </p>
	 * 
	 */
	public IncoreInstrument() {
		// set default values
		this.setName("Instrument 1");
		this.setDescription("Default Instrument");
		this.setId(1);

		thimble = new Ring("thimble");

		this.HDF5LWRTag = HDF5LWRTagType.INCORE_INSTRUMENT;

	}

	/**
	 * <p>
	 * A parameterized Constructor.
	 * </p>
	 * 
	 * @param name
	 *            <p>
	 *            The name of this IncoreInstrument.
	 *            </p>
	 * @param thimble
	 *            <p>
	 *            An empty thimble tube used a boundary between the detector and
	 *            the reactor.
	 *            </p>
	 */
	public IncoreInstrument(String name, Ring thimble) {
		// call nullary constructor
		this();
		this.setName(name);
		this.setThimble(thimble);
	}

	/**
	 * <p>
	 * Sets an empty thimble tube used a boundary between the detector and the
	 * reactor.
	 * </p>
	 * 
	 * @param thimble
	 *            <p>
	 *            An empty thimble tube used a boundary between the detector and
	 *            the reactor.
	 *            </p>
	 */
	public void setThimble(Ring thimble) {
		// if thimble is not Null set value
		if (thimble != null) {
			this.thimble = thimble;
		}
	}

	/**
	 * <p>
	 * Returns an empty thimble tube used a boundary between the detector and
	 * the reactor.
	 * </p>
	 * 
	 * @return <p>
	 *         An empty thimble tube used a boundary between the detector and
	 *         the reactor.
	 *         </p>
	 */
	public Ring getThimble() {
		return this.thimble;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<IHdfWriteable> getWriteableChildren() {

		// Get the children in super
		ArrayList<IHdfWriteable> children = super.getWriteableChildren();

		// If super had no children
		if (children == null) {

			// Initialize to new array list
			children = new ArrayList<IHdfWriteable>();
		}

		// Add the thimble Ring to children
		children.add(this.thimble);

		return children;
	}

	/**
	 * <p>
	 * This operation returns an ArrayList of IHdfReadable child objects. If
	 * this IHdfReadable has no IHdfReadable child objects, then null is
	 * returned.
	 * </p>
	 * 
	 * @param iHdfReadable
	 * @return
	 */
	public boolean readChild(IHdfReadable iHdfReadable) {

		// If the child is null or not an instance of LWRComponent, then return
		// false.
		if (iHdfReadable == null || !(iHdfReadable instanceof LWRComponent)) {
			return false;
		}

		// Cast the child into a LWRComponent
		LWRComponent childComponent = (LWRComponent) iHdfReadable;

		// If this is a Ring
		if (childComponent.getHDF5LWRTag() == HDF5LWRTagType.RING) {

			// Assign to correct object
			this.thimble = (Ring) childComponent;

		}

		return true;
	}

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
		IncoreInstrument instrument;
		boolean retVal = false;

		// If the same ref on heap, return
		if (otherObject == this) {
			return true;
		}

		// Make sure the otherObject is not null and of the same type as this
		// object
		if (otherObject != null && otherObject instanceof IncoreInstrument) {

			// Cast it
			instrument = (IncoreInstrument) otherObject;

			// check values
			retVal = (super.equals(otherObject) && this.thimble
					.equals(instrument.thimble));

		}

		// Return retVal
		return retVal;

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

		// Local Declarations
		int hash = super.hashCode();

		// Local contents of the hash
		hash += 31 * this.thimble.hashCode();

		// Return the hash
		return hash;

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
	public void copy(IncoreInstrument otherObject) {

		// If null, return
		if (otherObject == null) {
			return;
		}

		// Copy contents - super
		super.copy(otherObject);

		// Copy local contents
		this.thimble = (Ring) otherObject.thimble.clone();

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
		IncoreInstrument instrument = new IncoreInstrument();

		// Copy contents
		instrument.copy(this);

		// Return newly instantiated object
		return instrument;

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