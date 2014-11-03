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
package org.eclipse.ice.reactor.sfr.core.assembly;

import org.eclipse.ice.datastructures.updateableComposite.Component;
import org.eclipse.ice.reactor.sfr.base.GridDataManager;
import org.eclipse.ice.reactor.sfr.base.ISFRComponentVisitor;
import org.eclipse.ice.reactor.sfr.base.SFRComponent;
import org.eclipse.ice.reactor.sfr.core.AssemblyType;

import java.util.ArrayList;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Class representing radial reflector assemblies. Differentiated from
 * PinAssembly, as radial reflectors contain solid rods rather than pins.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author w5q
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ReflectorAssembly extends SFRAssembly {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The shortest distance between centers of adjacent reflector rods.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private double rodPitch;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A GridManager used to manage the locations of rods within this assembly.
	 * </p>
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 *            <!-- end-UML-doc -->
	 */
	private GridDataManager rodManager;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Parameterized constructor with the size (number of rods) specified.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param size
	 *            Size (number of rods) of the assembly.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ReflectorAssembly(int size) {
		// begin-user-code

		// Call the super constructor with some defaults.
		super("SFR Reflector Assembly 1", AssemblyType.Reflector, size);

		// Set the default description, and ID.
		setDescription("SFR Reflector Assembly 1's Description");
		setId(1);

		// Initialize rodPitch.
		rodPitch = 1.0;

		// Initialize the rod manager.
		rodManager = new GridDataManager(getSize() * getSize());

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Parameterized constructor with the name and size (number of rods)
	 * specified.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param name
	 *            Name of the assembly.
	 * @param size
	 *            Size (number of rods) of the assembly.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ReflectorAssembly(String name, int size) {
		// begin-user-code

		// Call the basic constructor. Sets all defaults.
		this(size);

		// Set the name.
		setName(name);

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the rod pitch (shortest distance from rod center to an adjacent rod
	 * center).
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param rodPitch
	 *            The rod pitch. Must be non-negative.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setRodPitch(double rodPitch) {
		// begin-user-code

		// Only set the rod pitch if it is 0 or greater.
		if (rodPitch >= 0.0) {
			this.rodPitch = rodPitch;
		}
		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the rod pitch (shortest distance from rod center to an adjacent
	 * rod center) as a double.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return Returns the rod pitch as a double.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public double getRodPitch() {
		// begin-user-code
		return rodPitch;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Adds the specified SFRRod to the Reflector assembly; returns true if the
	 * operation is successful.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param rod
	 *            The rod to be added to the assembly.
	 * @return Returns true if the operation was successful, otherwise false.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean addRod(SFRRod rod) {
		// begin-user-code

		// By default, we did not succeed in adding the Component.
		boolean success = false;

		// Check the parameters. Also make sure that the rod does not already
		// exist in the collection of Components.
		if (rod != null && !rod.equals(getComponent(rod.getName()))) {

			// Add the new Component to this Composite.
			super.addComponent(rod);

			// See if the rod was successfully added.
			success = rod.equals(getComponent(rod.getName()));
		}

		return success;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Adds the rod with the specified name to the assembly in the specified
	 * location. If the rod exists and the location is valid and is not occupied
	 * by the same rod, this returns true.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param name
	 *            The name of the rod to set. The name must correspond to an
	 *            existing rod.
	 * @param row
	 *            The row in which to put the rod.
	 * @param column
	 *            The column in which to put the rod.
	 * @return Returns whether or not the rod location was successfully set.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean setRodLocation(String name, int row, int column) {
		// begin-user-code

		// By default, we did not succeed in adding the Component.
		boolean success = false;

		// Check the parameters.
		int size = getSize();
		// If the Component exists, add it to the GridManager location.
		if (name != null && row >= 0 && row < size && column >= 0
				&& column < size && getComponent(name) != null) {
			success = rodManager.addComponent(name, row * size + column);
		}

		return success;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Removes the rod with the specified name from the ReflectorAssembly;
	 * returns true if the operation is successful.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param name
	 *            The name of the rod to remove.
	 * @return Returns true if the operation was successful, false otherwise.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean removeRod(String name) {
		// begin-user-code

		// By default, we did not succeed in removing the Component.
		boolean success = false;

		// Check the parameters.
		// If there is a rod with that name, remove it.
		if (name != null && getRodByName(name) != null) {
			// Remove the Component from this Composite.
			super.removeComponent(name);

			// Remove it from the GridManager.
			rodManager.removeComponent(name);

			success = true;
		}

		return success;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Removes the rod with the specified location (x, y coordinates) from the
	 * ReflectorAssembly; returns true operation is successful.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param row
	 *            The row in which the rod is located.
	 * @param column
	 *            The column in which the rod is located.
	 * @return Returns true if the operation was successful, false otherwise.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean removeRodFromLocation(int row, int column) {
		// begin-user-code

		// By default, we did not succeed in removing the Component from the
		// location.
		boolean success = false;

		// Check the parameters.
		int size = getSize();
		if (row >= 0 && row < size && column >= 0 && column < size) {

			// Try removing the location from the GridManager.
			success = rodManager.removeComponent(row * size + column);
		}

		return success;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns a String ArrayList of all rod names contained within the
	 * assembly.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return ArrayList of Strings representing names of all rods in the
	 *         assembly.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<String> getRodNames() {
		// begin-user-code
		return getComponentNames();
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the SFRRod with the specified name in the assembly.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param name
	 *            The name of the rod to be searched for.
	 * @return Returns the rod with the specified name. Returns a null rod if
	 *         none was found.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public SFRRod getRodByName(String name) {
		// begin-user-code
		return (SFRRod) getComponent(name);
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the SFRRod at the specified location (x, y coordinates) in the
	 * assembly.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param row
	 *            The row in which the rod is located.
	 * @param column
	 *            The column in which the rod is located.
	 * @return Returns the rod found in the specified location. Returns a null
	 *         rod if no match is found.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public SFRRod getRodByLocation(int row, int column) {
		// begin-user-code

		// Initialize the default return value.
		SFRRod rod = null;

		// Check the parameters.
		int size = getSize();
		if (row >= 0 && row < size && column >= 0 && column < size) {

			// Get the Component from this Composite.
			rod = getRodByName(rodManager.getComponentName(row * size + column));
		}

		return rod;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns an ArrayList of locations within the assembly that are occupied
	 * by the rod matching the specified name.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param name
	 *            The name of the rod being searched for.
	 * @return Returns an ArrayList of the locations the specified rod occupies.
	 *         If no match was found, returns an empty ArrayList.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<Integer> getRodLocations(String name) {
		// begin-user-code

		// We need to return a List of Integers representing rod locations.
		ArrayList<Integer> locations;

		// Check the parameters.
		if (name != null) {
			// Get the list of locations found by the GridManager.
			locations = (ArrayList<Integer>) rodManager
					.getComponentLocations(name);
		} else {
			// If the parameters are invalid, we need to return an empty List.
			locations = new ArrayList<Integer>();
		}

		return locations;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the number of rods in the assembly.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return The number of rods in the assembly.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int getNumberOfRods() {
		// begin-user-code
		return getNumberOfComponents();
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns an IDataProvider for the location in the assembly. This is
	 * distinct from the SFRRod, which, as an SFRComponent, is itself an
	 * IDataProvider.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param row
	 *            The row in which the IDataProvider is located.
	 * @param column
	 *            The column in which the IDataProvider is located.
	 * @return Returns the IDataProvider at the specified location. Returns null
	 *         if no match was found.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public SFRComponent getDataProviderByLocation(int row, int column) {
		// begin-user-code

		// Initialize the default return value.
		SFRComponent provider = null;

		// Check the parameters.
		int size = getSize();
		if (row >= 0 && row < size && column >= 0 && column < size) {

			// Get the IDataProvider from the GridDataManager.
			provider = rodManager.getDataProvider(row * size + column);
		}

		return provider;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * An operation that overrides the SFRComposite's operation. This operation
	 * does nothing and requires that the appropriate, more defined, associated
	 * operation to be utilized on this class.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param child
	 *            The Component that should be added to the Composite.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Override
	public void addComponent(Component child) {
		// begin-user-code
		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * An operation that overrides the SFRComposite's operation. This operation
	 * does nothing and requires that the appropriate, more defined, associated
	 * operation to be utilized on this class.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param childId
	 *            The ID of the child Component to remove.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Override
	public void removeComponent(int childId) {
		// begin-user-code
		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * An operation that overrides the SFRComposite's operation. This operation
	 * does nothing and requires that the appropriate, more defined, associated
	 * operation to be utilized on this class.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param name
	 *            The name of the child Component to remove.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Override
	public void removeComponent(String name) {
		// begin-user-code
		return;
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
	 *            The object to be compared.
	 * @return True if otherObject is equal. False otherwise.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Override
	public boolean equals(Object otherObject) {
		// begin-user-code

		// By default, the objects are not equivalent.
		boolean equals = false;

		// Check the reference.
		if (this == otherObject) {
			equals = true;
		}
		// Check the information stored in the other object.
		else if (otherObject != null
				&& otherObject instanceof ReflectorAssembly) {

			// We can now cast the other object.
			ReflectorAssembly assembly = (ReflectorAssembly) otherObject;

			// Compare the values between the two objects.
			equals = (super.equals(otherObject)
					&& rodPitch == assembly.rodPitch && rodManager
					.equals(assembly.rodManager));
		}

		return equals;
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

		// Hash based on super's hashCode.
		int hash = super.hashCode();

		// Add local hashes.
		hash += 31 * rodPitch;
		hash += 31 * rodManager.hashCode();

		return hash;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Deep copies the contents of the object from another object.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherObject
	 *            <p>
	 *            The object to be copied from.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void copy(ReflectorAssembly otherObject) {
		// begin-user-code

		// Check the parameters.
		if (otherObject == null) {
			return;
		}
		// Copy the super's values.
		super.copy(otherObject);

		// Copy the local values.
		rodPitch = otherObject.rodPitch;
		rodManager.copy(otherObject.rodManager);

		return;
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

		// Initialize a new object.
		ReflectorAssembly object = new ReflectorAssembly(getSize());

		// Copy the contents from this one.
		object.copy(this);

		// Return the newly instantiated object.
		return object;
		// end-user-code
	}

	/**
	 * Overrides the default behavior (ignore) from SFRComponent and implements
	 * the accept operation for this SFRComponent's type.
	 */
	@Override
	public void accept(ISFRComponentVisitor visitor) {
		// begin-user-code

		if (visitor != null) {
			visitor.visit(this);
		}
		return;
		// end-user-code
	}
}