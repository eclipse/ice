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
package org.eclipse.ice.reactor.sfr.core;

import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.reactor.sfr.base.GridManager;
import org.eclipse.ice.reactor.sfr.base.ISFRComponentVisitor;
import org.eclipse.ice.reactor.sfr.base.SFRComponent;
import org.eclipse.ice.reactor.sfr.base.SFRComposite;
import org.eclipse.ice.reactor.sfr.core.assembly.SFRAssembly;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * <p>
 * Class represents a sodium fast reactor at the highest core-level view.
 * </p>
 * 
 * @author w5q
 */
public class SFReactor extends SFRComposite {
	/**
	 * <p>
	 * Number of assemblies in the reactor core.
	 * </p>
	 * 
	 */
	private int size;
	/**
	 * <p>
	 * Shortest distance between centers of adjacent assemblies.
	 * </p>
	 * 
	 */
	private double latticePitch;
	/**
	 * <p>
	 * The physical size of assemblies within the SFR core; all assemblies are
	 * assumed to be the same size. Defined as the distance from one flat outer
	 * duct surface to the outer surface parallel to it.
	 * </p>
	 * 
	 */
	private double outerFlatToFlat;
	/**
	 * <p>
	 * The map of assembly composites, keyed by AssemblyType, that represent the
	 * collections of the different assemblies in this reactor.
	 * </p>
	 * 
	 */
	private HashMap<AssemblyType, SFRComposite> assemblyComposites;
	/**
	 * <p>
	 * The map of GridManagers, keyed by AssemblyType, that manage the locations
	 * of assemblies within a reactor.
	 * </p>
	 * 
	 */
	private HashMap<AssemblyType, GridManager> assemblyManagers;

	/**
	 * <p>
	 * Parameterized constructor with reactor size (number of assemblies)
	 * specified.
	 * </p>
	 * 
	 * @param size
	 *            Size of the reactor (number of assemblies).
	 */
	public SFReactor(int size) {

		// Store the size, but it must be a positive number.
		this.size = (size > 0 ? size : 1);

		// Set the default name, description, and ID.
		setName("SFReactor 1");
		setDescription("SFReactor 1's Description");
		setId(1);

		// Set the default latticePitch and outerFlatToFlat. This setting means
		// each assembly in the core is 1 unit wide and touches its six adjacent
		// assemblies.
		latticePitch = 1.0;
		outerFlatToFlat = 1.0;

		// Initialize the List of assembly SFRComposites.
		assemblyComposites = new HashMap<AssemblyType, SFRComposite>();

		// Initialize the Map of GridManagers.
		assemblyManagers = new HashMap<AssemblyType, GridManager>();

		/* ---- Set up basic Composites for each AssemblyType. ---- */
		// An ID counter to generate unique IDs for each Composite in the Map.
		int id = 1;

		// Generate a Composite for each AssemblyType and put it in the Map.
		for (AssemblyType type : AssemblyType.values()) {
			// Get the user-friendly String for the AssemblyType.
			String typeName = type.toString();

			// Create a new Composite for the assembly type.
			SFRComposite composite = new SFRComposite();
			composite.setName(typeName + " Composite");
			composite.setDescription("A Composite that contains many "
					+ typeName + " Components.");
			composite.setId(id++);

			// Store the Composite in the SFReactor's assembly Composite Map and
			// the Component Map (inherited from SFRComposite).
			assemblyComposites.put(type, composite);
			super.addComponent(composite);

			// Create a new GridManager for this assembly type.
			assemblyManagers.put(type, new GridManager(this.size * this.size));
		}
		/* -------------------------------------------------------- */

		/* ---- Customize individual Composites here as necessary. ---- */
		/* ------------------------------------------------------------ */

		return;
	}

	/**
	 * <p>
	 * Returns the size (number of assemblies) of the reactor core.
	 * </p>
	 * 
	 * @return Size of the reactor (number of assemblies).
	 */
	public int getSize() {
		return size;
	}

	/**
	 * <p>
	 * Sets the lattice pitch.
	 * </p>
	 * 
	 * @param latticePitch
	 *            The lattice pitch of the reactor. Must be positive.
	 */
	public void setLatticePitch(double latticePitch) {

		// Only allow positive lattice pitch.
		if (latticePitch > 0.0) {
			this.latticePitch = latticePitch;
		}
		return;
	}

