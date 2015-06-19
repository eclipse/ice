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

import java.util.ArrayList;

import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;

import org.eclipse.ice.datastructures.ICEObject.Component;
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

/**
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
 * 
 * @author Scott Forest Hull II
 */
public class PWRAssembly extends LWRComposite {
	/**
	 * <p>
	 * The size of either dimension of this PWRAssembly.
	 * </p>
	 * 
	 */
	protected int size;
	/**
	 * <p>
	 * A LWRComposite for LWRRods.
	 * </p>
	 * 
	 */
	protected LWRComposite lWRRodComposite;
	/**
	 * 
	 */
	protected LWRGridManager lWRRodGridManager;
	/**
	 * <p>
	 * ﻿The distance between centers of adjacent fuel rods in the fuel grid.
	 * Must be greater than zero.
	 * </p>
	 * 
	 */
	protected double rodPitch;

	// Private attributes for readChild operations.
	private static final String LWRROD_COMPOSITE_NAME = "LWRRods";
	private static final String LWRROD_GRID_MANAGER_NAME = "LWRRod Grid";

	/**
	 * <p>
	 * A parameterized Constructor.
	 * </p>
	 * 
	 * @param size
	 *            <p>
	 *            The size of either dimension of this PWRAssembly.
	 *            </p>
	 */
	public PWRAssembly(int size) {
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

	}

	/**
	 * <p>
	 * A parameterized Constructor.
	 * </p>
	 * 
	 * @param name
	 *            <p>
	 *            The name of this PWRAssembly.
	 *            </p>
	 * @param size
	 *            <p>
	 *            The size of either dimension of this PWRAssembly.
	 *            </p>
	 */
	public PWRAssembly(String name, int size) {
		// Call shorter constructor
		this(size);

		// Set name
		this.setName(name);

	}

	/**
	 * <p>
	 * Returns the size of either dimension of this PWRAssembly.
	 * </p>
	 * 
	 * @return <p>
	 *         The size of either dimension of this PWRAssembly.
	 *         </p>
	 */
	public int getSize() {
		return this.size;

	}

	/**
	 * <p>
	 * Adds a LWRRod to the collection of LWRRods. If a LWRRod with the same
	 * name exists in the collection or the passed parameter is null, then the
	 * LWRRod will not be added and a value of false will be returned.
	 * </p>
	 * 
	 * @param lWRRod
	 *            <p>
	 *            The LWRRod to add to the collection of LWRRods.
	 *            </p>
	 * @return <p>
	 *         True, if the LWRRod was added successfully.
	 *         </p>
	 */
	public boolean addLWRRod(LWRRod lWRRod) {
		// Add the component to the composite
		this.lWRRodComposite.addComponent((Component) lWRRod);

		// If the component is not contained, return false
		if (!this.lWRRodComposite.getComponents().contains(lWRRod)) {
			return false;
		}

		// The component was added to the composite, return true!
		return true;

	}

	/**
	 * <p>
	 * Removes a LWRRod from the collection of LWRRods. The passed string can
	 * not be null.
	 * </p>
	 * 
	 * @param lWRRodName
	 *            <p>
	 *            The name of the LWRRod to be removed. Returns true if the
	 *            LWRRod was removed successfully.
	 *            </p>
	 * @return <p>
	 *         True, if the LWRRod was removed successfully.
	 *         </p>
	 */
	public boolean removeLWRRod(String lWRRodName) {
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
	}

	/**
	 * <p>
	 * Returns an ArrayList of names for each element of the collection of
	 * LWRRods.
	 * </p>
	 * 
	 * @return <p>
	 *         An ArrayList of names for each element of the collection of
	 *         LWRRods.
	 *         </p>
	 */
	public ArrayList<String> getLWRRodNames() {

		// Return the Component's names
		return this.lWRRodComposite.getComponentNames();
	}

	/**
	 * <p>
	 * Returns the LWRRod corresponding to the provided name or null if the name
	 * is not found.
	 * </p>
	 * 
	 * @param name
	 *            <p>
	 *            The provided LWRRod's name.
	 *            </p>
	 * @return <p>
	 *         The LWRRod corresponding to the provided name or null if the name
	 *         is not found.
	 *         </p>
	 */
	public LWRRod getLWRRodByName(String name) {

		return (LWRRod) this.lWRRodComposite.getComponent(name);
	}

