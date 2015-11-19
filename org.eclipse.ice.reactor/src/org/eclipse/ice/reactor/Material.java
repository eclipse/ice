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
 * The Material class is a representation of any material property of any class
 * within an LWReactor. The setName for this class should represent the material
 * name. The Material Class can also indicate what type of material is defined
 * (solid, liquid, gas).
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public class Material extends LWRComponent {
	/**
	 * <p>
	 * The phase of this Material. Must be one of the enumeration values listed
	 * in MaterialType.
	 * </p>
	 * 
	 */
	private MaterialType materialType;

	/**
	 * <p>
	 * The nullary Constructor.
	 * </p>
	 * 
	 */
	public Material() {

		// Default Values
		this.name = "Material";
		this.description = "Material's Description";
		this.id = 1;

		// Setup Default Material Type
		this.materialType = MaterialType.SOLID;

		// Setup LWRComponentType to correct type
		this.HDF5LWRTag = HDF5LWRTagType.MATERIAL;

	}

	/**
	 * <p>
	 * A parameterized Constructor.
	 * </p>
	 * 
	 * @param name
	 *            <p>
	 *            The name of this Material.
	 *            </p>
	 */
	public Material(String name) {

		// Call the nullary constructor
		this();
		// Set the name with the LWRComponent's setName method.
		this.setName(name);

	}

	/**
	 * <p>
	 * A parameterized Constructor.
	 * </p>
	 * 
	 * @param name
	 *            <p>
	 *            The name of this Material.
	 *            </p>
	 * @param materialType
	 *            <p>
	 *            The phase of this Material. Must be one of the enumeration
	 *            values listed in MaterialType.
	 *            </p>
	 */
	public Material(String name, MaterialType materialType) {
		// Call the nullary constructor
		this();
		// Set the name with the LWRComponent's setName method.
		this.setName(name);
		// Set the material type with the correct method
		this.setMaterialType(materialType);
	}

	/**
	 * <p>
	 * Sets the phase of this Material. Must be one of the enumeration values
	 * listed in MaterialType.
	 * </p>
	 * 
	 * @param materialType
	 *            <p>
	 *            The phase of this Material. Must be one of the enumeration
	 *            values listed in MaterialType.
	 *            </p>
	 */
	public void setMaterialType(MaterialType materialType) {

		// If the material type is not null, set the type.
		if (materialType != null) {
			this.materialType = materialType;
		}

	}

	/**
	 * <p>
	 * Returns the phase of this Material. Must be one of the enumeration values
	 * listed in MaterialType.
	 * </p>
	 * 
	 * @return <p>
	 *         The phase of this Material. Must be one of the enumeration values
	 *         listed in MaterialType.
	 *         </p>
	 */
	public MaterialType getMaterialType() {

		return this.materialType;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.reactor.LWRComponent#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object otherObject) {

		// Local Declarations
		Material material;
		boolean retVal = false;

		// If they are equal to the same object, return true
		if (this == otherObject) {
			return true;
		}
		// If the otherObject is null or is not an instance of this object,
		// return false
		if (otherObject != null && otherObject instanceof Material) {

			// Cast object
			material = (Material) otherObject;

			// Check values
			retVal = (super.equals(otherObject) && this.materialType
					.equals(material.materialType));

		}

		// Return retVal
		return retVal;

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.reactor.LWRComponent#hashCode()
	 */
	@Override
	public int hashCode() {

		// Local Declarations
		int hash = 31;

		// Hash values
		hash += super.hashCode();
		hash += 31 * this.materialType.hashCode();

		// return the hash
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
	public void copy(Material otherObject) {

		// If otherObject is null, return
		if (otherObject == null) {
			return;
		}
		// Copy contents
		super.copy(otherObject);
		this.materialType = otherObject.materialType;

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.reactor.LWRComponent#clone()
	 */
	@Override
	public Object clone() {
		// Local Delcarations
		Material material = new Material();

		// Copy contents
		material.copy(this);

		// Return newly instantiated object
		return material;

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.reactor.LWRComponent#writeAttributes(ncsa.hdf.object.h5.H5File, ncsa.hdf.object.h5.H5Group)
	 */
	@Override
	public boolean writeAttributes(H5File h5File, H5Group h5Group) {
		boolean flag = true;

		flag &= super.writeAttributes(h5File, h5Group);
		flag &= HdfWriterFactory.writeStringAttribute(h5File, h5Group,
				"materialType", materialType.toString());

		return flag;
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
		MaterialType type1 = MaterialType.GAS;
		MaterialType type = type1.toType((HdfReaderFactory.readStringAttribute(
				h5Group, "materialType")));

		// Call super
		flag &= super.readAttributes(h5Group);

		// check values
		if (type == null || !flag || h5Group == null) {
			return false;
		}
		// If everything is valid, then set data
		this.materialType = type;

		return true;

	}

}