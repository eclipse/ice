/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
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
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;

import org.eclipse.ice.io.hdf.HdfReaderFactory;
import org.eclipse.ice.io.hdf.HdfWriterFactory;
import org.eclipse.ice.io.hdf.IHdfReadable;
import org.eclipse.ice.io.hdf.IHdfWriteable;
import org.eclipse.ice.reactor.AssemblyType;
import org.eclipse.ice.reactor.GridLabelProvider;
import org.eclipse.ice.reactor.GridLocation;
import org.eclipse.ice.reactor.HDF5LWRTagType;
import org.eclipse.ice.reactor.ILWRComponentVisitor;
import org.eclipse.ice.reactor.LWRComponent;
import org.eclipse.ice.reactor.LWRComposite;
import org.eclipse.ice.reactor.LWRDataProvider;
import org.eclipse.ice.reactor.LWRGridManager;
import org.eclipse.ice.reactor.LWReactor;

/**
 * <p>
 * The PressurizedWaterReactor (PWReactor) class represents any Pressurized
 * Water Reactor. This class extends the LWReactor and provides a defined
 * utility for representing a PWReactor. It can store specific assemblies in
 * their own grids, so that one type of assembly can share the same position as
 * another type.
 * </p>
 * <p>
 * Keep in mind although there are many ways to prevent erroneous
 * PressurizedWaterReactors from being built, this class should not manage the
 * deeper logic behind building a reactor. For example, there is no logic to
 * stop a user from setting all assemblies in all locations (although it does
 * have logic to stop two assemblies of the same type to be stored at the same
 * location). The deeper types of logic are up to the user of this class who
 * delegates the conversion between their reactor model to this model. This is
 * to allow flexibility and portability to many applications so that other users
 * can apply this model to earlier or later versions of a
 * PressurizedWaterReactor with minimal restrictions.
 * </p>
 * <p>
 * Please note that when the add&lt;Component&gt;() operation is used, if a
 * &lt;Component&gt; with the same name exists in the &lt;Component&gt;
 * collection, then the &lt;Component&gt; will not be added. When using the
 * set&lt;Component&gt;Location() operation, if a &lt;Component&gt;with the same
 * name exists at the provided location, then the current &lt;Component&gt; name
 * at the provided location will be overwritten. Here, &lt;Component&gt; can be
 * (but not limited to) "ControlBanks", "FuelAssembly", "IncoreInstrument", or
 * "RodClusterAssembly". Please check the enumeration called AssemblyTypes for a
 * complete list of Assemblies that could be managed by this class.
 * </p>
 * <p>
 * </p>
 * <p>
 * </p>
 * 
 * @author Scott Forest Hull II
 */
@XmlRootElement
public class PressurizedWaterReactor extends LWReactor {
	/**
	 * <p>
	 * The map of grid managers, keyed by AssemblyType, that manage state point
	 * data for the assemblies.
	 * </p>
	 * 
	 */
	@XmlTransient
	private HashMap<AssemblyType, LWRGridManager> managers;
	/**
	 * <p>
	 * The map of assembly composites, keyed by AssemblyType, that represent the
	 * collections of the different assemblies in this reactor.
	 * </p>
	 * 
	 */
	@XmlTransient
	private HashMap<AssemblyType, LWRComposite> assemblyComposites;
	/**
	 * <p>
	 * ﻿The distance between assemblies in the core, determined by the seating
	 * location in the core plates.
	 * </p>
	 * 
	 */
	@XmlTransient
	private double fuelAssemblyPitch;
	/**
	 * <p>
	 * The GridLabelProvider for this PWReactor.
	 * </p>
	 * 
	 */
	@XmlTransient
	private GridLabelProvider gridLabelProvider;