	/**
	 * <p>
	 * Returns the LWRRod corresponding to the provided column and row or null
	 * if one is not found at the provided location.
	 * </p>
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
	 */
	public LWRRod getLWRRodByLocation(int row, int column) {

		// Local Declarations
		String name = "";

		// Get the name
		name = this.lWRRodGridManager.getComponentName(new GridLocation(row,
				column));

		// Return the component
		return (LWRRod) this.getLWRRodByName(name);

	}

	/**
	 * <p>
	 * Returns the number of LWRRods in the collection of LWRRods.
	 * </p>
	 * 
	 * @return <p>
	 *         The number of LWRRods in the collection of LWRRods.
	 *         </p>
	 */
	public int getNumberOfLWRRods() {

		// Return the number of components
		return this.lWRRodComposite.getNumberOfComponents();

	}

	/**
	 * <p>
	 * Sets the location for the provided name. Overrides the location of
	 * another component name as required. Returns true if this operation was
	 * successful, false otherwise. Note it will return true if the same name is
	 * overridden.
	 * </p>
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
	 */
	public boolean setLWRRodLocation(String lWRRodName, int row, int column) {
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

	}

	/**
	 * <p>
	 * Removes the LWRRod at the provided location. Returns true if the removal
	 * was successful.
	 * </p>
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
	 */
	public boolean removeLWRRodFromLocation(int row, int column) {
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

	}

	/**
	 * <p>
	 * Returns the distance between centers of adjacent fuel rods.
	 * </p>
	 * 
	 * @return <p>
	 *         The distance between centers of adjacent fuel rods in the fuel
	 *         lattice.
	 *         </p>
	 */
	public double getRodPitch() {

		return this.rodPitch;

	}

	/**
	 * <p>
	 * Sets the distance between centers of adjacent fuel rods in the fuel
	 * lattice. The rodPitch value must be greater than zero.
	 * </p>
	 * 
	 * @param rodPitch
	 *            <p>
	 *            The distance between centers of adjacent fuel rods in the fuel
	 *            lattice.
	 *            </p>
	 */
	public void setRodPitch(double rodPitch) {

		// If the rod pitch is less than zero, do not set to new value.
		if (rodPitch > 0) {
			this.rodPitch = rodPitch;
		}

	}

	/**
	 * <p>
	 * This operation overrides the LWRComposite's operation. This operation
	 * does nothing and requires that the appropriate, more defined, associated
	 * operation to be utilized on this class.
	 * </p>
	 * 
	 * @param component
	 *            <p>
	 *            The component to be added.
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
	 *            The id of the LWRComponent to remove.
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
	 *            The name of the LWRComponent to remove.
	 *            </p>
	 */
	@Override
	public void removeComponent(String name) {

		// Does nothing

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

		// Add hashes of local attributes
		hash += 31 * this.rodPitch;
		hash += 31 * this.size;
		hash += 31 * this.lWRRodComposite.hashCode();
		hash += 31 * this.lWRRodGridManager.hashCode();

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
	public void copy(PWRAssembly otherObject) {

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
		PWRAssembly assembly = new PWRAssembly(0);

		// Copy contents
		assembly.copy(this);

		// Return newly instantiated object
		return assembly;

	}

	/**
	 * 
	 * @param h5File
	 * @param h5Group
	 * @return
	 */
	@Override
	public boolean writeAttributes(H5File h5File, H5Group h5Group) {
		boolean flag = true;

		flag &= super.writeAttributes(h5File, h5Group);
		flag &= HdfWriterFactory.writeIntegerAttribute(h5File, h5Group, "size",
				this.size);
		flag &= HdfWriterFactory.writeDoubleAttribute(h5File, h5Group,
				"rodPitch", rodPitch);

		return flag;
	}

	/**
	 * 
	 * @return
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

		// Add children
		children.add(this.lWRRodGridManager);

		return children;
	}

	/**
	 * 
	 * @param h5Group
	 * @return
	 */
	@Override
	public boolean readAttributes(H5Group h5Group) {

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

	}

	/**
	 * <p>
	 * Returns the data provider for specific group at location or null if it
	 * does not exist.
	 * </p>
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
	 */
	public LWRDataProvider getLWRRodDataProviderAtLocation(int row, int column) {

		return this.lWRRodGridManager
				.getDataProviderAtLocation(new GridLocation(row, column));

	}

	/**
	 * <p>
	 * This operation returns an ArrayList of IHdfReadable child objects. If
	 * this IHdfReadable has no IHdfReadable child objects, then null is
	 * returned.
	 * </p>
	 * 
	 * @param iHdfReadable
	 * @return
	 */
	@Override
	public boolean readChild(IHdfReadable iHdfReadable) {

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
	}

}