	/**
	 * <p>
	 * Returns the lattice pitch (shortest distance between centers of adjacent
	 * assemblies) as a double.
	 * </p>
	 * 
	 * @return The lattice pitch of the reactor.
	 */
	public double getLatticePitch() {
		return latticePitch;
	}

	/**
	 * <p>
	 * Sets the outer flat-to-flat distance.
	 * </p>
	 * 
	 * @param outerFlatToFlat
	 *            The outer flat-to-flat distance. Must be positive.
	 */
	public void setOuterFlatToFlat(double outerFlatToFlat) {

		// Only allow outer flat-to-flat.
		if (outerFlatToFlat > 0.0) {
			this.outerFlatToFlat = outerFlatToFlat;
		}
		return;
	}

	/**
	 * <p>
	 * Returns the outer flat-to-flat distance as a double.
	 * </p>
	 * 
	 * @return The outer flat-to-flat distance.
	 */
	public double getOuterFlatToFlat() {
		return outerFlatToFlat;
	}

	/**
	 * <p>
	 * An operation that overrides the SFRComposite's operation. This operation
	 * does nothing and requires that the appropriate, more defined, associated
	 * operation to be utilized on this class.
	 * </p>
	 * 
	 * @param child
	 *            The Component that should be added to the Composite.
	 */
	@Override
	public void addComponent(Component child) {
		return;
	}

	/**
	 * <p>
	 * An operation that overrides the SFRComposite's operation. This operation
	 * does nothing and requires that the appropriate, more defined, associated
	 * operation to be utilized on this class.
	 * </p>
	 * 
	 * @param childId
	 *            The ID of the child Component to remove.
	 */
	@Override
	public void removeComponent(int childId) {
		return;
	}

	/**
	 * <p>
	 * An operation that overrides the SFRComposite's operation. This operation
	 * does nothing and requires that the appropriate, more defined, associated
	 * operation to be utilized on this class.
	 * </p>
	 * 
	 * @param name
	 *            The name of the child Component to remove.
	 */
	@Override
	public void removeComponent(String name) {
		return;
	}

	/**
	 * <p>
	 * Adds the specified assembly to the reactor core; returns true if the
	 * operation was successful.
	 * </p>
	 * 
	 * @param type
	 *            The AssemblyType of assembly being added.
	 * @param assembly
	 *            The assembly being added to the reactor.
	 * @return Returns true if the operation was successful, otherwise false.
	 */
	public boolean addAssembly(AssemblyType type, SFRAssembly assembly) {

		// Set the default initial status.
		boolean success = false;

		// Check the parameters.
		if (type != null && assembly != null) {

			// Get the Composite that contains all assemblies of the type.
			SFRComposite composite = assemblyComposites.get(type);

			// Make sure the assembly is not already in the Composite.
			if (composite.getComponent(assembly.getName()) == null) {

				// Add the new assembly to the Composite.
				composite.addComponent(assembly);

				// See if the assembly was successfully added.
				success = assembly.equals(composite.getComponent(assembly
						.getName()));
			}
		}

		return success;
	}

	/**
	 * <p>
	 * Removes the specified assembly from the reactor core; returns true if the
	 * operation was successful.
	 * </p>
	 * 
	 * @param type
	 *            The AssemblyType of the assembly being removed.
	 * @param name
	 *            The name of the assembly being removed.
	 * @return Returns true if the operation was successful, otherwise false.
	 */
	public boolean removeAssembly(AssemblyType type, String name) {

		// Set the default initial status.
		boolean success = false;

		if (type != null && name != null) {
			// Get the Composite that contains all assemblies of the type.
			SFRComposite composite = assemblyComposites.get(type);

			// Get the Component in question from the appropriate Composite.
			SFRComponent component = composite.getComponent(name);

			// Try to remove the Component.
			if (component != null) {
				// Remove it from the Composite.
				composite.removeComponent(name);

				// Remove it from the appropriate GridManager.
				assemblyManagers.get(type).removeComponent(name);

				success = true;
			}
		}

		return success;
	}

