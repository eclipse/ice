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

import ncsa.hdf.object.h5.H5Group;

import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.io.hdf.IHdfReadable;
import org.eclipse.ice.io.hdf.IHdfWriteable;
import org.eclipse.ice.reactor.GridLabelProvider;
import org.eclipse.ice.reactor.GridLocation;
import org.eclipse.ice.reactor.HDF5LWRTagType;
import org.eclipse.ice.reactor.ILWRComponentVisitor;
import org.eclipse.ice.reactor.LWRComponent;
import org.eclipse.ice.reactor.LWRComposite;
import org.eclipse.ice.reactor.LWRDataProvider;
import org.eclipse.ice.reactor.LWRGridManager;
import org.eclipse.ice.reactor.Tube;

/**
 * <p>
 * The FuelAssembly class is a PWRAssembly populated with a collection of Tubes
 * positioned on a fixed grid. When the addTube() operation is used, if a Tube
 * with the same name exists in the collection, then the Tube will not be added
 * and a value of false will be returned. When using the setTubeLocation()
 * operation, if a Tube with the same name exists at the provided location, then
 * the current Tube name at the provided location will be overwritten.
 * </p>
 * <p>
 * StatePointData for Tubes should be stored by position and accessed by the
 * getTubeDataProviderAtLocation operation.
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public class FuelAssembly extends PWRAssembly {
	/**
	 * <p>
	 * The RodClusterAssembly associated with this FuelAssembly.
	 * </p>
	 * 
	 */
	private RodClusterAssembly rodClusterAssembly;
	/**
	 * <p>
	 * A LWRComposite for Tubes.
	 * </p>
	 * 
	 */
	private LWRComposite tubeComposite;
	/**
	 * 
	 */
	private LWRGridManager tubeGridManager;
	/**
	 * <p>
	 * The GridLabelProvider for this FuelAssembly.
	 * </p>
	 * 
	 */
	private GridLabelProvider gridLabelProvider;

	public static final String TUBE_COMPOSITE_NAME = "Tubes";
	public static final String TUBE_GRID_MANAGER_NAME = "Tube Grid";
	public static final String GRID_LABEL_PROVIDER_NAME = "Grid Labels";

	/**
	 * <p>
	 * A parameterized Constructor.
	 * </p>
	 * 
	 * @param size
	 *            <p>
	 *            The size of either dimension of the location grid.
	 *            </p>
	 */
	public FuelAssembly(int size) {

		// Call super constructor
		super(size);

		// Set defaults for FuelAssembly
		this.name = "FuelAssembly";
		this.description = "FuelAssembly's Description";
		this.id = 1;

		// Setup rows and cols
		this.gridLabelProvider = new GridLabelProvider(this.size);
		this.gridLabelProvider.setName(FuelAssembly.GRID_LABEL_PROVIDER_NAME);

		// Setup tube map
		this.tubeComposite = new LWRComposite();
		this.tubeComposite.setName(FuelAssembly.TUBE_COMPOSITE_NAME);
		this.tubeComposite
				.setDescription("A Composite that contains many Tubes.");
		this.tubeComposite.setId(2);

		// Setup tube grid manager
		this.tubeGridManager = new LWRGridManager(this.size);
		this.tubeGridManager.setName(FuelAssembly.TUBE_GRID_MANAGER_NAME);

		// Add tubeComposite to components

		// this has to be added manually
		this.lWRComponents
				.put(this.tubeComposite.getName(), this.tubeComposite);

		// Setup the LWRComponentType to correct type
		this.HDF5LWRTag = HDF5LWRTagType.FUEL_ASSEMBLY;

	}

	/**
	 * <p>
	 * A parameterized Constructor.
	 * </p>
	 * 
	 * @param name
	 *            <p>
	 *            The name of this FuelAssembly.
	 *            </p>
	 * @param size
	 *            <p>
	 *            The size of either dimension of the location grid.
	 *            </p>
	 */
	public FuelAssembly(String name, int size) {

		// Call this constructor
		this(size);

		// Setup name
		this.setName(name);
	}

	/**
	 * <p>
	 * Returns the RodClusterAssembly associated with this FuelAssembly or null
	 * if one has not been set.
	 * </p>
	 * 
	 * @return <p>
	 *         The RodClusterAssembly associated with this FuelAssembly or null
	 *         if one has not been set.
	 *         </p>
	 */
	public RodClusterAssembly getRodClusterAssembly() {
		return this.rodClusterAssembly;
	}

	/**
	 * <p>
	 * Sets the RodClusterAssembly associated with this FuelAssembly.
	 * </p>
	 * 
	 * @param rodClusterAssembly
	 *            <p>
	 *            The RodClusterAssembly associated with this FuelAssembly.
	 *            </p>
	 */
	public void setRodClusterAssembly(RodClusterAssembly rodClusterAssembly) {

		// RodClusterAssembly can be null
		this.rodClusterAssembly = rodClusterAssembly;

	}

	/**
	 * <p>
	 * Adds a Tube to the collection of Tubes. If a Tube with the same name
	 * exists in the collection or if the passed tube is null, then the Tube
	 * will not be added and a value of false will be returned.
	 * </p>
	 * 
	 * @param tube
	 *            <p>
	 *            The Tube to add to the collection of Tubes.
	 *            </p>
	 * @return <p>
	 *         True, if the Tube was added successfully.
	 *         </p>
	 */
	public boolean addTube(Tube tube) {

		// Add the component to the composite
		this.tubeComposite.addComponent(tube);

		// If the component is not contained, return false
		if (!this.tubeComposite.getComponents().contains(tube)) {
			return false;
		}

		// The component was added to the composite, return true!
		return true;
	}

	/**
	 * <p>
	 * Removes a Tube from the collection of Tubes. Returns false if the
	 * tubeName does not exist or if the string passed is null.
	 * </p>
	 * 
	 * @param tubeName
	 *            <p>
	 *            The name of the Tube to be removed.
	 *            </p>
	 * @return <p>
	 *         True, if the Tube was removed successfully.
	 *         </p>
	 */
	public boolean removeTube(String tubeName) {

		// If the name does not exist, return
		if (this.tubeComposite.getComponent(tubeName) == null) {
			return false;
		}

		// Remove it from the grid as well
		this.tubeGridManager.removeComponent(this.tubeComposite
				.getComponent(tubeName));

		// Remove the component from the composite with the given name
		this.tubeComposite.removeComponent(tubeName);

		// Remove it from the grid as well
		this.tubeGridManager.removeComponent(this.tubeComposite
				.getComponent(tubeName));

		// If name does not exist, return true. Else false
		if (this.tubeComposite.getComponent(tubeName) != null) {
			return false;
		}

		// The component was deleted from the composite, return true!
		return true;

	}

	/**
	 * <p>
	 * Removes the Tube at the provided location. Returns true if the removal
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
	 *         True, if the Tube removal was successful.
	 *         </p>
	 */
	public boolean removeTubeFromLocation(int row, int column) {
		// Local Declarations
		GridLocation location = new GridLocation(row, column);

		// If the rows and columns dont match the location, return false
		if (location.getColumn() != column || location.getRow() != row) {
			return false;
		}
		// Check to make sure that a name exists there.
		if (this.tubeGridManager.getComponentName(location) == null) {
			return false;
		}

		// Remove the component from the location
		this.tubeGridManager.removeComponent(location);

		// If the composite at that location is null, then return true
		if (this.tubeGridManager.getComponentName(location) == null) {
			return true;
		}

		// Nothing was changed, return false
		return false;

	}

	/**
	 * <p>
	 * Returns an ArrayList of names for each element of the collection of
	 * Tubes.
	 * </p>
	 * 
	 * @return <p>
	 *         An ArrayList of names for each element of the collection of
	 *         Tubes.
	 *         </p>
	 */
	public ArrayList<String> getTubeNames() {
		// Return the Component's names
		return this.tubeComposite.getComponentNames();
	}

	/**
	 * <p>
	 * Returns the Tube corresponding to the provided name or null if the name
	 * is not found.
	 * </p>
	 * 
	 * @param name
	 *            <p>
	 *            The provided Tube's name.
	 *            </p>
	 * @return <p>
	 *         The Tube corresponding to the provided name or null if the name
	 *         is not found.
	 *         </p>
	 */
	public Tube getTubeByName(String name) {
		return (Tube) this.tubeComposite.getComponent(name);
	}

	/**
	 * <p>
	 * Returns the Tube corresponding to the provided column and row or null if
	 * one is not found at the provided location.
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
	 *         The Tube corresponding to the provided column and row or null if
	 *         one is not found at the provided location.
	 *         </p>
	 */
	public Tube getTubeByLocation(int row, int column) {
		String name = "";

		// Get the component name
		name = this.tubeGridManager.getComponentName(new GridLocation(row,
				column));

		// Return the component by name
		return this.getTubeByName(name);
	}

	/**
	 * <p>
	 * Returns the number of Tubes in the collection of Tubes.
	 * </p>
	 * 
	 * @return <p>
	 *         The number of Tubes.
	 *         </p>
	 */
	public int getNumberOfTubes() {
		// Return the number of components
		return this.tubeComposite.getNumberOfComponents();
	}

	/**
	 * <p>
	 * Sets the location for the provided name. Overrides the location of
	 * another component name as required. Returns true if this operation was
	 * successful, false otherwise. Note it will return true if the same name is
	 * overridden.
	 * </p>
	 * 
	 * @param tubeName
	 *            <p>
	 *            The Tube's name.
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
	public boolean setTubeLocation(String tubeName, int row, int column) {

		// Local declarations
		GridLocation location = new GridLocation(row, column);

		// If the rows and columns dont match the location, return false
		if (location.getColumn() != column || location.getRow() != row) {
			return false;
		}

		// Set the location
		this.tubeGridManager
				.addComponent(
						this.tubeComposite.getComponent(tubeName),
						location);

		// If the name changed, then return true
		if (this.tubeGridManager.getComponentName(location) != null
				&& this.tubeGridManager.getComponentName(location).equals(
						tubeName)) {
			return true;
		}

		// Otherwise, name did not change
		return false;

	}

	/**
	 * <p>
	 * Returns the GridLabelProvider for this FuelAssembly.
	 * </p>
	 * 
	 * @return <p>
	 *         The GridLabelProvider for this FuelAssembly.
	 *         </p>
	 */
	public GridLabelProvider getGridLabelProvider() {

		return this.gridLabelProvider;
	}

	/**
	 * <p>
	 * Sets the GridLabelProvider for this FuelAssembly. Can not be set to null.
	 * </p>
	 * 
	 * @param gridLabelProvider
	 *            <p>
	 *            The GridLabelProvider for this FuelAssembly.
	 *            </p>
	 */
	public void setGridLabelProvider(GridLabelProvider gridLabelProvider) {

		// If the GridLabelProvider passed is not null and of the same size.
		if (gridLabelProvider != null
				&& gridLabelProvider.getSize() == this.size) {
			this.gridLabelProvider = gridLabelProvider;
		}

	}

	/*
	 * Overrides a method from PWRAssembly.
	 */
	@Override
	public boolean equals(Object otherObject) {

		// Local Declarations
		FuelAssembly assembly;
		boolean retVal = false;

		// If they are equal to the same place on the heap, return true
		if (otherObject == this) {
			return true;
		}

		// If the otherObject is null or not an instanceof this object, return
		// false
		if (otherObject != null && otherObject instanceof FuelAssembly) {

			// Cast it
			assembly = (FuelAssembly) otherObject;

			// Compare values
			retVal = super.equals(otherObject)
					&& this.gridLabelProvider
							.equals(assembly.gridLabelProvider)
					&& this.tubeComposite.equals(assembly.tubeComposite)
					&& this.tubeGridManager.equals(assembly.tubeGridManager);

			// RodClusterAssembly can be null:
			if (this.rodClusterAssembly == null) {
				retVal = retVal
						&& this.rodClusterAssembly == assembly.rodClusterAssembly;
			} else {
				retVal = retVal
						&& this.rodClusterAssembly
								.equals(assembly.rodClusterAssembly);
			}

		}

		// Return retVal
		return retVal;
	}

	/*
	 * Overrides a method from PWRAssembly.
	 */
	@Override
	public int hashCode() {
		int hash = super.hashCode();

		// Hash local values
		hash += 31 * this.gridLabelProvider.hashCode();
		hash += 31 * (null == this.rodClusterAssembly ? 1
				: this.rodClusterAssembly.hashCode());
		hash += 31 * this.tubeComposite.hashCode();
		hash += 31 * this.tubeGridManager.hashCode();

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
	public void copy(FuelAssembly otherObject) {

		// If otherObject is null, return
		if (otherObject == null) {
			return;
		}

		// Copy contents - super
		super.copy(otherObject);

		// Copy local values
		this.gridLabelProvider = (GridLabelProvider) otherObject.gridLabelProvider
				.clone();
		// If the other object is null, set to null. Else, set the other object
		// to the clone
		this.rodClusterAssembly = (null == otherObject.rodClusterAssembly ? null
				: (RodClusterAssembly) otherObject.rodClusterAssembly.clone());
		this.tubeComposite = (LWRComposite) otherObject.tubeComposite.clone();
		this.tubeGridManager = (LWRGridManager) otherObject.tubeGridManager
				.clone();

	}

	/*
	 * Overrides a method from PWRAssembly.
	 */
	@Override
	public Object clone() {

		// Local Declarations
		FuelAssembly assembly = new FuelAssembly(0);

		// Copy contents
		assembly.copy(this);

		// Return newly instantiated object
		return assembly;

	}

	/*
	 * Overrides a method from PWRAssembly.
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
		children.add(this.gridLabelProvider);
		children.add(this.tubeGridManager);

		return children;
	}

	/*
	 * Overrides a method from PWRAssembly.
	 */
	@Override
	public boolean readChild(IHdfReadable iHdfReadable) {

		// If the child is null or not an instance of LWRComponent, then return
		// false.
		if (iHdfReadable == null || !(iHdfReadable instanceof LWRComponent)) {
			return false;
		}

		// Call readChild on PWRAssembly
		super.readChild(iHdfReadable);

		// Cast the child into a LWRComponent
		LWRComponent childComponent = (LWRComponent) iHdfReadable;

		// If this is a GridLabelProvider
		if (childComponent.getHDF5LWRTag() == HDF5LWRTagType.GRID_LABEL_PROVIDER
				&& childComponent.getName().equals(GRID_LABEL_PROVIDER_NAME)) {

			// Assign to correct object
			this.gridLabelProvider = (GridLabelProvider) childComponent;

			// If this is a LwrGridManager
		} else if (childComponent.getHDF5LWRTag() == HDF5LWRTagType.LWRGRIDMANAGER) {

			// Cast into a LWRGridManager object
			LWRGridManager lWRGridManager = (LWRGridManager) childComponent;

			// Assign to correct object
			if (lWRGridManager.getName().equals(FuelAssembly.TUBE_GRID_MANAGER_NAME)) {

				this.tubeGridManager = (LWRGridManager) childComponent;

			}

			// If this is an LWRComposite
		} else if (childComponent.getHDF5LWRTag() == HDF5LWRTagType.LWRCOMPOSITE) {

			// Cast into a LWRComposite object
			LWRComposite lWRComposite = (LWRComposite) childComponent;

			// Assign to correct object
			if (lWRComposite.getName().equals(FuelAssembly.TUBE_COMPOSITE_NAME)) {

				// Remove the tube from the composite and add it back
				this.lWRComponents.remove(FuelAssembly.TUBE_COMPOSITE_NAME);
				this.tubeComposite = lWRComposite;
				this.lWRComponents.put(tubeComposite.getName(), tubeComposite);

			}

			// If it is a rod cluster assembly
		} else if (childComponent.getHDF5LWRTag() == HDF5LWRTagType.ROD_CLUSTER_ASSEMBLY) {

			this.rodClusterAssembly = (RodClusterAssembly) childComponent;

		}

		return true;
	}

	/*
	 * Overrides a method from PWRAssembly.
	 */
	@Override
	public boolean readAttributes(H5Group h5Group) {

		boolean flag = super.readAttributes(h5Group);

		if (flag) {
			this.gridLabelProvider = new GridLabelProvider(this.size);
			this.gridLabelProvider.setName(GRID_LABEL_PROVIDER_NAME);
			this.tubeGridManager = new LWRGridManager(this.size);
			this.tubeGridManager.setName(TUBE_GRID_MANAGER_NAME);
		}

		return flag;

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
	public LWRDataProvider getTubeDataProviderAtLocation(int row, int column) {

		// Pass through
		return this.tubeGridManager.getDataProviderAtLocation(new GridLocation(
				row, column));

	}

	/*
	 * Overrides a method from LWRComponent.
	 */
	@Override
	public void accept(ILWRComponentVisitor visitor) {
		visitor.visit(this);
	}
}