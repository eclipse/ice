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

import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;

import org.eclipse.ice.io.hdf.HdfReaderFactory;
import org.eclipse.ice.io.hdf.HdfWriterFactory;

/**
 * <p>
 * The Tube class represents the hollow tubes in a FuelAssembly which allow for
 * the insertion of discrete poison rodlets (Guide Tubes) and instrument
 * thimbles (Instrument Tube).
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public class Tube extends Ring {
	/**
	 * <p>
	 * One of the TubeType enumeration values.
	 * </p>
	 * 
	 */
	private TubeType tubeType;

	/**
	 * <p>
	 * The nullary Constructor.
	 * </p>
	 * 
	 */
	public Tube() {
		// Call super constructor - sets up defaults
		super();

		// Set name and description for tube
		this.name = "Tube";
		this.description = "Tube's Description";

		// Setup other attributes for tube
		this.tubeType = TubeType.GUIDE;

		// Setup the LWRComponentType to the correct type
		this.HDF5LWRTag = HDF5LWRTagType.TUBE;

	}

	/**
	 * <p>
	 * The parameterized Constructor.
	 * </p>
	 * 
	 * @param name
	 *            <p>
	 *            The name of this Tube.
	 *            </p>
	 */
	public Tube(String name) {
		// Call nullary constructor
		this();

		// Set the name
		this.setName(name);

	}

	/**
	 * <p>
	 * A parameterized Constructor.
	 * </p>
	 * 
	 * @param name
	 *            <p>
	 *            The name of this Tube.
	 *            </p>
	 * @param tubeType
	 *            <p>
	 *            The TubeType enumeration value for this Tube.
	 *            </p>
	 */
	public Tube(String name, TubeType tubeType) {

		// Call non-nullary constructor
		this(name);

		// Call extra operations
		this.setTubeType(tubeType);

	}

	/**
	 * <p>
	 * A parameterized Constructor.
	 * </p>
	 * 
	 * @param name
	 *            <p>
	 *            The name of this Tube.
	 *            </p>
	 * @param tubeType
	 *            <p>
	 *            The TubeType enumeration value for this Tube.
	 *            </p>
	 * @param material
	 *            <p>
	 *            The Material for this Tube.
	 *            </p>
	 * @param height
	 *            <p>
	 *            The height of this Tube, which must be greater than zero.
	 *            </p>
	 * @param outerRadius
	 *            <p>
	 *            The outer radius of this Tube, which must be greater than the
	 *            innerRadius value.
	 *            </p>
	 */
	public Tube(String name, TubeType tubeType, Material material,
			double height, double outerRadius) {
		// Call non-nullary constructor
		this(name, tubeType);

		// Set other values
		super.setMaterial(material);
		super.setHeight(height);
		super.setOuterRadius(outerRadius);

	}

	/**
	 * <p>
	 * A parameterized Constructor.
	 * </p>
	 * 
	 * @param name
	 *            <p>
	 *            The name of this Tube.
	 *            </p>
	 * @param tubeType
	 *            <p>
	 *            The TubeType enumeration value for this Tube.
	 *            </p>
	 * @param material
	 *            <p>
	 *            The Material for this Tube.
	 *            </p>
	 * @param height
	 *            <p>
	 *            The height of this Tube, which must be greater than zero.
	 *            </p>
	 * @param innerRadius
	 *            <p>
	 *            The inner radius of this Tube, which must be greater than or
	 *            equal to zero.
	 *            </p>
	 * @param outerRadius
	 *            <p>
	 *            The outer radius of this Tube, which must be greater than the
	 *            innerRadius value.
	 *            </p>
	 */
	public Tube(String name, TubeType tubeType, Material material,
			double height, double innerRadius, double outerRadius) {
		// Call non-nullary constructor
		this(name, tubeType, material, height, outerRadius);

		// Set other values
		this.setInnerRadius(innerRadius);

	}

	/**
	 * <p>
	 * Returns the TubeType enumeration value for this Tube.
	 * </p>
	 * 
	 * @return <p>
	 *         The TubeType enumeration value for this Tube.
	 *         </p>
	 */
	public TubeType getTubeType() {

		return this.tubeType;
	}

	/**
	 * <p>
	 * Sets the TubeType enumeration value for this Tube. Can not set to null.
	 * </p>
	 * 
	 * @param tubeType
	 *            <p>
	 *            The TubeType enumeration value for this Tube.
	 *            </p>
	 */
	public void setTubeType(TubeType tubeType) {

		// If tube type is not null
		if (tubeType != null) {
			this.tubeType = tubeType;
		}

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
		Tube tube;
		boolean retVal = false;

		// If they are equal to the same object, return true
		if (this == otherObject) {
			return true;
		}
		if (otherObject != null && otherObject instanceof Tube) {

			// Cast
			tube = (Tube) otherObject;

			// Check values
			retVal = super.equals(otherObject)
					&& this.tubeType.equals(tube.tubeType);
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

		// Get hash from super
		int hash = super.hashCode();

		// Get local hash
		hash += 31 * this.tubeType.hashCode();

		// Return the hashCode
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
	public void copy(Tube otherObject) {

		// If the otherObject is null, return
		if (otherObject == null) {
			return;
		}
		// Copy contents - super
		super.copy(otherObject);

		// copy local contents
		this.tubeType = otherObject.tubeType;

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
		Tube tube = new Tube();

		// Copy contents
		tube.copy(this);

		// Return newly instantiated object
		return tube;

	}

	/*
	 * Overrides a method from LWRComponent.
	 */
	@Override
	public boolean writeAttributes(H5File h5File, H5Group h5Group) {
		boolean flag = true;

		flag &= super.writeAttributes(h5File, h5Group);
		flag &= HdfWriterFactory.writeStringAttribute(h5File, h5Group,
				"tubeType", tubeType.toString());

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
		TubeType type1 = TubeType.GUIDE;
		TubeType type = type1.toType((HdfReaderFactory.readStringAttribute(
				h5Group, "tubeType")));

		// Call super
		flag &= super.readAttributes(h5Group);

		// check values
		if (type == null || !flag || h5Group == null) {
			return false;
		}
		// If everything is valid, then set data
		this.tubeType = type;

		return true;

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
	@Override
	public void accept(ILWRComponentVisitor visitor) {
		visitor.visit(this);
	}
}