	@XmlTransient
	public static final String CONTROL_BANK_COMPOSITE_NAME = "Control Banks";
	@XmlTransient
	public static final String FUEL_ASSEMBLY_COMPOSITE_NAME = "Fuel Assemblies";
	@XmlTransient
	public static final String INCORE_INSTRUMENT_COMPOSITE_NAME = "Incore Instruments";
	@XmlTransient
	public static final String ROD_CLUSTER_ASSEMBLY_COMPOSITE_NAME = "Rod Cluster Assemblies";
	@XmlTransient
	public static final String CONTROL_BANK_GRID_MANAGER_NAME = "Control Bank Grid";
	@XmlTransient
	public static final String FUEL_ASSEMBLY_GRID_MANAGER_NAME = "Fuel Assembly Grid";
	@XmlTransient
	public static final String INCORE_INSTRUMENT_GRID_MANAGER_NAME = "Incore Instrument Grid";
	@XmlTransient
	public static final String ROD_CLUSTER_ASSEMBLY_GRID_MANAGER_NAME = "Rod Cluster Assembly Grid";
	@XmlTransient
	public static final String GRID_LABEL_PROVIDER_NAME = "Grid Labels";

	/**
	 * A default constructor that should ONLY be used for persistence and
	 * testing. It is equivalent to PressurizedWaterReactor(15).
	 */
	public PressurizedWaterReactor() {
		this(15);
	}

	/**
	 * <p>
	 * A parameterized Constructor.
	 * </p>
	 * 
	 * @param size
	 *            <p>
	 *            The number of fuel assemblies across the core.
	 *            </p>
	 */
	public PressurizedWaterReactor(int size) {
		super(size);

		// Set the default value of size
		if (size > 0) {
			this.size = size;
		} else {
			this.size = 17; // Auto default size!
		}

		// Initialize everything
		// Setup row and column information

		// Setup Control Banks information
		LWRComposite controlBankComposite = new LWRComposite();
		controlBankComposite.setName(PressurizedWaterReactor.CONTROL_BANK_COMPOSITE_NAME);
		controlBankComposite
				.setDescription("A Composite that contains many ControlBank Components.");
		controlBankComposite.setId(1);
		// Put into the lWRComponents
		this.lWRComponents.put(controlBankComposite.getName(),
				controlBankComposite);

		// Setup Grid Manager
		LWRGridManager controlBankGridManager = new LWRGridManager(this.size);
		controlBankGridManager.setName(PressurizedWaterReactor.CONTROL_BANK_GRID_MANAGER_NAME);

		// Setup Fuel Assemblies information
		LWRComposite fuelAssemblyComposite = new LWRComposite();
		fuelAssemblyComposite.setName(PressurizedWaterReactor.FUEL_ASSEMBLY_COMPOSITE_NAME);
		fuelAssemblyComposite
				.setDescription("A Composite that contains many FuelAssembly Components.");
		fuelAssemblyComposite.setId(2);
		// Put into the lWRComponents
		this.lWRComponents.put(fuelAssemblyComposite.getName(),
				fuelAssemblyComposite);

		// Setup Grid Manager
		LWRGridManager fuelAssemblyGridManager = new LWRGridManager(this.size);
		fuelAssemblyGridManager.setName(PressurizedWaterReactor.FUEL_ASSEMBLY_GRID_MANAGER_NAME);

		this.fuelAssemblyPitch = 0.0;

		// Setup InCoreInstruments
		LWRComposite incoreInstrumentComposite = new LWRComposite();
		incoreInstrumentComposite
				.setName(PressurizedWaterReactor.INCORE_INSTRUMENT_COMPOSITE_NAME);
		incoreInstrumentComposite
				.setDescription("A Composite that contains many IncoreInstrument Components.");
		incoreInstrumentComposite.setId(3);
		// Put into the lWRComponents
		this.lWRComponents.put(incoreInstrumentComposite.getName(),
				incoreInstrumentComposite);

		// Setup Grid Manager
		LWRGridManager incoreInstrumentGridManager = new LWRGridManager(
				this.size);
		incoreInstrumentGridManager
				.setName(PressurizedWaterReactor.INCORE_INSTRUMENT_GRID_MANAGER_NAME);

		// Setup RodClusterAssemblies
		LWRComposite rodClusterAssemblyComposite = new LWRComposite();
		rodClusterAssemblyComposite
				.setName(PressurizedWaterReactor.ROD_CLUSTER_ASSEMBLY_COMPOSITE_NAME);
		rodClusterAssemblyComposite
				.setDescription("A Composite that contains many RodClusterAssembly Components.");
		rodClusterAssemblyComposite.setId(4);
		// Put into the lWRComponents
		this.lWRComponents.put(rodClusterAssemblyComposite.getName(),
				rodClusterAssemblyComposite);

		// Setup Grid Manager
		LWRGridManager rodClusterAssemblyGridManager = new LWRGridManager(
				this.size);
		rodClusterAssemblyGridManager
				.setName(PressurizedWaterReactor.ROD_CLUSTER_ASSEMBLY_GRID_MANAGER_NAME);

		// Add a default GridLabelProvider
		this.gridLabelProvider = new GridLabelProvider(this.size);
		this.gridLabelProvider.setName(PressurizedWaterReactor.GRID_LABEL_PROVIDER_NAME);

		// Setup the Name, Id, Description
		this.name = "PWReactor 1";
		this.id = 1;
		this.description = "PWReactor 1's Description";

		// Setup the LWRComponentType to the correct Type
		this.HDF5LWRTag = HDF5LWRTagType.PWREACTOR;

		// Setup the composite map
		assemblyComposites = new HashMap<AssemblyType, LWRComposite>();
		assemblyComposites.put(AssemblyType.ControlBank, controlBankComposite);
		assemblyComposites.put(AssemblyType.Fuel, fuelAssemblyComposite);
		assemblyComposites.put(AssemblyType.IncoreInstrument,
				incoreInstrumentComposite);
		assemblyComposites.put(AssemblyType.RodCluster,
				rodClusterAssemblyComposite);

		// Setup the grid map
		managers = new HashMap<AssemblyType, LWRGridManager>();
		managers.put(AssemblyType.ControlBank, controlBankGridManager);
		managers.put(AssemblyType.Fuel, fuelAssemblyGridManager);
		managers.put(AssemblyType.IncoreInstrument, incoreInstrumentGridManager);
		managers.put(AssemblyType.RodCluster, rodClusterAssemblyGridManager);

	}

