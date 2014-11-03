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
import org.eclipse.ice.io.hdf.IHdfReadable;
import org.eclipse.ice.io.hdf.IHdfWriteable;

import java.lang.Comparable;
import java.util.ArrayList;

import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * The ring class represents a single instance of a material at a particular
 * radial coordinate within a cylindrical location on the rod. The height
 * variable on this class should uniformly represent the height from the bottom
 * of the MaterialBlock (or Z coordinate displacement) to help compensate for
 * varying types of materials across a cylindrical segment of a rod.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author s4h
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class Ring extends LWRComponent implements Comparable<Ring> {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The height of this Ring, which must be greater than zero.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected double height;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The inner radius of this Ring, which must be greater than or equal to
	 * zero and less than the outer radius.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected double innerRadius;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The outer radius of this Ring, which must be greater than the innerRadius
	 * value and greater than 0.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected double outerRadius;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The Material for this Ring.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected Material material;

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
	public Ring() {
		// begin-user-code

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
	 *            The name of this Ring.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Ring(String name) {
		// begin-user-code
		// Call nullary constructor
		this();

		// Pass values to operations
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
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Ring(String name, Material material, double height,
			double outerRadius) {
		// begin-user-code
		// Call nullary constructor
		this();

		// Pass values to operations
		this.setName(name);
		this.setMaterial(material);
		this.setHeight(height);
		this.setOuterRadius(outerRadius);

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
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Ring(String name, Material material, double height,
			double innerRadius, double outerRadius) {
		// begin-user-code
		// Call lower level nullary constructor
		this(name, material, height, outerRadius);

		// Set other values
		this.setInnerRadius(innerRadius);

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the height of this Ring.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The height of this Ring.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public double getHeight() {
		// begin-user-code
		return this.height;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the height of this Ring, which must be greater than zero.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param height
	 *            <p>
	 *            The height of this Ring, which must be greater than zero.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setHeight(double height) {
		// begin-user-code

		// If the height is not less than zero, valid
		if (height > 0.0) {
			this.height = height;
		}

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the inner radius of this Ring.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The inner radius of this Ring.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public double getInnerRadius() {
		// begin-user-code
		return this.innerRadius;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the inner radius of this Ring, which must be greater than or equal
	 * to zero and less than the outer radius.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param innerRadius
	 *            <p>
	 *            The inner radius of this Ring, which must be greater than or
	 *            equal to zero.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setInnerRadius(double innerRadius) {
		// begin-user-code

		// If the inner Radius is greater than or equal to 0 AND it is less than
		// current outer radius
		if (innerRadius >= 0.0 && innerRadius < this.outerRadius) {
			this.innerRadius = innerRadius;
		}

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the outer radius for this Ring. Must be greater than 0 and the
	 * inner radius.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The outer radius for this Ring
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public double getOuterRadius() {
		// begin-user-code

		return this.outerRadius;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the outer radius of this Ring, which must be greater than the
	 * innerRadius value.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param outerRadius
	 *            <p>
	 *            The outer radius of this Ring, which must be greater than the
	 *            innerRadius value.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setOuterRadius(double outerRadius) {
		// begin-user-code

		// If the outer Radius is greater than 0 AND it is greater than current
		// inner radius
		if (outerRadius > 0 && outerRadius > this.innerRadius) {
			this.outerRadius = outerRadius;
		}

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the Material for this ring.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The Material for this Ring.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Material getMaterial() {
		// begin-user-code
		return this.material;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the material. Can not set to null.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param material
	 *            <p>
	 *            The Material for this Ring.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setMaterial(Material material) {
		// begin-user-code

		// If the material is not null
		if (material != null) {
			this.material = material;
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

		// Get super's hash
		hash += super.hashCode();

		// Get hash from local attributes
		hash += 31 * this.height;
		hash += 31 * this.innerRadius;
		hash += 31 * this.outerRadius;
		hash += 31 * this.material.hashCode();

		// Return the hash
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
	public void copy(Ring otherObject) {
		// begin-user-code

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
		Ring ring = new Ring();

		// Copy contents
		ring.copy(this);

		// Return newly instantiated object
		return ring;

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
		flag &= HdfWriterFactory.writeDoubleAttribute(h5File, h5Group,
				"height", height);
		flag &= HdfWriterFactory.writeDoubleAttribute(h5File, h5Group,
				"innerRadius", innerRadius);
		flag &= HdfWriterFactory.writeDoubleAttribute(h5File, h5Group,
				"outerRadius", outerRadius);

		return flag;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<IHdfWriteable> getWriteableChildren() {
		// begin-user-code

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
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns an ArrayList of IHdfReadable child objects. If
	 * this IHdfReadable has no IHdfReadable child objects, then null is
	 * returned.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param iHdfReadable
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean readChild(IHdfReadable iHdfReadable) {
		// begin-user-code

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

	/**
	 * (non-Javadoc)
	 * 
	 * @see Comparable#compareTo(Object arg0)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int compareTo(Ring ring) {
		// begin-user-code
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
		// end-user-code
	}
}