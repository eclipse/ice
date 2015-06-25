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

import java.util.ArrayList;

import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;

import org.eclipse.ice.io.hdf.HdfReaderFactory;
import org.eclipse.ice.io.hdf.HdfWriterFactory;
import org.eclipse.ice.io.hdf.IHdfReadable;
import org.eclipse.ice.io.hdf.IHdfWriteable;

/**
 * <p>
 * The ring class represents a single instance of a material at a particular
 * radial coordinate within a cylindrical location on the rod. The height
 * variable on this class should uniformly represent the height from the bottom
 * of the MaterialBlock (or Z coordinate displacement) to help compensate for
 * varying types of materials across a cylindrical segment of a rod.
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public class Ring extends LWRComponent implements Comparable<Ring> {
	/**
	 * <p>
	 * The height of this Ring, which must be greater than zero.
	 * </p>
	 * 
	 */
	protected double height;
	/**
	 * <p>
	 * The inner radius of this Ring, which must be greater than or equal to
	 * zero and less than the outer radius.
	 * </p>
	 * 
	 */
	protected double innerRadius;
	/**
	 * <p>
	 * The outer radius of this Ring, which must be greater than the innerRadius
	 * value and greater than 0.
	 * </p>
	 * 
	 */
	protected double outerRadius;
	/**
	 * <p>
	 * The Material for this Ring.
	 * </p>
	 * 
	 */
	protected Material material;

	/**
	 * <p>
	 * The nullary Constructor.
	 * </p>
	 * 
	 */
	public Ring() {

		// Set default LWRComponent values
		this.name = "Ring";
		this.description = "Ring's Description";
		this.id = 1;

		// Set default values for other pieces
		this.height = 1.0;
		this.outerRadius = 1.0;
		this.innerRadius = 0.0;
		this.material = new Material();

		// Setup the LWRComponentType to correct type
		this.HDF5LWRTag = HDF5LWRTagType.RING;

	}

	/**
	 * <p>
	 * A parameterized Constructor.
	 * </p>
	 * 
	 * @param name
	 *            <p>
	 *            The name of this Ring.
	 *            </p>
	 */
	public Ring(String name) {
		// Call nullary constructor
		this();

		// Pass values to operations
		this.setName(name);

	}

	/**
	 * <p>
	 * A parameterized Constructor.
	 * </p>
	 * 
	 * @param name
	 *            <p>
	 *            The name of this Ring.
	 *            </p>
	 * @param material
	 *            <p>
	 *            The Material for this Ring.
	 *            </p>
	 * @param height
	 *            <p>
	 *            The height of this Ring, which must be greater than zero.
	 *            </p>
	 * @param outerRadius
	 *            <p>
	 *            The outer radius of this Ring, which must be greater than the
	 *            innerRadius value.
	 *            </p>
	 */
	public Ring(String name, Material material, double height,
			double outerRadius) {
		// Call nullary constructor
		this();

		// Pass values to operations
		this.setName(name);
		this.setMaterial(material);
		this.setHeight(height);
		this.setOuterRadius(outerRadius);

	}

	/**
	 * <p>
	 * A parameterized Constructor.
	 * </p>
	 * 
	 * @param name
	 *            <p>
	 *            The name of this Ring.
	 *            </p>
	 * @param material
	 *            <p>
	 *            The Material for this Ring.
	 *            </p>
	 * @param height
	 *            <p>
	 *            The height of this Ring, which must be greater than zero.
	 *            </p>
	 * @param innerRadius
	 *            <p>
	 *            The inner radius of this Ring, which must be greater than or
	 *            equal to zero.
	 *            </p>
	 * @param outerRadius
	 *            <p>
	 *            The outer radius of this Ring, which must be greater than the
	 *            innerRadius value.
	 *            </p>
	 */
	public Ring(String name, Material material, double height,
			double innerRadius, double outerRadius) {
		// Call lower level nullary constructor
		this(name, material, height, outerRadius);

		// Set other values
		this.setInnerRadius(innerRadius);

	}

	/**
	 * <p>
	 * Returns the height of this Ring.
	 * </p>
	 * 
	 * @return <p>
	 *         The height of this Ring.
	 *         </p>
	 */
	public double getHeight() {
		return this.height;
	}

	/**
	 * <p>
	 * Sets the height of this Ring, which must be greater than zero.
	 * </p>
	 * 
	 * @param height
	 *            <p>
	 *            The height of this Ring, which must be greater than zero.
	 *            </p>
	 */
	public void setHeight(double height) {

		// If the height is not less than zero, valid
		if (height > 0.0) {
			this.height = height;
		}

	}

	/**
	 * <p>
	 * Returns the inner radius of this Ring.
	 * </p>
	 * 
	 * @return <p>
	 *         The inner radius of this Ring.
	 *         </p>
	 */
	public double getInnerRadius() {
		return this.innerRadius;

	}

	/**
	 * <p>
	 * Sets the inner radius of this Ring, which must be greater than or equal
	 * to zero and less than the outer radius.
	 * </p>
	 * 
	 * @param innerRadius
	 *            <p>
	 *            The inner radius of this Ring, which must be greater than or
	 *            equal to zero.
	 *            </p>
	 */
	public void setInnerRadius(double innerRadius) {

		// If the inner Radius is greater than or equal to 0 AND it is less than
		// current outer radius
		if (innerRadius >= 0.0 && innerRadius < this.outerRadius) {
			this.innerRadius = innerRadius;
		}

	}

	/**
	 * <p>
	 * Returns the outer radius for this Ring. Must be greater than 0 and the
	 * inner radius.
	 * </p>
	 * 
	 * @return <p>
	 *         The outer radius for this Ring
	 *         </p>
	 */
	public double getOuterRadius() {

		return this.outerRadius;
	}

	/**
	 * <p>
	 * Sets the outer radius of this Ring, which must be greater than the
	 * innerRadius value.
	 * </p>
	 * 
	 * @param outerRadius
	 *            <p>
	 *            The outer radius of this Ring, which must be greater than the
	 *            innerRadius value.
	 *            </p>
	 */
	public void setOuterRadius(double outerRadius) {

		// If the outer Radius is greater than 0 AND it is greater than current
		// inner radius
		if (outerRadius > 0 && outerRadius > this.innerRadius) {
			this.outerRadius = outerRadius;
		}

	}

	/**
	 * <p>
	 * Returns the Material for this ring.
	 * </p>
	 * 
	 * @return <p>
	 *         The Material for this Ring.
	 *         </p>
	 */
	public Material getMaterial() {
		return this.material;
	}

	/**
	 * <p>
	 * Sets the material. Can not set to null.
	 * </p>
	 * 
	 * @param material
	 *            <p>
	 *            The Material for this Ring.
	 *            </p>
	 */
	public void setMaterial(Material material) {

		// If the material is not null
		if (material != null) {
			this.material = material;
		}

	}

	/*
	 * Overrides a method from LWRComponent.
	 */
	@Override
	public boolean equals(Object otherObject) {

		// Local Declarations
		Ring ring;
		boolean retVal = false;

		// If this object is the same on the heap, return true

		if (this == otherObject) {
			return true;
		}
		// If the otherObject is null or not an instance of this object, return
		// false
		if (otherObject != null && otherObject instanceof Ring) {

			// Cast
			ring = (Ring) otherObject;

			// Check values
			retVal = (super.equals(otherObject) && this.height == ring.height
					&& this.innerRadius == ring.innerRadius
					&& this.outerRadius == ring.outerRadius && this.material
					.equals(ring.material));

		}

		// Return the retVal
		return retVal;

	}

	/*
	 * Overrides a method from LWRComponent.
	 */
	@Override
	public int hashCode() {

		// Local Declarations
		int hash = 31;

		// Get super's hash
		hash += super.hashCode();

		// Get hash from local attributes
		hash += 31 * this.height;
		hash += 31 * this.innerRadius;
		hash += 31 * this.outerRadius;
		hash += 31 * this.material.hashCode();

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
	public void copy(Ring otherObject) {

		// If the otherObject is null, return
		if (otherObject == null) {
			return;
		}
		// Copy values - super
		super.copy(otherObject);

		// Copy local values
		this.height = otherObject.height;
		this.innerRadius = otherObject.innerRadius;
		this.outerRadius = otherObject.outerRadius;

		// Deep copy Material
		this.material = (Material) otherObject.material.clone();

	}

	/*
	 * Overrides a method from LWRComponent.
	 */
	@Override
	public Object clone() {

		// Local Declarations
		Ring ring = new Ring();

		// Copy contents
		ring.copy(this);

		// Return newly instantiated object
		return ring;

	}

	/*
	 * Overrides a method from LWRComponent.
	 */
	@Override
	public boolean writeAttributes(H5File h5File, H5Group h5Group) {
		boolean flag = true;

		flag &= super.writeAttributes(h5File, h5Group);
		flag &= HdfWriterFactory.writeDoubleAttribute(h5File, h5Group,
				"height", height);
		flag &= HdfWriterFactory.writeDoubleAttribute(h5File, h5Group,
				"innerRadius", innerRadius);
		flag &= HdfWriterFactory.writeDoubleAttribute(h5File, h5Group,
				"outerRadius", outerRadius);

		return flag;
	}

	/*
	 * Overrides a method from LWRComponent.
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

		// Add the material to children
		children.add(this.material);

		return children;
	}

	/*
	 * Overrides a method from LWRComponent.
	 */
	@Override
	public boolean readAttributes(H5Group h5Group) {

		// Local Declarations
		boolean flag = true;

		// Get values
		Double height = HdfReaderFactory.readDoubleAttribute(h5Group, "height");
		Double innerRadius = HdfReaderFactory.readDoubleAttribute(h5Group,
				"innerRadius");
		Double outerRadius = HdfReaderFactory.readDoubleAttribute(h5Group,
				"outerRadius");

		// Call super
		flag &= super.readAttributes(h5Group);

		// check values
		if (height == null || innerRadius == null || outerRadius == null
				|| !flag || h5Group == null) {
			return false;
		}
		// If everything is valid, then set data
		this.height = height.doubleValue();
		this.innerRadius = innerRadius.doubleValue();
		this.outerRadius = outerRadius.doubleValue();

		return true;
	}

	/*
	 * Overrides a method from LWRComponent.
	 */
	@Override
	public boolean readChild(IHdfReadable iHdfReadable) {

		if (iHdfReadable == null || !(iHdfReadable instanceof LWRComponent)) {
			return false;
		}

		// Cast the child into a LWRComponent
		LWRComponent childComponent = (LWRComponent) iHdfReadable;

		// If this is a material
		if (childComponent.getHDF5LWRTag() == HDF5LWRTagType.MATERIAL) {

			// Assign to correct object
			this.material = (Material) childComponent;

		}

		return true;
	}

	/*
	 * Overrides a method from LWRComponent.
	 */
	@Override
	public void accept(ILWRComponentVisitor visitor) {
		visitor.visit(this);
	}

	/*
	 * Implements a method from Comparable.
	 */
	@Override
	public int compareTo(Ring ring) {
		// Create some constants
		final int BEFORE = -1;
		final int EQUAL = 0;
		final int AFTER = 1;

		// Check if this ring's outer radius is less than or equal to that
		// ring's inner radius
		if (this.getOuterRadius() <= ring.getInnerRadius()) {
			return BEFORE;
		}

		// Check if this ring's inner radius is less than or equal to that
		// ring's outer radius
		if (this.getInnerRadius() >= ring.getOuterRadius()) {
			return AFTER;
		}

		// Otherwise return equal which means in this context that the rings
		// overlap
		return EQUAL;
	}
}