	/*
	 * Overrides a method from LWReactor.
	 */
	@Override
	public int getSize() {

		return this.size;
	}

	/**
	 * <p>
	 * Returns the GridLabelProvider for this PWReactor.
	 * </p>
	 * 
	 * @return <p>
	 *         The GridLabelProvider for this PWReactor.
	 *         </p>
	 */
	public GridLabelProvider getGridLabelProvider() {

		return this.gridLabelProvider;
	}

	/**
	 * <p>
	 * Sets the GridLabelProvider for this PWReactor.
	 * </p>
	 * 
	 * @param gridLabelProvider
	 *            <p>
	 *            The GridLabelProvider for this FuelAssembly.
	 *            </p>
	 */
	public void setGridLabelProvider(GridLabelProvider gridLabelProvider) {

		// Set the grid label provider if it is not null and the same size
		if (gridLabelProvider != null
				&& gridLabelProvider.getSize() == this.size) {
			this.gridLabelProvider = gridLabelProvider;
		}

	}

	/**
	 * <p>
	 * Returns the distance between assemblies in the core, determined by the
	 * seating location in the core plates.
	 * </p>
	 * 
	 * @return <p>
	 *         The distance between assemblies in the core, determined by the
	 *         seating location in the core plates.
	 *         </p>
	 */
	public double getFuelAssemblyPitch() {

		return this.fuelAssemblyPitch;

	}

	/**
	 * <p>
	 * Sets the distance between assemblies in the core, determined by the
	 * seating location in the core plates.
	 * </p>
	 * 
	 * @param fuelAssemblyPitch
	 *            <p>
	 *            The distance between assemblies in the core, determined by the
	 *            seating location in the core plates.
	 *            </p>
	 */
	public void setFuelAssemblyPitch(double fuelAssemblyPitch) {

		if (fuelAssemblyPitch >= 0.0) {
			this.fuelAssemblyPitch = fuelAssemblyPitch;
		}

	}

