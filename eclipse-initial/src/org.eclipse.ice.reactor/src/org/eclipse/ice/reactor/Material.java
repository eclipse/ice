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

import org.eclipse.ice.io.hdf.HdfReaderFactory;
import org.eclipse.ice.io.hdf.HdfWriterFactory;
import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * The Material class is a representation of any material property of any class
 * within an LWReactor. The setName for this class should represent the material
 * name. The Material Class can also indicate what type of material is defined
 * (solid, liquid, gas).
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author s4h
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class Material extends LWRComponent {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The phase of this Material. Must be one of the enumeration values listed
	 * in MaterialType.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private MaterialType materialType;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The nullary Constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Material() {
		// begin-user-code

		// Default Values
		this.name = "Material";
		this.description = "Material's Description";
		this.id = 1;

		// Setup Default Material Type
		this.materialType = MaterialType.SOLID;

		// Setup LWRComponentType to correct type
		this.HDF5LWRTag = HDF5LWRTagType.MATERIAL;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A parameterized Constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param name
	 *            <p>
	 *            The name of this Material.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Material(String name) {
		// begin-user-code

		// Call the nullary constructor
		this();
		// Set the name with the LWRComponent's setName method.
		this.setName(name);

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A parameterized Constructor.
	 * </p>
	 * <!-- end-UML-doc -->
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
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Material(String name, MaterialType materialType) {
		// begin-user-code
		// Call the nullary constructor
		this();
		// Set the name with the LWRComponent's setName method.
		this.setName(name);
		// Set the material type with the correct method
		this.setMaterialType(materialType);
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the phase of this Material. Must be one of the enumeration values
	 * listed in MaterialType.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param materialType
	 *            <p>
	 *            The phase of this Material. Must be one of the enumeration
	 *            values listed in MaterialType.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setMaterialType(MaterialType materialType) {
		// begin-user-code

		// If the material type is not null, set the type.
		if (materialType != null) {
			this.materialType = materialType;
		}

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the phase of this Material. Must be one of the enumeration values
	 * listed in MaterialType.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The phase of this Material. Must be one of the enumeration values
	 *         listed in MaterialType.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public MaterialType getMaterialType() {
		// begin-user-code

		return this.materialType;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Overrides the equals operation to check the attributes on this object
	 * with another object of the same type. Returns true if the objects are
	 * equal. False otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherObject
	 *            <p>
	 *            The object to be compared.
	 *            </p>
	 * @return <p>
	 *         True if otherObject is equal. False otherwise.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean equals(Object otherObject) {
		// begin-user-code

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

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the hashCode of the object.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The hash of the object.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int hashCode() {
		// begin-user-code

		// Local Declarations
		int hash = 31;

		// Hash values
		hash += super.hashCode();
		hash += 31 * this.materialType.hashCode();

		// return the hash
		return hash;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Deep copies the contents of the object.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherObject
	 *            <p>
	 *            The object to be copied.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void copy(Material otherObject) {
		// begin-user-code

		// If otherObject is null, return
		if (otherObject == null) {
			return;
		}
		// Copy contents
		super.copy(otherObject);
		this.materialType = otherObject.materialType;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Deep copies and returns a newly instantiated object.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The newly instantiated copied object.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Object clone() {
		// begin-user-code
		// Local Delcarations
		Material material = new Material();

		// Copy contents
		material.copy(this);

		// Return newly instantiated object
		return material;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @param h5File
	 * @param h5Group
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean writeAttributes(H5File h5File, H5Group h5Group) {
		// begin-user-code
		boolean flag = true;

		flag &= super.writeAttributes(h5File, h5Group);
		flag &= HdfWriterFactory.writeStringAttribute(h5File, h5Group,
				"materialType", materialType.toString());

		return flag;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @param h5Group
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean readAttributes(H5Group h5Group) {
		// begin-user-code

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

		// end-user-code
	}

}