	/**
	 * <p>
	 * Removes the assembly of AssemblyType, from the specified (x, y) location;
	 * returns true if the operation was successful.
	 * </p>
	 * 
	 * @param type
	 *            The AssemblyType of the assembly being removed.
	 * @param row
	 *            The row in which the assembly to be removed is found in the
	 *            AssemblyType's GridManager.
	 * @param column
	 *            The column in which the assembly to be removed is found in the
	 *            AssemblyType's GridManager.
	 * @return Returns true if the operation was successful, otherwise false.
	 */
	public boolean removeAssemblyFromLocation(AssemblyType type, int row,
			int column) {

		// Set the default initial status.
		boolean success = false;

		// Check the parameters. If they are valid, remove the Component from
		// the specified location.
		if (type != null && row >= 0 && row < size && column >= 0
				&& column < size) {

			// Try removing the location from the corresponding GridManager.
			success = assemblyManagers.get(type).removeComponent(
					row * size + column);
		}

		return success;
	}

	/**
	 * <p>
	 * Returns the number of assemblies of AssemblyType in the reactor.
	 * </p>
	 * 
	 * @param type
	 *            The AssemblyType being searched for.
	 * @return Returns the number of assemblies, of AssemblyType, contained in
	 *         the reactor.
	 */
	public int getNumberOfAssemblies(AssemblyType type) {

		// Initialize the number of assemblies we have.
		int numberOfAssemblies = 0;

		// Only search for non-null AssemblyTypes.
		if (type != null) {
			// Get the number of Components from the appropriate Composite.
			numberOfAssemblies = assemblyComposites.get(type)
					.getNumberOfComponents();
		}

		return numberOfAssemblies;
	}

	/**
	 * <p>
	 * Returns a string ArrayList of the names of all assemblies of the
	 * specified type.
	 * </p>
	 * 
	 * @param type
	 *            The AssemblyType being searched for.
	 * @return An ArrayList of Strings containing names of all AssemblyType
	 *         assemblies in the reactor.
	 */
	public ArrayList<String> getAssemblyNames(AssemblyType type) {

		// Initialize a List of assembly names.
		ArrayList<String> assemblyNames;

		// Only search for non-null AssemblyTypes.
		if (type != null) {
			// Get the Component names from the appropriate Composite.
			assemblyNames = assemblyComposites.get(type).getComponentNames();
		} else {
			// We must return an empty List.
			assemblyNames = new ArrayList<String>();
		}

		return assemblyNames;
	}

	/**
	 * <p>
	 * Returns the assembly of the specified type and name.
	 * </p>
	 * 
	 * @param type
	 *            The AssemblyType of the assembly being searched for.
	 * @param name
	 *            The name of the assembly being searched for.
	 * @return The Component with the specified name and type. If no match was
	 *         found, returns a null assembly.
	 */
	public SFRComponent getAssemblyByName(AssemblyType type, String name) {

		// Set the default return value.
		SFRComponent component = null;

		// If the type and name are valid, fetch the Component from the
		// appropriate Composite.
		if (type != null && name != null) {
			component = assemblyComposites.get(type).getComponent(name);
		}
		return component;
	}

	/**
	 * <p>
	 * Returns the assembly of AssemblyType, at the specified (x, y)
	 * coordinates.
	 * </p>
	 * 
	 * @param type
	 *            The AssemblyType being searched for.
	 * @param row
	 *            The row of the assembly in the AssemblyType GridManager.
	 * @param column
	 *            The column of the assembly in the AssemblyType GridManager.
	 * @return Returns the Component in the specified location or null if one
	 *         could not be found satisfying the arguments.
	 */
	public SFRComponent getAssemblyByLocation(AssemblyType type, int row,
			int column) {

		// Set the default return value.
		SFRComponent component = null;

		// Check the parameters. If they are valid, fetch the Component from the
		// specified location.
		if (type != null && row >= 0 && row < size && column >= 0
				&& column < size) {

			// Get the name of the Component from the appropriate GridManager.
			String name = assemblyManagers.get(type).getComponentName(
					row * size + column);

			// Get the Component from the appropriate Composite.
			component = assemblyComposites.get(type).getComponent(name);
		}

		return component;
	}

	/**
	 * <p>
	 * Adds an assembly of the specified type and name to the reactor in the
	 * specified location.
	 * </p>
	 * 
	 * @param type
	 *            The type of assembly that will be added.
	 * @param name
	 *            The name of the assembly to set. The name must correspond to
	 *            an existing assembly.
	 * @param row
	 *            The row in which to put the assembly.
	 * @param column
	 *            The column in which to put the assembly.
	 * @return Returns whether or not the assembly location was successfully
	 *         set.
	 */
	public boolean setAssemblyLocation(AssemblyType type, String name, int row,
			int column) {

		boolean success = false;

		// Check the parameters.
		// If the component exists, add it to the appropriate GridManager.
		if (type != null && row >= 0 && row < size && column >= 0
				&& column < size
				&& assemblyComposites.get(type).getComponent(name) != null) {

			// Try adding the Component to the corresponding GridManager.
			success = assemblyManagers.get(type).addComponent(name,
					row * size + column);
		}

		return success;
	}