	/*
	 * Overrides a method from LWReactor.
	 */
	@Override
	public boolean readAttributes(H5Group h5Group) {

		boolean flag = super.readAttributes(h5Group);

		Double fuelAssemblyPitch = HdfReaderFactory.readDoubleAttribute(
				h5Group, "fuelAssemblyPitch");

		// If any of them are erroneous, return false
		if (fuelAssemblyPitch == null || !flag) {
			return false;
		}

		// Set the primitive data
		this.fuelAssemblyPitch = fuelAssemblyPitch.doubleValue();

		// Reset info
		this.gridLabelProvider = new GridLabelProvider(this.size);
		this.gridLabelProvider.setName(PressurizedWaterReactor.GRID_LABEL_PROVIDER_NAME);

		LWRGridManager controlBankGridManager = new LWRGridManager(this.size);
		controlBankGridManager.setName(PressurizedWaterReactor.CONTROL_BANK_GRID_MANAGER_NAME);
		LWRGridManager fuelAssemblyGridManager = new LWRGridManager(this.size);
		fuelAssemblyGridManager.setName(FUEL_ASSEMBLY_GRID_MANAGER_NAME);
		LWRGridManager incoreInstrumentGridManager = new LWRGridManager(
				this.size);
		incoreInstrumentGridManager
				.setName(INCORE_INSTRUMENT_GRID_MANAGER_NAME);
		LWRGridManager rodClusterAssemblyGridManager = new LWRGridManager(
				this.size);
		rodClusterAssemblyGridManager
				.setName(ROD_CLUSTER_ASSEMBLY_GRID_MANAGER_NAME);

		// Setup the grid map
		managers = new HashMap<AssemblyType, LWRGridManager>();
		managers.put(AssemblyType.ControlBank, controlBankGridManager);
		managers.put(AssemblyType.Fuel, fuelAssemblyGridManager);
		managers.put(AssemblyType.IncoreInstrument, incoreInstrumentGridManager);
		managers.put(AssemblyType.RodCluster, rodClusterAssemblyGridManager);

		return true;

	}

	/*
	 * Overrides a method from LWReactor.
	 */
	@Override
	public boolean writeAttributes(H5File h5File, H5Group h5Group) {

		boolean flag = true;

		flag &= super.writeAttributes(h5File, h5Group);
		flag &= HdfWriterFactory.writeDoubleAttribute(h5File, h5Group,
				"fuelAssemblyPitch", fuelAssemblyPitch);

		return flag;
	}

	/*
	 * Overrides a method from LWReactor.
	 */
	@Override
	public boolean equals(Object otherObject) {
		// Local Declarations
		PressurizedWaterReactor reactor;
		boolean retVal = false;
		// Return true if they are equal on the heap
		if (otherObject == this) {
			return true;
		}
		// If the otherObject is null or if the otherObject is not an instance
		// of this object, return false
		if (otherObject != null
				&& otherObject instanceof PressurizedWaterReactor) {
			// Cast it
			reactor = (PressurizedWaterReactor) otherObject;
			// Compare values
			retVal = super.equals(otherObject)
					&& this.size == reactor.size
					&& this.fuelAssemblyPitch == reactor.fuelAssemblyPitch
					&& this.managers.equals(reactor.managers)
					&& this.assemblyComposites
							.equals(reactor.assemblyComposites)
					&& this.gridLabelProvider.equals(reactor.gridLabelProvider);
		}
		// Return retVal
		return retVal;
	}

