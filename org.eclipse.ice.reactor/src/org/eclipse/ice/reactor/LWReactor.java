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
package org.eclipse.ice.reactor;

import javax.xml.bind.annotation.XmlTransient;

import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;

import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.io.hdf.HdfReaderFactory;
import org.eclipse.ice.io.hdf.HdfWriterFactory;

/**
 * <p>
 * The LWReactor class represents any Light Water Nuclear Reactor.
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public class LWReactor extends LWRComposite {

	/**
	 * <p>
	 * The size. Defaults to 1 if not set correctly in the constructor.
	 * </p>
	 * 
	 */
	@XmlTransient
	protected int size;

	/**
	 * A default constructor that should ONLY be used for persistence and
	 * testing. It is equivalent to LWReactor(15).
	 */
	public LWReactor() {
		this(15);
	}

	/**
	 * <p>
	 * A parameterized constructor.
	 * </p>
	 * 
	 * @param size
	 *            <p>
	 *            The size of the reactor.
	 *            </p>
	 */
	public LWReactor(int size) {

		// Setup size
		this.size = size;
		if (size <= 0) {
			this.size = 1;
		}

		// Setup default LWRComponent info
		this.name = "LWReactor 1";
		this.description = "LWReactor 1's Description";
		this.id = 1;

		// Setup LWRComponentType to correct type
		this.HDF5LWRTag = HDF5LWRTagType.LWREACTOR;

	}

	/**
	 * <p>
	 * An operation that overrides the LWRComposite's operation. This operation
	 * does nothing and requires that the appropriate, more defined, associated
	 * operation to be utilized on this class.
	 * </p>
	 * 
	 * @param component
	 *            <p>
	 *            The Component to be added.
	 *            </p>
	 */
	@Override
	public void addComponent(Component component) {

		// Does nothing

	}

	/**
	 * <p>
	 * An operation that overrides the LWRComposite's operation. This operation
	 * does nothing and requires that the appropriate, more defined, associated
	 * operation to be utilized on this class.
	 * </p>
	 * 
	 * @param childId
	 *            <p>
	 *            The id of the Component to remove.
	 *            </p>
	 */
	@Override
	public void removeComponent(int childId) {

		// Does nothing

	}

	/**
	 * <p>
	 * An operation that overrides the LWRComposite's operation. This operation
	 * does nothing and requires that the appropriate, more defined, associated
	 * operation to be utilized on this class.
	 * </p>
	 * 
	 * @param name
	 *            <p>
	 *            The name of the Component to remove.
	 *            </p>
	 */
	@Override
	public void removeComponent(String name) {

		// Does nothing

	}

	/**
	 * <p>
	 * Returns the size (usually the diameter) of the reactor.
	 * </p>
	 * 
	 * @return The size.
	 */
	public int getSize() {

		return this.size;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.reactor.LWRComponent#writeAttributes(ncsa.hdf.object.h5.H5File, ncsa.hdf.object.h5.H5Group)
	 */
	@Override
	public boolean writeAttributes(H5File h5File, H5Group h5Group) {

		boolean flag = true;

		flag &= super.writeAttributes(h5File, h5Group);
		flag &= HdfWriterFactory.writeIntegerAttribute(h5File, h5Group, "size",
				size);

		return flag;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.reactor.LWRComposite#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object otherObject) {

		// Local Declarations
		LWReactor reactor;
		boolean retVal = false;

		// Check to see if they are equal to the same heap - return true
		if (otherObject == this) {
			return true;
		}
		// If otherObject is null or is not an instance of this object, return
		// false
		if (otherObject != null && otherObject instanceof LWReactor) {

			// Cast it
			reactor = (LWReactor) otherObject;

			// Check values
			retVal = super.equals(otherObject) && this.size == reactor.size;
		}

		// Return retVal
		return retVal;

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.reactor.LWRComposite#hashCode()
	 */
	@Override
	public int hashCode() {

		// Local Declarations
		int hash = super.hashCode();

		// Hash local values
		hash += 31 * this.size;

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
	public void copy(LWReactor otherObject) {

		// If the otherObject is null, return
		if (otherObject == null) {
			return;
		}
		// Copy contents - super
		super.copy(otherObject);

		// Copy local contents
		this.size = otherObject.size;

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.reactor.LWRComposite#clone()
	 */
	@Override
	public Object clone() {

		// Local Declarations
		LWReactor reactor = new LWReactor(0);

		// Copy contents
		reactor.copy(this);

		// Return newly instantiated object
		return reactor;

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.reactor.LWRComponent#readAttributes(ncsa.hdf.object.h5.H5Group)
	 */
	@Override
	public boolean readAttributes(H5Group h5Group) {

		// Local Declarations
		boolean flag = true;

		// Get values
		Integer size = HdfReaderFactory.readIntegerAttribute(h5Group, "size");

		// Call super
		flag &= super.readAttributes(h5Group);

		// check values
		if (size == null || !flag || h5Group == null) {
			return false;
		}
		// If everything is valid, then set data
		this.size = size.intValue();

		return true;

	}

}