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
 * The Tube class represents the hollow tubes in a FuelAssembly which allow for
 * the insertion of discrete poison rodlets (Guide Tubes) and instrument
 * thimbles (Instrument Tube).
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author s4h
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class Tube extends Ring {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * One of the TubeType enumeration values.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private TubeType tubeType;

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
	public Tube() {
		// begin-user-code
		// Call super constructor - sets up defaults
		super();

		// Set name and description for tube
		this.name = "Tube";
		this.description = "Tube's Description";

		// Setup other attributes for tube
		this.tubeType = TubeType.GUIDE;

		// Setup the LWRComponentType to the correct type
		this.HDF5LWRTag = HDF5LWRTagType.TUBE;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The parameterized Constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param name
	 *            <p>
	 *            The name of this Tube.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Tube(String name) {
		// begin-user-code
		// Call nullary constructor
		this();

		// Set the name
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
	 *            The name of this Tube.
	 *            </p>
	 * @param tubeType
	 *            <p>
	 *            The TubeType enumeration value for this Tube.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Tube(String name, TubeType tubeType) {
		// begin-user-code

		// Call non-nullary constructor
		this(name);

		// Call extra operations
		this.setTubeType(tubeType);

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
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Tube(String name, TubeType tubeType, Material material,
			double height, double outerRadius) {
		// begin-user-code
		// Call non-nullary constructor
		this(name, tubeType);

		// Set other values
		super.setMaterial(material);
		super.setHeight(height);
		super.setOuterRadius(outerRadius);

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
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Tube(String name, TubeType tubeType, Material material,
			double height, double innerRadius, double outerRadius) {
		// begin-user-code
		// Call non-nullary constructor
		this(name, tubeType, material, height, outerRadius);

		// Set other values
		this.setInnerRadius(innerRadius);

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the TubeType enumeration value for this Tube.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The TubeType enumeration value for this Tube.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public TubeType getTubeType() {
		// begin-user-code

		return this.tubeType;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the TubeType enumeration value for this Tube. Can not set to null.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param tubeType
	 *            <p>
	 *            The TubeType enumeration value for this Tube.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setTubeType(TubeType tubeType) {
		// begin-user-code

		// If tube type is not null
		if (tubeType != null) {
			this.tubeType = tubeType;
		}

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

		// Get hash from super
		int hash = super.hashCode();

		// Get local hash
		hash += 31 * this.tubeType.hashCode();

		// Return the hashCode
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
	public void copy(Tube otherObject) {
		// begin-user-code

		// If the otherObject is null, return
		if (otherObject == null) {
			return;
		}
		// Copy contents - super
		super.copy(otherObject);

		// copy local contents
		this.tubeType = otherObject.tubeType;

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
		// Local Declarations
		Tube tube = new Tube();

		// Copy contents
		tube.copy(this);

		// Return newly instantiated object
		return tube;

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
				"tubeType", tubeType.toString());

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

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation accepts an ILWRComponentVisitor that can be visit the
	 * LWRComponent to ascertain its type and perform various type-specific
	 * operations.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param visitor
	 *            <p>
	 *            The visitor
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void accept(ILWRComponentVisitor visitor) {
		// begin-user-code
		visitor.visit(this);
		// end-user-code
	}
}