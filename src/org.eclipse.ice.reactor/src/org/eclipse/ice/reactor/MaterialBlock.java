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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;

import org.eclipse.ice.io.hdf.HdfReaderFactory;
import org.eclipse.ice.io.hdf.HdfWriterFactory;
import org.eclipse.ice.io.hdf.IHdfReadable;
import org.eclipse.ice.io.hdf.IHdfWriteable;

/**
 * <p>
 * The MaterialBlock class is a generalized class containing a set of concentric
 * and/or radial collection of Rings that constitute the inner core of an
 * LWRRod.
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public class MaterialBlock extends LWRComponent implements
		Comparable<MaterialBlock> {
	/**
	 * <p>
	 * A TreeMap of Rings.
	 * </p>
	 * 
	 */
	private TreeSet<Ring> rings;

	/**
	 * <p>
	 * The position from the bottom of the rod.
	 * </p>
	 * 
	 */
	private double pos;

	/**
	 * <p>
	 * The nullary Constructor.
	 * </p>
	 * 
	 */
	public MaterialBlock() {

		// Call super constructor
		super();
		// Create the TreeSet
		this.rings = new TreeSet<Ring>();

		// Setup default values
		this.name = "MaterialBlock 1";
		this.description = "MaterialBlock 1's Description";
		this.id = 1;
		this.pos = 0;

		// Setup the LWRComponentType to correct Type
		this.HDF5LWRTag = HDF5LWRTagType.MATERIALBLOCK;

	}

	/**
	 * <p>
	 * Adds a Ring to this MaterialBlock's ring collection. If the ring could
	 * not be successfully added, then false is returned. This could be due to a
	 * ring existing at within the inner and outer radius of an existing Ring
	 * object in the Slice.
	 * </p>
	 * 
	 * @param ring
	 *            <p>
	 *            The ring to add to this MaterialBlock's Ring collection.
	 *            </p>
	 * @return <p>
	 *         True, if the Ring was successfully added.
	 *         </p>
	 */
	public boolean addRing(Ring ring) {

		// Check for a null ring
		if (ring == null) {
			return false;
		}

		// Get the Ring iterator
		Iterator<Ring> itr = this.rings.iterator();

		// Loop over the iterator
		while (itr.hasNext()) {

			// Get the next Ring
			Ring currentRing = itr.next();

			// Check if this ring overlaps the current ring from the iterator
			if (currentRing.compareTo(ring) == 0) {

				// We found an overlapping ring. Do not add to the rings
				// collection.
				return false;

			}

		}

		// We could not locate an overlapping ring
		// therefore add the new ring
		rings.add(ring);

		return true;
	}

	/**
	 * <p>
	 * Returns the Ring located at the provided radius value or null if one
	 * could not be found.
	 * </p>
	 * 
	 * @param radius
	 *            <p>
	 *            A radius value.
	 *            </p>
	 * @return <p>
	 *         The Ring located at the provided radius value or null if one
	 *         could not be found.
	 *         </p>
	 */
	public Ring getRing(double radius) {

		// Get the Ring iterator from the rings TreeSet
		Iterator<Ring> itr = rings.iterator();

		// Cycle through the iterator
		while (itr.hasNext()) {

			// Get the next ring
			Ring ring = itr.next();

			// Check if the provided radius is equal to or
			// between this ring's inner and outer radius.
			// We are looking for the first Ring in rings
			// which qualifies these constraints.
			if (ring.getInnerRadius() <= radius
					&& ring.getOuterRadius() >= radius) {

				// Return the ring
				return ring;

			}

		}

		return null;
	}

	/**
	 * <p>
	 * Returns the Ring with the provided name or null if one could not be
	 * found.
	 * </p>
	 * 
	 * @param ringName
	 *            <p>
	 *            A Ring name.
	 *            </p>
	 * @return <p>
	 *         The Ring with the provided name or null if one could not be
	 *         found.
	 *         </p>
	 */
	public Ring getRing(String ringName) {

		// if the ringName is not null, return the name
		if (ringName != null) {

			// Get the Ring iterator
			Iterator<Ring> itr = this.rings.iterator();

			// Loop over the iterator
			while (itr.hasNext()) {

				// Get the next Ring
				Ring ring = itr.next();

				// If the Ring's name matches the provided name
				if (ring.getName().equals(ringName)) {

					// Return the Ring
					return ring;

				}
			}
		}

		// Name was null or not found in the rings TreeSet
		return null;
	}

	/**
	 * <p>
	 * Returns an ArrayList of Rings ordered by ascending radii.
	 * </p>
	 * 
	 * @return <p>
	 *         An ArrayList of Rings ordered by ascending radii.
	 *         </p>
	 */
	public ArrayList<Ring> getRings() {

		// Create the ArrayList
		ArrayList<Ring> list = new ArrayList<Ring>();

		// Add all of the elements from the TreeSet to the ArrayList
		list.addAll(rings);

		// Return the ArrayList
		return list;
	}

	/**
	 * <p>
	 * Removes the Ring from this MaterialBlock's ring collection that has the
	 * provided name. Returns true, if the Ring was successfully removed.
	 * </p>
	 * 
	 * @param ringName
	 *            <p>
	 *            The name of the Ring to remove.
	 *            </p>
	 * @return <p>
	 *         True, if the Ring was successfully removed from this
	 *         MaterialBlock's Ring collection.
	 *         </p>
	 */
	public boolean removeRing(String ringName) {

		// if the ringName is not null, return the name
		if (ringName != null) {

			// Get the Ring iterator
			Iterator<Ring> itr = this.rings.iterator();

			// Loop over the iterator
			while (itr.hasNext()) {

				// Get the next Ring
				Ring ring = itr.next();

				// If the Ring's name matches the provided name
				if (ring.getName().equals(ringName)) {

					// Remove the Ring
					this.rings.remove(ring);

					return true;

				}
			}
		}

		// Name was null or not found in the rings TreeSet
		return false;

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
	@Override
	public boolean equals(Object otherObject) {

		// Local Declarations
		MaterialBlock materialBlock;
		boolean retVal = false;

		// If they are equal to the same object, return true
		if (this == otherObject) {
			return true;
		}
		// If the otherObject is null or not an instance of the current object,
		// return false
		if (otherObject != null && otherObject instanceof MaterialBlock) {

			// Cast
			materialBlock = (MaterialBlock) otherObject;

			// Check values
			retVal = super.equals(otherObject)
					&& this.rings.equals(materialBlock.rings)
					&& this.pos == materialBlock.pos;

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
	@Override
	public int hashCode() {

		// Local Declarations
		int hash = super.hashCode();

		// Hash local values
		hash += 31 * this.rings.hashCode();
		hash += 31 * this.pos;

		// Return hash
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
	public void copy(MaterialBlock otherObject) {

		// Local Declarations
		Iterator<Ring> iter;

		// if otherObject is null, return
		if (otherObject == null) {
			return;
		}
		// copy contents - super
		super.copy(otherObject);

		this.pos = otherObject.pos;

		// Copy local contents
		// Deep Copy the Tree's contents
		this.rings.clear();
		iter = otherObject.rings.iterator();

		// Iterate over the list and clone each Ring
		while (iter.hasNext()) {
			Ring key = iter.next();
			this.rings.add((Ring) key.clone());

		}

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
	@Override
	public Object clone() {

		// Local Declarations
		MaterialBlock materialBlock = new MaterialBlock();

		// Copy contents
		materialBlock.copy(this);

		// Return newly instantiated object
		return materialBlock;

	}

	/**
	 * <p>
	 * Returns writeable children.
	 * </p>
	 * 
	 * @return <p>
	 *         the children
	 *         </p>
	 */
	@Override
	public ArrayList<IHdfWriteable> getWriteableChildren() {

		// Get the children in super
		ArrayList<IHdfWriteable> children = super.getWriteableChildren();

		// If super had no children
		if (children == null) {

			// Initialize to new array list
			children = new ArrayList<IHdfWriteable>();
		}

		// Add all of the Rings to the children array list
		children.addAll(rings);

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
	 *            The child that will be read.
	 * @return True if the child could be read and added to this block, false
	 *         otherwise.
	 */
	@Override
	public boolean readChild(IHdfReadable iHdfReadable) {

		if (iHdfReadable == null || !(iHdfReadable instanceof LWRComponent)) {
			return false;
		}

		// Cast the child into a LWRComponent
		LWRComponent childComponent = (LWRComponent) iHdfReadable;

		// If this is ring
		if (childComponent.getHDF5LWRTag() == HDF5LWRTagType.RING) {

			// Ass to the collection of Rings
			this.addRing((Ring) childComponent);

		}

		return true;
	}

	/*
	 * Overrides a method from LWRComponent.
	 */
	@Override
	public boolean writeAttributes(H5File h5File, H5Group h5Group) {
		boolean flag = true;

		flag &= super.writeAttributes(h5File, h5Group);
		flag &= HdfWriterFactory.writeDoubleAttribute(h5File, h5Group,
				"position", this.pos);

		return flag;
	}

	/*
	 * Overrides a method from LWRComponent.
	 */
	@Override
	public boolean readAttributes(H5Group h5Group) {

		// Local Declarations
		boolean flag = true;

		// Get values
		Double position = HdfReaderFactory.readDoubleAttribute(h5Group,
				"position");

		// Call super
		flag &= super.readAttributes(h5Group);

		// check values
		if (position == null || !flag || h5Group == null) {
			return false;
		}
		// If everything is valid, then set data
		this.pos = position.doubleValue();

		return true;
	}

	/**
	 * <p>
	 * Sets the position.
	 * </p>
	 * 
	 * @param pos
	 *            <p>
	 *            the position to set
	 *            </p>
	 */
	public void setPosition(double pos) {
		if (pos >= 0.0) {
			this.pos = pos;
		}
	}

	/**
	 * <p>
	 * Gets the position
	 * </p>
	 * 
	 * @return <p>
	 *         The position set.
	 *         </p>
	 */
	public double getPosition() {
		return this.pos;
	}

	@Override
	public int compareTo(MaterialBlock arg0) {

		if (this.pos < arg0.pos) {
			return -1;
		} else if (this.pos > arg0.pos) {
			return 1;
		} else {
			return 0;
		}

	}
}