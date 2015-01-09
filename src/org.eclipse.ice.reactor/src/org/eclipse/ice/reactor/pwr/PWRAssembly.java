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

import org.eclipse.ice.io.hdf.HdfReaderFactory;
import org.eclipse.ice.io.hdf.HdfWriterFactory;
import org.eclipse.ice.io.hdf.IHdfReadable;
import org.eclipse.ice.io.hdf.IHdfWriteable;
import org.eclipse.ice.reactor.GridLocation;
import org.eclipse.ice.reactor.HDF5LWRTagType;
import org.eclipse.ice.reactor.LWRComponent;
import org.eclipse.ice.reactor.LWRComposite;
import org.eclipse.ice.reactor.LWRDataProvider;
import org.eclipse.ice.reactor.LWRGridManager;
import org.eclipse.ice.reactor.LWRRod;
import org.eclipse.ice.datastructures.ICEObject.Component;

import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;

import java.util.ArrayList;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * The PWRAssembly class contains a collection of LWRRods mapped to locations on
 * a grid. When the addLWRRod() operation is used, if a LWRRod with the same
 * name exists in the collection, then the LWRRod will not be added and a value
 * of false will be returned. When using the setLWRRodLocation() operation, if a
 * LWRRod with the same name exists at the provided location, then the current
 * LWRRod name at the provided location will be overwritten.
 * </p>
 * <p>
 * </p>
 * <p>
 * StatePointData for LWRRods should be stored by position and accessed by the
 * getLWRRodDataProviderAtLocation operation.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author s4h
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class PWRAssembly extends LWRComposite {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The size of either dimension of this PWRAssembly.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected int size;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A LWRComposite for LWRRods.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected LWRComposite lWRRodComposite;
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected LWRGridManager lWRRodGridManager;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * ﻿The distance between centers of adjacent fuel rods in the fuel grid.
	 * Must be greater than zero.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected double rodPitch;

	// Private attributes for readChild operations.
	private static final String LWRROD_COMPOSITE_NAME = "LWRRods";
	private static final String LWRROD_GRID_MANAGER_NAME = "LWRRod Grid";

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A parameterized Constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param size
	 *            <p>
	 *            The size of either dimension of this PWRAssembly.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public PWRAssembly(int size) {
		// begin-user-code
		super();
		// Setup default values - LWRComponent
		this.name = "PWRAssembly";
		this.description = "PWRAssembly's Description";
		this.id = 1;

		// Setup default values for size and map
		this.size = 1;

		// Set the size if it is greater than 0
		if (size > 0) {
			this.size = size;
		}

		// Setup the LWRComposite and associated values.
		this.lWRRodComposite = new LWRComposite();
		this.lWRRodComposite.setName(this.LWRROD_COMPOSITE_NAME);
		this.lWRRodComposite
				.setDescription("A Composite that contains many LWRRods.");
		this.lWRRodComposite.setId(1);

		// Setup GridManager
		this.lWRRodGridManager = new LWRGridManager(this.size);
		this.lWRRodGridManager.setName(this.LWRROD_GRID_MANAGER_NAME);

		// Add the component to the LWRComposite list
		super.addComponent(lWRRodComposite);

		// Default size for rod pitch
		this.rodPitch = 1.0;

		// Setup the LWRComponentType to the correct type
		this.HDF5LWRTag = HDF5LWRTagType.PWRASSEMBLY;

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
	 *            The name of this PWRAssembly.
	 *            </p>
	 * @param size
	 *            <p>
	 *            The size of either dimension of this PWRAssembly.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public PWRAssembly(String name, int size) {
		// begin-user-code
		// Call shorter constructor
		this(size);

		// Set name
		this.setName(name);

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the size of either dimension of this PWRAssembly.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The size of either dimension of this PWRAssembly.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int getSize() {
		// begin-user-code
		return this.size;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Adds a LWRRod to the collection of LWRRods. If a LWRRod with the same
	 * name exists in the collection or the passed parameter is null, then the
	 * LWRRod will not be added and a value of false will be returned.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param lWRRod
	 *            <p>
	 *            The LWRRod to add to the collection of LWRRods.
	 *            </p>
	 * @return <p>
	 *         True, if the LWRRod was added successfully.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean addLWRRod(LWRRod lWRRod) {
		// begin-user-code
		// Add the component to the composite
		this.lWRRodComposite.addComponent((Component) lWRRod);

		// If the component is not contained, return false
		if (!this.lWRRodComposite.getComponents().contains(lWRRod)) {
			return false;
		}

		// The component was added to the composite, return true!
		return true;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Removes a LWRRod from the collection of LWRRods. The passed string can
	 * not be null.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param lWRRodName
	 *            <p>
	 *            The name of the LWRRod to be removed. Returns true if the
	 *            LWRRod was removed successfully.
	 *            </p>
	 * @return <p>
	 *         True, if the LWRRod was removed successfully.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean removeLWRRod(String lWRRodName) {
		// begin-user-code
		// If the name does not exist, return
		if (this.lWRRodComposite.getComponent(lWRRodName) == null) {
			return false;
		}

		// Remove it from the grid as well
		this.lWRRodGridManager.removeComponent(this.lWRRodComposite
				.getComponent(lWRRodName));

		// Remove the component from the composite with the given name
		this.lWRRodComposite.removeComponent(lWRRodName);

		// Remove it from the grid as well
		this.lWRRodGridManager.removeComponent(this.lWRRodComposite
				.getComponent(lWRRodName));

		// If name does not exist, return true. Else false
		if (this.lWRRodComposite.getComponent(lWRRodName) != null) {
			return false;
		}

		// The component was deleted from the composite, return true!
		return true;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns an ArrayList of names for each element of the collection of
	 * LWRRods.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         An ArrayList of names for each element of the collection of
	 *         LWRRods.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<String> getLWRRodNames() {
		// begin-user-code

		// Return the Component's names
		return this.lWRRodComposite.getComponentNames();
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the LWRRod corresponding to the provided name or null if the name
	 * is not found.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param name
	 *            <p>
	 *            The provided LWRRod's name.
	 *            </p>
	 * @return <p>
	 *         The LWRRod corresponding to the provided name or null if the name
	 *         is not found.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public LWRRod getLWRRodByName(String name) {
		// begin-user-code

		return (LWRRod) this.lWRRodComposite.getComponent(name);
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the LWRRod corresponding to the provided column and row or null
	 * if one is not found at the provided location.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param row
	 *            <p>
	 *            The row position.
	 *            </p>
	 * @param column
	 *            <p>
	 *            The column position.
	 *            </p>
	 * @return <p>
	 *         The LWRRod corresponding to the provided column and row or null
	 *         if one is not found at the provided location.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public LWRRod getLWRRodByLocation(int row, int column) {
		// begin-user-code

		// Local Declarations
		String name = "";

		// Get the name
		name = this.lWRRodGridManager.getComponentName(new GridLocation(row,
				column));

		// Return the component
		return (LWRRod) this.getLWRRodByName(name);

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the number of LWRRods in the collection of LWRRods.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The number of LWRRods in the collection of LWRRods.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int getNumberOfLWRRods() {
		// begin-user-code

		// Return the number of components
		return this.lWRRodComposite.getNumberOfComponents();

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the location for the provided name. Overrides the location of
	 * another component name as required. Returns true if this operation was
	 * successful, false otherwise. Note it will return true if the same name is
	 * overridden.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param lWRRodName
	 *            <p>
	 *            The LWRRod's name.
	 *            </p>
	 * @param row
	 *            <p>
	 *            The row position.
	 *            </p>
	 * @param column
	 *            <p>
	 *            The column position.
	 *            </p>
	 * @return <p>
	 *         True, if the location of the LWRRod was set successfully.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean setLWRRodLocation(String lWRRodName, int row, int column) {
		// begin-user-code
		// Local declarations
		GridLocation location = new GridLocation(row, column);

		// If the rows and columns dont match the location, return false
		if (location.getColumn() != column || location.getRow() != row) {
			return false;
		}

		// Set the location
		this.lWRRodGridManager.addComponent(
				(Component) this.lWRRodComposite.getComponent(lWRRodName),
				location);

		// If the name changed, then return true
		if (this.lWRRodGridManager.getComponentName(location) != null
				&& this.lWRRodGridManager.getComponentName(location).equals(
						lWRRodName)) {
			return true;
		}

		// Otherwise, name did not change
		return false;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Removes the LWRRod at the provided location. Returns true if the removal
	 * was successful.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param row
	 *            <p>
	 *            The row position.
	 *            </p>
	 * @param column
	 *            <p>
	 *            The column position.
	 *            </p>
	 * @return <p>
	 *         True, if the removal was successful.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean removeLWRRodFromLocation(int row, int column) {
		// begin-user-code
		// Local Declarations
		GridLocation location = new GridLocation(row, column);

		// If the rows and columns dont match the location, return false
		if (location.getColumn() != column || location.getRow() != row) {
			return false;
		}

		// Check to make sure that a name exists there.
		if (this.lWRRodGridManager.getComponentName(location) == null) {
			return false;
		}

		// Remove the component from the location
		this.lWRRodGridManager.removeComponent(location);

		// If the composite at that location is null, then return true
		if (this.lWRRodGridManager.getComponentName(location) == null) {
			return true;
		}

		// Nothing was changed, return false
		return false;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the distance between centers of adjacent fuel rods.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The distance between centers of adjacent fuel rods in the fuel
	 *         lattice.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public double getRodPitch() {
		// begin-user-code

		return this.rodPitch;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the distance between centers of adjacent fuel rods in the fuel
	 * lattice. The rodPitch value must be greater than zero.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param rodPitch
	 *            <p>
	 *            The distance between centers of adjacent fuel rods in the fuel
	 *            lattice.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setRodPitch(double rodPitch) {
		// begin-user-code

		// If the rod pitch is less than zero, do not set to new value.
		if (rodPitch > 0) {
			this.rodPitch = rodPitch;
		}

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation overrides the LWRComposite's operation. This operation
	 * does nothing and requires that the appropriate, more defined, associated
	 * operation to be utilized on this class.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param component
	 *            <p>
	 *            The component to be added.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void addComponent(Component component) {
		// begin-user-code

		// Does nothing
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * An operation that overrides the LWRComposite's operation. This operation
	 * does nothing and requires that the appropriate, more defined, associated
	 * operation to be utilized on this class.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param childId
	 *            <p>
	 *            The id of the LWRComponent to remove.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void removeComponent(int childId) {
		// begin-user-code

		// Does nothing

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * An operation that overrides the LWRComposite's operation. This operation
	 * does nothing and requires that the appropriate, more defined, associated
	 * operation to be utilized on this class.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param name
	 *            <p>
	 *            The name of the LWRComponent to remove.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void removeComponent(String name) {
		// begin-user-code

		// Does nothing

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
		PWRAssembly assembly;
		boolean retVal = false;

		// If they are equal to the same place on the heap, return true
		if (otherObject == this) {
			return true;
		}

		// If the otherObject is null or not an instanceof this object, return
		// false
		if (otherObject != null && otherObject instanceof PWRAssembly) {

			// Cast it
			assembly = (PWRAssembly) otherObject;

			// Compare values
			retVal = super.equals(otherObject)
					&& this.lWRRodComposite.equals(assembly.lWRRodComposite)
					&& this.lWRRodGridManager
							.equals(assembly.lWRRodGridManager)
					&& this.size == assembly.size
					&& this.rodPitch == assembly.rodPitch;

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

		// Add hashes of local attributes
		hash += 31 * this.rodPitch;
		hash += 31 * this.size;
		hash += 31 * this.lWRRodComposite.hashCode();
		hash += 31 * this.lWRRodGridManager.hashCode();

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
	public void copy(PWRAssembly otherObject) {
		// begin-user-code

		// If the otherObject is null, return
		if (otherObject == null) {
			return;
		}

		// Copy contents - super
		super.copy(otherObject);

		// Copy local contents
		this.rodPitch = otherObject.rodPitch;
		this.size = otherObject.size;
		this.lWRRodComposite = (LWRComposite) otherObject.lWRRodComposite
				.clone();
		this.lWRRodGridManager = (LWRGridManager) otherObject.lWRRodGridManager
				.clone();

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
		PWRAssembly assembly = new PWRAssembly(0);

		// Copy contents
		assembly.copy(this);

		// Return newly instantiated object
		return assembly;

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
		flag &= HdfWriterFactory.writeIntegerAttribute(h5File, h5Group, "size",
				this.size);
		flag &= HdfWriterFactory.writeDoubleAttribute(h5File, h5Group,
				"rodPitch", rodPitch);

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

		// Add children
		children.add(this.lWRRodGridManager);

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
		Integer size = HdfReaderFactory.readIntegerAttribute(h5Group, "size");
		Double rodPitch = HdfReaderFactory.readDoubleAttribute(h5Group,
				"rodPitch");

		// Call super
		flag &= super.readAttributes(h5Group);

		// check values
		if (size == null || rodPitch == null || !flag || h5Group == null) {
			return false;
		}

		// If everything is valid, then set data
		this.size = size.intValue();
		this.rodPitch = rodPitch.doubleValue();

		// Reset the sizes on the grids
		this.lWRRodGridManager = new LWRGridManager(size.intValue());
		this.lWRRodGridManager.setName(this.LWRROD_GRID_MANAGER_NAME);

		return true;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the data provider for specific group at location or null if it
	 * does not exist.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param row
	 *            <p>
	 *            the row
	 *            </p>
	 * @param column
	 *            <p>
	 *            The column
	 *            </p>
	 * @return <p>
	 *         the provider
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public LWRDataProvider getLWRRodDataProviderAtLocation(int row, int column) {
		// begin-user-code

		return this.lWRRodGridManager
				.getDataProviderAtLocation(new GridLocation(row, column));

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

		// If this is a LWRGridManager
		if (childComponent.getHDF5LWRTag() == HDF5LWRTagType.LWRGRIDMANAGER) {

			// Cast into a LWRGridManager object
			LWRGridManager lWRGridManager = (LWRGridManager) childComponent;

			// Assign to the correct object
			if (lWRGridManager.getName().equals(this.LWRROD_GRID_MANAGER_NAME)) {
				this.lWRRodGridManager = (LWRGridManager) childComponent;

			}

			// If this is an LWRComposite
		} else if (childComponent.getHDF5LWRTag() == HDF5LWRTagType.LWRCOMPOSITE) {

			// Cast into a LWRComposite object
			LWRComposite lWRComposite = (LWRComposite) childComponent;
			// Assign to the correct object
			if (lWRComposite.getName().equals(this.LWRROD_COMPOSITE_NAME)) {
				super.removeComponent(this.LWRROD_COMPOSITE_NAME);
				this.lWRRodComposite = lWRComposite;
				super.addComponent(lWRRodComposite);

			}

		}

		return true;
		// end-user-code
	}

}