	/*
	 * Overrides a method from LWRComposite.
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

		// Add the label provider to the children
		children.add(this.gridLabelProvider);

		// Add all the managers
		children.addAll(this.managers.values());

		return children;
	}

	/*
	 * Overrides a method from LWRComposite.
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

		// If this is a GridLabelProvider
		if (childComponent.getHDF5LWRTag() == HDF5LWRTagType.GRID_LABEL_PROVIDER
				&& childComponent.getName().equals(GRID_LABEL_PROVIDER_NAME)) {

			// Assign to variable
			this.gridLabelProvider = (GridLabelProvider) childComponent;

			// If this is a LwrGridManager
		} else if (childComponent.getHDF5LWRTag() == HDF5LWRTagType.LWRGRIDMANAGER) {

			// Cast into a LWRGridManager object
			LWRGridManager lWRGridManager = (LWRGridManager) childComponent;

			// Check the name and assign to correct object
			if (lWRGridManager.getName().equals(
					PressurizedWaterReactor.CONTROL_BANK_GRID_MANAGER_NAME)) {

				this.managers.put(AssemblyType.ControlBank, lWRGridManager);

			} else if (lWRGridManager.getName().equals(
					PressurizedWaterReactor.FUEL_ASSEMBLY_GRID_MANAGER_NAME)) {

				this.managers.put(AssemblyType.Fuel, lWRGridManager);

			} else if (lWRGridManager.getName().equals(
					PressurizedWaterReactor.INCORE_INSTRUMENT_GRID_MANAGER_NAME)) {

				this.managers
						.put(AssemblyType.IncoreInstrument, lWRGridManager);

			} else if (lWRGridManager.getName().equals(
					PressurizedWaterReactor.ROD_CLUSTER_ASSEMBLY_GRID_MANAGER_NAME)) {

				this.managers.put(AssemblyType.RodCluster, lWRGridManager);

			}

			// If this is an LWRComposite
		} else if (childComponent.getHDF5LWRTag() == HDF5LWRTagType.LWRCOMPOSITE) {

			// Cast into a LWRComposite object
			LWRComposite lWRComposite = (LWRComposite) childComponent;

			// Check the name and assign to correct object
			if (lWRComposite.getName().equals(PressurizedWaterReactor.CONTROL_BANK_COMPOSITE_NAME)) {

				// Remove existing component and replace with new one
				this.lWRComponents.remove(lWRComposite.getName());
				this.lWRComponents.put(lWRComposite.getName(), lWRComposite);
				this.assemblyComposites.put(AssemblyType.ControlBank,
						lWRComposite);

			} else if (lWRComposite.getName().equals(
					PressurizedWaterReactor.FUEL_ASSEMBLY_COMPOSITE_NAME)) {

				// Remove existing component and replace with new one
				this.lWRComponents.remove(lWRComposite.getName());
				this.lWRComponents.put(lWRComposite.getName(), lWRComposite);
				this.assemblyComposites.put(AssemblyType.Fuel, lWRComposite);

			} else if (lWRComposite.getName().equals(
					PressurizedWaterReactor.INCORE_INSTRUMENT_COMPOSITE_NAME)) {

				// Remove existing component and replace with new one
				this.lWRComponents.remove(lWRComposite.getName());
				this.lWRComponents.put(lWRComposite.getName(), lWRComposite);
				this.assemblyComposites.put(AssemblyType.IncoreInstrument,
						lWRComposite);

			} else if (lWRComposite.getName().equals(
					PressurizedWaterReactor.ROD_CLUSTER_ASSEMBLY_COMPOSITE_NAME)) {

				// Remove existing component and replace with new one
				this.lWRComponents.remove(lWRComposite.getName());
				this.lWRComponents.put(lWRComposite.getName(), lWRComposite);
				this.assemblyComposites.put(AssemblyType.RodCluster,
						lWRComposite);

			}

		}

		return true;
	}

	/*
	 * Overrides a method from LWReactor.
	 */
	@Override
	public int hashCode() {

		// Local Declarations
		int hash = super.hashCode();

		// Get the hash of local attributes
		hash += 31 * this.fuelAssemblyPitch;
		hash += 31 * this.size;
		hash += 31 * this.assemblyComposites.hashCode();
		hash += 31 * this.managers.hashCode();
		hash += 31 * this.gridLabelProvider.hashCode();

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
	public void copy(PressurizedWaterReactor otherObject) {

		// If otherObject is null, return
		if (otherObject == null) {
			return;
		}

		// Copy contents - super
		super.copy(otherObject);

		// Copy local contents
		this.fuelAssemblyPitch = otherObject.fuelAssemblyPitch;
		this.size = otherObject.size;

		// Deep copy gridManagers
		this.managers.clear();

		// Get the iterator for the GridManagers
		for (Map.Entry<AssemblyType, LWRGridManager> entry : otherObject.managers
				.entrySet()) {

			// Clone and add
			this.managers.put(entry.getKey(), (LWRGridManager) entry.getValue()
					.clone());

			// Add reference

		}

		// Due to the super's deep copy, we will need to grab the references
		// from the cloned components above
		// So, we will add them and then OVERRIDE the mapping within the
		// lwrComponents

		// Clear the assemblyComposites
		this.assemblyComposites.clear();

		for (Map.Entry<AssemblyType, LWRComposite> entry : otherObject.assemblyComposites
				.entrySet()) {
			this.lWRComponents.remove(entry.getKey());
		}

		// Add them back
		for (Map.Entry<AssemblyType, LWRComposite> entry : otherObject.assemblyComposites
				.entrySet()) {
			LWRComposite composite = (LWRComposite) entry.getValue().clone();
			this.assemblyComposites.put(entry.getKey(), composite);

			// Add back to the lwrComponents
			this.lWRComponents.put(composite.getName(), composite);
		}

		// Copy the GridLabelProvider
		this.gridLabelProvider = (GridLabelProvider) otherObject.gridLabelProvider
				.clone();

	}

	/*
	 * Overrides a method from LWReactor.
	 */
	@Override
	public Object clone() {

		// Local Declarations
		PressurizedWaterReactor reactor = new PressurizedWaterReactor(0);

		// copy contents
		reactor.copy(this);

		// Return newly instantiated object
		return reactor;

	}

	/*
	 * Overrides a method from LWRComponent.
	 */
	@Override
	public void accept(ILWRComponentVisitor visitor) {
		visitor.visit(this);
	}

	/**
	 * <p>
	 * This operation adds an assembly of the specified AssemblyType to the
	 * reactor and return true. If an assembly of the same name and type already
	 * exists in the reactor, then the new assembly will not be added and this
	 * operation will return false.
	 * </p>
	 * 
	 * @param type
	 *            <p>
	 *            The type of the assembly.
	 *            </p>
	 * @param assembly
	 *            <p>
	 *            The assembly to add to the collection of FuelAssemblies.
	 *            </p>
	 * @return <p>
	 *         True, if the assembly was added successfully.
	 *         </p>
	 */
	public boolean addAssembly(AssemblyType type, LWRComponent assembly) {

		// Local Declarations
		LWRComposite composite = assemblyComposites.get(type);
		boolean status = false;

		// Add the component to the composite
		composite.addComponent(assembly);

		// Set the return flag
		status = composite.getComponents().contains(assembly);

		return status;

	}

	/**
	 * <p>
	 * Removes an assembly of the specified type from the collection of
	 * assemblies. Returns true if the operation was successful, false
	 * otherwise.
	 * </p>
	 * 
	 * @param type
	 *            <p>
	 *            The type of the assembly.
	 *            </p>
	 * @param assemblyName
	 *            <p>
	 *            The name of the assembly to be removed.
	 *            </p>
	 * @return <p>
	 *         True, if the assembly was removed successfully.
	 *         </p>
	 */
	public boolean removeAssembly(AssemblyType type, String assemblyName) {

		// Local Declarations
		LWRComposite composite = assemblyComposites.get(type);
		LWRComponent component = composite.getComponent(assemblyName);
		LWRGridManager manager = managers.get(type);
		boolean status = true;

		// Only do this if the component exists
		if (component != null) {
			// Remove the assembly from the composite and grid
			manager.removeComponent(component);
			composite.removeComponent(assemblyName);
		} else {
			status = false;
		}

		return status;

	}

	/**
	 * <p>
	 * Returns an list of names for each assembly in the reactor of the
	 * specified type.
	 * </p>
	 * 
	 * @param type
	 *            <p>
	 *            The type of the assembly.
	 *            </p>
	 * @return <p>
	 *         An ArrayList of names for each element of the collection of
	 *         assemblies.
	 *         </p>
	 */
	public ArrayList<String> getAssemblyNames(AssemblyType type) {

		// Local Declarations
		LWRComposite composite = assemblyComposites.get(type);

		return composite.getComponentNames();
	}

	/**
	 * <p>
	 * Returns the assembly of the specified type with the provided name or null
	 * if an assembly of that type and name does not exist.
	 * </p>
	 * 
	 * @param type
	 *            <p>
	 *            The type of the assembly.
	 *            </p>
	 * @param name
	 *            <p>
	 *            The name of the assembly to find.
	 *            </p>
	 * @return <p>
	 *         The assembly
	 *         </p>
	 */
	public LWRComponent getAssemblyByName(AssemblyType type, String name) {

		// Local Declarations
		LWRComposite composite = assemblyComposites.get(type);

		// Return the composite
		return composite.getComponent(name);

	}

	/**
	 * <p>
	 * Returns the assembly of the specified type at the specified column and
	 * row in the reactor or null if one is not found at the provided location.
	 * </p>
	 * 
	 * @param type
	 *            <p>
	 *            The type of the assembly.
	 *            </p>
	 * @param row
	 *            <p>
	 *            The row id.
	 *            </p>
	 * @param column
	 *            <p>
	 *            The column id.
	 *            </p>
	 * @return <p>
	 *         The assembly corresponding to the provided type, column and row
	 *         or null if one is not found at the provided location.
	 *         </p>
	 */
	public LWRComponent getAssemblyByLocation(AssemblyType type, int row,
			int column) {

		// Local Declarations
		LWRGridManager manager = managers.get(type);
		String name = "";

		// Get the name
		name = manager.getComponentName(new GridLocation(row, column));

		// Return the component
		return getAssemblyByName(type, name);

	}

	/**
	 * <p>
	 * Sets the location for the assembly of the specified type with the
	 * provided name. Overrides the location of another assembly as required.
	 * Returns true if this operation was successful, false otherwise. Note it
	 * will return true if the same name is overridden.
	 * </p>
	 * 
	 * @param type
	 *            <p>
	 *            The type of the assembly.
	 *            </p>
	 * @param assemblyName
	 *            <p>
	 *            The name of the assembly.
	 *            </p>
	 * @param row
	 *            <p>
	 *            The row id.
	 *            </p>
	 * @param column
	 *            <p>
	 *            The column id.
	 *            </p>
	 * @return <p>
	 *         True, if the location of the assembly was set successfully.
	 *         </p>
	 */
	public boolean setAssemblyLocation(AssemblyType type, String assemblyName,
			int row, int column) {

		// Local declarations
		LWRComposite composite = assemblyComposites.get(type);
		LWRComponent component = composite.getComponent(assemblyName);
		LWRGridManager manager = managers.get(type);
		GridLocation location = new GridLocation(row, column);
		boolean status = false;

		// Set the location, but only if it is valid. This makes sure that
		// negative row numbers and other nasty things were not passed to this
		// operation.
		if (location.getColumn() == column && location.getRow() == row) {

			// Set the location
			manager.addComponent(component, location);

			// Update the status
			status = (manager.getComponentName(location) != null && manager
					.getComponentName(location).equals(assemblyName));
		}

		return status;
	}

	/**
	 * <p>
	 * Removes the assembly at the provided location and of the specified if it
	 * exists. Returns true if the removal was successful, false otherwise.
	 * </p>
	 * 
	 * @param type
	 *            <p>
	 *            The type of the assembly.
	 *            </p>
	 * @param row
	 *            <p>
	 *            The row id.
	 *            </p>
	 * @param column
	 *            <p>
	 *            The column id.
	 *            </p>
	 * @return <p>
	 *         True, if the assembly removal was successful.
	 *         </p>
	 */
	public boolean removeAssemblyFromLocation(AssemblyType type, int row,
			int column) {

		// Local Declarations
		LWRGridManager manager = managers.get(type);
		boolean status = true;
		GridLocation location = new GridLocation(row, column);
		String name = manager.getComponentName(location);

		// Only do this if there is a component at the specified location
		if (name != null) {
			// Remove the component from the location
			manager.removeComponent(location);
		} else {
			status = false;
		}

		return status;
	}

	/**
	 * <p>
	 * Returns the data provider for the assembly of the specified type at the
	 * given location or null if it does not exist.
	 * </p>
	 * 
	 * @param type
	 *            <p>
	 *            The type of the assembly.
	 *            </p>
	 * @param row
	 *            <p>
	 *            The row id.
	 *            </p>
	 * @param column
	 *            <p>
	 *            The column id.
	 *            </p>
	 * @return <p>
	 *         The DataProvider that manages state point data for the specified
	 *         assembly.
	 *         </p>
	 */
	public LWRDataProvider getAssemblyDataProviderAtLocation(AssemblyType type,
			int row, int column) {

		// Local Declarations
		LWRGridManager manager = managers.get(type);

		// Return the provider
		return manager.getDataProviderAtLocation(new GridLocation(row, column));

	}

	/**
	 * <p>
	 * This operation returns the number of assemblies of the specified type.
	 * </p>
	 * 
	 * @param type
	 *            <p>
	 *            The type of the assembly.
	 *            </p>
	 * @return <p>
	 *         The number of assemblies of the specified type.
	 *         </p>
	 */
	public int getNumberOfAssemblies(AssemblyType type) {
		return assemblyComposites.get(type).getNumberOfComponents();
	}

}