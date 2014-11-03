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
import java.util.ArrayList;
import java.util.TreeSet;

import org.eclipse.ice.io.hdf.HdfReaderFactory;
import org.eclipse.ice.io.hdf.HdfWriterFactory;
import org.eclipse.ice.io.hdf.IHdfReadable;
import org.eclipse.ice.io.hdf.IHdfWriteable;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * The LWRRod class is a generalized class representing a basic rod as a
 * collection of materialBlocks contained by a ring (aka the clad). A LWRRod
 * should be considered the basis for all "rod-tyoes" implemented within a
 * reactor. Details concerning material compositions should be taken care of on
 * the collections of rings on the materialblocks.
 * </p>
 * <p>
 * Please note that the MaterialBlocks MUST HAVE UNIQUE POSITIONS SET! If there
 * are material blocks with 2 of the same EXACT position type, then the first
 * materialblock will be overridden by the second materialblock of the same
 * position value. This is very important to understand when adding
 * materialBlocks to the grid.
 * </p>
 * <p>
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author s4h
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class LWRRod extends LWRComponent {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * An annular Ring which surrounds this LWRRod's MaterialBlocks list.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private Ring clad;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A Material of MaterialType.GAS that fills the voids within this LWRRod.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private Material fillGas;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The pressure of the fillGas Material. Can not be less than or equal to 0.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private double pressure;
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private TreeSet<MaterialBlock> materialBlocks;

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
	public LWRRod() {
		// begin-user-code

		// Set default LWRComponent Values
		this.name = "LWRRod";
		this.description = "LWRRod's Description";
		this.id = 1;

		// Set default values for privates
		this.pressure = 2200.00;
		Material cladMaterial = new Material("Zirc", MaterialType.SOLID);
		this.clad = new Ring("Clad", cladMaterial, -1.0, -1.0);
		this.fillGas = new Material("Void", MaterialType.GAS);
		this.materialBlocks = new TreeSet<MaterialBlock>();

		// Setup LWRComponentType to correct type
		this.HDF5LWRTag = HDF5LWRTagType.LWRROD;

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
	 *            The name of this LWRRod.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public LWRRod(String name) {
		// begin-user-code
		// Call nullary operator
		this();
		// Pass name
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
	 *            The name of this LWRRod.
	 *            </p>
	 * @param fillGas
	 *            <p>
	 *            A Material of MaterialType.GAS that fills the voids within
	 *            this LWRRod.
	 *            </p>
	 * @param pressure
	 *            <p>
	 *            The pressure of the fillGas Material.
	 *            </p>
	 * @param materialBlocks
	 *            <p>
	 *            The MaterialBlock list within this LWRRod.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public LWRRod(String name, Material fillGas, double pressure,
			TreeSet<MaterialBlock> materialBlocks) {
		// begin-user-code

		// Call the respective operators for passed parameters
		this(name);
		this.setFillGas(fillGas);
		this.setPressure(pressure);
		this.setMaterialBlocks(materialBlocks);

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns a Material of MaterialType.GAS that fills the voids within this
	 * LWRRod.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         A Material of MaterialType.GAS that fills the voids within this
	 *         LWRRod.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Material getFillGas() {
		// begin-user-code
		return this.fillGas;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the Material of MaterialType.GAS that fills the voids within this
	 * LWRRod. Can not be set to null.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param fillGas
	 *            <p>
	 *            A Material of MaterialType.GAS that fills the voids within
	 *            this LWRRod.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setFillGas(Material fillGas) {
		// begin-user-code
		// if fillGas is not null
		if (fillGas != null) {
			this.fillGas = fillGas;
		}

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the pressure of the fillGas Material. Can not be set less than or
	 * equal to 0.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param pressure
	 *            <p>
	 *            The pressure of the fillGas Material.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setPressure(double pressure) {
		// begin-user-code
		// if pressure is not 0 or negative
		if (pressure > 0.0) {
			this.pressure = pressure;
		}

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the pressure of the fillGas Material.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The pressure of the fillGas Material.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public double getPressure() {
		// begin-user-code
		return this.pressure;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the list of MaterialBlocks within this LWRRod.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The Stack object within this LWRRod.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public TreeSet<MaterialBlock> getMaterialBlocks() {
		// begin-user-code
		return this.materialBlocks;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the list of MaterialBlocks within this LWRRod. Can not be set null.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param materialBlocks
	 *            <p>
	 *            The materialBlocks list within this LWRRod.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setMaterialBlocks(TreeSet<MaterialBlock> materialBlocks) {
		// begin-user-code
		if (materialBlocks != null && !(materialBlocks.isEmpty())) {
			this.materialBlocks = materialBlocks;
		}

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the clad object of this LWRRod object, if set or null.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Ring getClad() {
		// begin-user-code
		return this.clad;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the clad object for this LWRRod. It can not be set to null.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param clad
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setClad(Ring clad) {
		// begin-user-code
		// if clad is not null
		if (clad != null) {
			this.clad = clad;
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
		LWRRod rod;
		boolean retVal = false;
		// If the objects are the same on the heap, return true
		if (this == otherObject) {
			return true;
		}
		// If the object is null or not an instance of this object, return false
		if (otherObject != null && otherObject instanceof LWRRod) {
			// Cast it
			rod = (LWRRod) otherObject;
			// Check values
			retVal = (super.equals(otherObject) && this.clad.equals(rod.clad)
					&& this.fillGas.equals(rod.fillGas)
					&& this.materialBlocks.equals(rod.materialBlocks) && this.pressure == rod.pressure);
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
		int hash = super.hashCode();

		// Hash local contents
		hash += 31 * this.pressure;
		hash += 31 * this.clad.hashCode();
		hash += 31 * this.fillGas.hashCode();
		hash += 31 * this.materialBlocks.hashCode();

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
	public void copy(LWRRod otherObject) {
		// begin-user-code

		// If the otherObject is null, return
		if (otherObject == null) {
			return;
		}
		// Copy contents - super
		super.copy(otherObject);

		// Copy local contents - deep copy
		this.clad = (Ring) otherObject.clad.clone();
		this.fillGas = (Material) otherObject.fillGas.clone();
		this.pressure = otherObject.pressure;
		this.materialBlocks.clear();
		for (int i = 0; i < otherObject.materialBlocks.size(); i++) {
			this.materialBlocks
					.add((MaterialBlock) ((MaterialBlock) otherObject.materialBlocks
							.toArray()[i]).clone());
		}

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
		LWRRod rod = new LWRRod();

		// Copy contents
		rod.copy(this);

		// Return newly instantiated object
		return rod;

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
				"pressure", pressure);

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

		// Add the materialBlocks, clad and fillGas to children
		children.add(this.clad);
		children.add(this.fillGas);
		children.addAll(this.materialBlocks);

		return children;
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
			this.clad = (Ring) childComponent;

			// If this is a material
		} else if (childComponent.getHDF5LWRTag() == HDF5LWRTagType.MATERIAL) {

			// Assign to correct object
			this.fillGas = (Material) childComponent;

			// If this is a Material Block
		} else if (childComponent.getHDF5LWRTag() == HDF5LWRTagType.MATERIALBLOCK) {

			// Assign to correct object
			this.materialBlocks.add((MaterialBlock) childComponent);

		}

		return true;
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
		Double pressure = HdfReaderFactory.readDoubleAttribute(h5Group,
				"pressure");

		// Call super
		flag &= super.readAttributes(h5Group);

		// check values
		if (pressure == null || !flag || h5Group == null) {
			return false;
		}
		// If everything is valid, then set data
		this.pressure = pressure;

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