	/**
	 * <p>
	 * Returns an ArrayList of locations within the reactor that are occupied by
	 * the assembly matching the specified type and name.
	 * </p>
	 * 
	 * @param type
	 *            The assembly type (fuel, control, reflector) of the assembly
	 *            being searched for.
	 * @param name
	 *            The name of the assembly being searched for.
	 * @return Returns an ArrayList of the locations the specified assembly
	 *         occupies. If no match was found, returns an empty ArrayList.
	 */
	public ArrayList<Integer> getAssemblyLocations(AssemblyType type,
			String name) {

		// We need to return a List of Integers representing assembly locations.
		ArrayList<Integer> locations;

		// Check the parameters.
		if (type != null && name != null) {
			// Get the GridManager and the Component for the assembly with the
			// specified type and name.
			GridManager manager = assemblyManagers.get(type);

			// Get the List of locations found by the GridManager.
			locations = (ArrayList<Integer>) manager
					.getComponentLocations(name);
		} else {
			// If the parameters are invalid, we need to return an empty List.
			locations = new ArrayList<Integer>();
		}

		return locations;
	}

	/**
	 * <p>
	 * Overrides the equals operation to check the attributes on this object
	 * with another object of the same type. Returns true if the objects are
	 * equal. False otherwise.
	 * </p>
	 * 
	 * @param otherObject
	 *            The object to be compared against.
	 * @return True if otherObject is equal. False otherwise.
	 */
	public boolean equals(Object otherObject) {

		// By default, the objects are not equivalent.
		boolean equals = false;

		// Check the reference.
		if (this == otherObject) {
			equals = true;
		}
		// Check the information stored in the other object.
		else if (otherObject != null && otherObject instanceof SFReactor) {

			// We can now cast the other object.
			SFReactor reactor = (SFReactor) otherObject;

			// Compare the values between the two objects.
			equals = (super.equals(otherObject) && size == reactor.size
					&& latticePitch == reactor.latticePitch
					&& outerFlatToFlat == reactor.outerFlatToFlat
					&& assemblyManagers.equals(reactor.assemblyManagers) && assemblyComposites
					.equals(reactor.assemblyComposites));
		}

		return equals;
	}

	/**
	 * <p>
	 * Returns the hashCode of the object.
	 * </p>
	 * 
	 * @return The hashcode of the object.
	 */
	public int hashCode() {

		// Hash based on super's hashCode.
		int hash = super.hashCode();

		// Add local hashes.
		hash += 31 * size;
		hash += 31 * latticePitch;
		hash += 31 * outerFlatToFlat;
		hash += 31 * assemblyManagers.hashCode();
		hash += 31 * assemblyComposites.hashCode();

		return hash;
	}

	/**
	 * <p>
	 * Deep copies the contents of the object.
	 * </p>
	 * 
	 * @param otherObject
	 *            The object to be copied from.
	 */
	public void copy(SFReactor otherObject) {

		// Check the parameters.
		if (otherObject == null) {
			return;
		}
		// Copy the super's values.
		super.copy(otherObject);

		// Copy the local values.
		size = otherObject.size;
		latticePitch = otherObject.latticePitch;
		outerFlatToFlat = otherObject.outerFlatToFlat;
		assemblyManagers.clear();
		assemblyManagers.putAll(otherObject.assemblyManagers);
		assemblyComposites.clear();
		assemblyComposites.putAll(otherObject.assemblyComposites);

		return;
	}

	/**
	 * <p>
	 * Deep copies and returns a newly instantiated object.
	 * </p>
	 * 
	 * @return The newly instantiated copied object.
	 */
	public Object clone() {

		// Initialize a new object.
		SFReactor object = new SFReactor(size);

		// Copy the contents from this one.
		object.copy(this);

		// Return the newly instantiated object.
		return object;
	}

	/**
	 * Overrides the default behavior (ignore) from SFRComponent and implements
	 * the accept operation for this SFRComponent's type.
	 */
	@Override
	public void accept(ISFRComponentVisitor visitor) {

		if (visitor != null) {
			visitor.visit(this);
		}
		return;
	}

}