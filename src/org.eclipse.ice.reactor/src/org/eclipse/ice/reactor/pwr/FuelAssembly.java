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

import org.eclipse.ice.io.hdf.IHdfReadable;

import ncsa.hdf.object.HObject;
import ncsa.hdf.object.h5.H5Group;

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
import org.eclipse.ice.datastructures.updateableComposite.Component;

import java.util.ArrayList;

/**
 * <!-- begin-UML-doc -->
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
 * <!-- end-UML-doc -->
 * 
 * @author s4h
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class FuelAssembly extends PWRAssembly {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The RodClusterAssembly associated with this FuelAssembly.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private RodClusterAssembly rodClusterAssembly;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A LWRComposite for Tubes.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private LWRComposite tubeComposite;
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private LWRGridManager tubeGridManager;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The GridLabelProvider for this FuelAssembly.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private GridLabelProvider gridLabelProvider;

	private static final String TUBE_COMPOSITE_NAME = "Tubes";
	private static final String TUBE_GRID_MANAGER_NAME = "Tube Grid";
	private static final String GRID_LABEL_PROVIDER_NAME = "Grid Labels";

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A parameterized Constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param size
	 *            <p>
	 *            The size of either dimension of the location grid.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public FuelAssembly(int size) {
		// begin-user-code

		// Call super constructor
		super(size);

		// Set defaults for FuelAssembly
		this.name = "FuelAssembly";
		this.description = "FuelAssembly's Description";
		this.id = 1;

		// Setup rows and cols
		this.gridLabelProvider = new GridLabelProvider(this.size);
		this.gridLabelProvider.setName(this.GRID_LABEL_PROVIDER_NAME);

		// Setup tube map
		this.tubeComposite = new LWRComposite();
		this.tubeComposite.setName(this.TUBE_COMPOSITE_NAME);
		this.tubeComposite
				.setDescription("A Composite that contains many Tubes.");
		this.tubeComposite.setId(2);

		// Setup tube grid manager
		this.tubeGridManager = new LWRGridManager(this.size);
		this.tubeGridManager.setName(this.TUBE_GRID_MANAGER_NAME);

		// Add tubeComposite to components

		// this has to be added manually
		this.lWRComponents
				.put(this.tubeComposite.getName(), this.tubeComposite);

		// Setup the LWRComponentType to correct type
		this.HDF5LWRTag = HDF5LWRTagType.FUEL_ASSEMBLY;

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
	 *            The name of this FuelAssembly.
	 *            </p>
	 * @param size
	 *            <p>
	 *            The size of either dimension of the location grid.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public FuelAssembly(String name, int size) {
		// begin-user-code

		// Call this constructor
		this(size);

		// Setup name
		this.setName(name);
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the RodClusterAssembly associated with this FuelAssembly or null
	 * if one has not been set.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The RodClusterAssembly associated with this FuelAssembly or null
	 *         if one has not been set.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public RodClusterAssembly getRodClusterAssembly() {
		// begin-user-code
		return this.rodClusterAssembly;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the RodClusterAssembly associated with this FuelAssembly.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param rodClusterAssembly
	 *            <p>
	 *            The RodClusterAssembly associated with this FuelAssembly.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setRodClusterAssembly(RodClusterAssembly rodClusterAssembly) {
		// begin-user-code

		// RodClusterAssembly can be null
		this.rodClusterAssembly = rodClusterAssembly;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Adds a Tube to the collection of Tubes. If a Tube with the same name
	 * exists in the collection or if the passed tube is null, then the Tube
	 * will not be added and a value of false will be returned.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param tube
	 *            <p>
	 *            The Tube to add to the collection of Tubes.
	 *            </p>
	 * @return <p>
	 *         True, if the Tube was added successfully.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean addTube(Tube tube) {
		// begin-user-code

		// Add the component to the composite
		this.tubeComposite.addComponent((Component) tube);

		// If the component is not contained, return false
		if (!this.tubeComposite.getComponents().contains(tube)) {
			return false;
		}

		// The component was added to the composite, return true!
		return true;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Removes a Tube from the collection of Tubes. Returns false if the
	 * tubeName does not exist or if the string passed is null.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param tubeName
	 *            <p>
	 *            The name of the Tube to be removed.
	 *            </p>
	 * @return <p>
	 *         True, if the Tube was removed successfully.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean removeTube(String tubeName) {
		// begin-user-code

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

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Removes the Tube at the provided location. Returns true if the removal
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
	 *         True, if the Tube removal was successful.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean removeTubeFromLocation(int row, int column) {
		// begin-user-code
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

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns an ArrayList of names for each element of the collection of
	 * Tubes.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         An ArrayList of names for each element of the collection of
	 *         Tubes.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<String> getTubeNames() {
		// begin-user-code
		// Return the Component's names
		return this.tubeComposite.getComponentNames();
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the Tube corresponding to the provided name or null if the name
	 * is not found.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param name
	 *            <p>
	 *            The provided Tube's name.
	 *            </p>
	 * @return <p>
	 *         The Tube corresponding to the provided name or null if the name
	 *         is not found.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Tube getTubeByName(String name) {
		// begin-user-code
		return (Tube) this.tubeComposite.getComponent(name);
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the Tube corresponding to the provided column and row or null if
	 * one is not found at the provided location.
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
	 *         The Tube corresponding to the provided column and row or null if
	 *         one is not found at the provided location.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Tube getTubeByLocation(int row, int column) {
		// begin-user-code
		String name = "";

		// Get the component name
		name = this.tubeGridManager.getComponentName(new GridLocation(row,
				column));

		// Return the component by name
		return (Tube) this.getTubeByName(name);
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the number of Tubes in the collection of Tubes.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The number of Tubes.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int getNumberOfTubes() {
		// begin-user-code
		// Return the number of components
		return this.tubeComposite.getNumberOfComponents();
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
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean setTubeLocation(String tubeName, int row, int column) {
		// begin-user-code

		// Local declarations
		GridLocation location = new GridLocation(row, column);

		// If the rows and columns dont match the location, return false
		if (location.getColumn() != column || location.getRow() != row) {
			return false;
		}

		// Set the location
		this.tubeGridManager
				.addComponent(
						(Component) this.tubeComposite.getComponent(tubeName),
						location);

		// If the name changed, then return true
		if (this.tubeGridManager.getComponentName(location) != null
				&& this.tubeGridManager.getComponentName(location).equals(
						tubeName)) {
			return true;
		}

		// Otherwise, name did not change
		return false;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the GridLabelProvider for this FuelAssembly.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The GridLabelProvider for this FuelAssembly.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public GridLabelProvider getGridLabelProvider() {
		// begin-user-code

		return this.gridLabelProvider;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the GridLabelProvider for this FuelAssembly. Can not be set to null.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param gridLabelProvider
	 *            <p>
	 *            The GridLabelProvider for this FuelAssembly.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setGridLabelProvider(GridLabelProvider gridLabelProvider) {
		// begin-user-code

		// If the GridLabelProvider passed is not null and of the same size.
		if (gridLabelProvider != null
				&& gridLabelProvider.getSize() == this.size) {
			this.gridLabelProvider = gridLabelProvider;
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
		int hash = super.hashCode();

		// Hash local values
		hash += 31 * this.gridLabelProvider.hashCode();
		hash += 31 * (null == this.rodClusterAssembly ? 1
				: this.rodClusterAssembly.hashCode());
		hash += 31 * this.tubeComposite.hashCode();
		hash += 31 * this.tubeGridManager.hashCode();

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
	public void copy(FuelAssembly otherObject) {
		// begin-user-code

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
		FuelAssembly assembly = new FuelAssembly(0);

		// Copy contents
		assembly.copy(this);

		// Return newly instantiated object
		return assembly;

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
		children.add(this.gridLabelProvider);
		children.add(this.tubeGridManager);

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
			if (lWRGridManager.getName().equals(this.TUBE_GRID_MANAGER_NAME)) {

				this.tubeGridManager = (LWRGridManager) childComponent;

			}

			// If this is an LWRComposite
		} else if (childComponent.getHDF5LWRTag() == HDF5LWRTagType.LWRCOMPOSITE) {

			// Cast into a LWRComposite object
			LWRComposite lWRComposite = (LWRComposite) childComponent;

			// Assign to correct object
			if (lWRComposite.getName().equals(this.TUBE_COMPOSITE_NAME)) {

				// Remove the tube from the composite and add it back
				this.lWRComponents.remove(this.TUBE_COMPOSITE_NAME);
				this.tubeComposite = lWRComposite;
				this.lWRComponents.put(tubeComposite.getName(), tubeComposite);

			}

			// If it is a rod cluster assembly
		} else if (childComponent.getHDF5LWRTag() == HDF5LWRTagType.ROD_CLUSTER_ASSEMBLY) {

			this.rodClusterAssembly = (RodClusterAssembly) childComponent;

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

		boolean flag = super.readAttributes(h5Group);

		if (flag) {
			this.gridLabelProvider = new GridLabelProvider(this.size);
			this.gridLabelProvider.setName(GRID_LABEL_PROVIDER_NAME);
			this.tubeGridManager = new LWRGridManager(this.size);
			this.tubeGridManager.setName(TUBE_GRID_MANAGER_NAME);
		}

		return flag;

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
	public LWRDataProvider getTubeDataProviderAtLocation(int row, int column) {
		// begin-user-code

		// Pass through
		return this.tubeGridManager.getDataProviderAtLocation(new GridLocation(
				row, column));

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