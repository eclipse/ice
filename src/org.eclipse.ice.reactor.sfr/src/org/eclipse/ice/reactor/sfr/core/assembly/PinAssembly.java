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

import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.reactor.sfr.base.GridDataManager;
import org.eclipse.ice.reactor.sfr.base.ISFRComponentVisitor;
import org.eclipse.ice.reactor.sfr.base.SFRComponent;
import org.eclipse.ice.reactor.sfr.core.AssemblyType;

import java.util.ArrayList;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Class representing any assemblies in a SFR that contain pins. This includes
 * both fuel pins (core and blanket), in addition to control assemblies (primary
 * or secondary/shutdown), test assemblies, and shield assemblies. The
 * distinction between the pin assembly type is made using the pinType
 * attribute.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author w5q
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class PinAssembly extends SFRAssembly {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Shortest distance between centers of adjacent pins.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private double pinPitch;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The PinType of the pin assembly; can be either core fuel, blanket fuel,
	 * primary control or secondary control.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private PinType pinType;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The physical size of the inner duct within the assembly. Defined as the
	 * distance from one outer duct surface to the outer surface parallel to it.
	 * Since control assemblies (PrimaryControl, SecondaryControl) are the only
	 * PinAssembly type with inner ducts, this attribute will be 0 for all other
	 * PinAssembly types.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private double innerDuctFlatToFlat;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Thickness of the assembly's inner duct wall. Since control assemblies
	 * (PrimaryControl, SecondaryControl) are the only PinAssembly type with
	 * inner ducts, this attribute will be 0 for all other PinAssembly types.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private double innerDuctThickness;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A GridManager used to manage the locations of pins within this assembly.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private GridDataManager pinManager;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Parameterized constructor with the assembly size (number of pins)
	 * specified.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param size
	 *            The size (number of pins) of the assembly.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public PinAssembly(int size) {
		// begin-user-code

		// Call the super constructor with some defaults.
		super("SFR Pin Assembly 1", AssemblyType.Fuel, size);

		// Set the default description and ID.
		setDescription("SFR Pin Assembly 1's Description");
		setId(1);

		// Set the default pin type.
		pinType = PinType.InnerFuel;

		// Initialize pinPitch and innerDuct values
		pinPitch = 1.0;
		innerDuctFlatToFlat = 0.0;
		innerDuctThickness = 0.0;
		// Inner duct values are assumed to be 0, except for the case of control
		// assemblies. Since the nullary constructor sets the pin's type as
		// InnerFuel,
		// the default inner duct is non-existent.

		// Initialize the pin manager.
		pinManager = new GridDataManager(getSize() * getSize());

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Parameterized constructor with the assembly size (number of pins), pin
	 * type, and name specified.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param name
	 *            The name of the assembly.
	 * @param pinType
	 *            The type of the pin.
	 * @param size
	 *            The size (number of pins) of the assembly.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public PinAssembly(String name, PinType pinType, int size) {
		// begin-user-code

		// Call the basic constructor. Sets all defaults.
		this(size);

		// Set the name.
		setName(name);

		// Set the pin type of this assembly. If null, default to InnerFuel.
		this.pinType = (pinType != null ? pinType : PinType.InnerFuel);

		// Initialize the inner duct values; assumed to be 0 unless a control
		// assembly
		if (this.pinType == PinType.PrimaryControl
				|| this.pinType == PinType.SecondaryControl) {
			innerDuctFlatToFlat = 0.75;
			innerDuctThickness = 0.05;
		} else {
			innerDuctFlatToFlat = 0.0;
			innerDuctThickness = 0.0;
		}

		// Set the assembly type to Control, Shield or Test if necessary (is
		// already Fuel by default)
		switch (this.pinType) {
		case PrimaryControl:
		case SecondaryControl:
			assemblyType = AssemblyType.Control;
			break;
		case Shield:
			assemblyType = AssemblyType.Shield;
			break;
		case MaterialTest:
		case FuelTest:
			assemblyType = AssemblyType.Test;
			break;
		case InnerFuel:
		case OuterFuel:
		case BlanketFuel:
		default:
			break;
		}

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the pin pitch (shortest distance between a pin center to an adjacent
	 * pin center).
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param pinPitch
	 *            The pin pitch.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setPinPitch(double pinPitch) {
		// begin-user-code

		// Only set the pin pitch if it is 0 or greater.
		if (pinPitch >= 0.0) {
			this.pinPitch = pinPitch;
		}
		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the pin pitch (shortest distance between a pin center to an
	 * adjacent pin center) as a double.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return Returns the pin pitch as a double.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public double getPinPitch() {
		// begin-user-code
		return pinPitch;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the inner duct's (outer) flat-to-flat distance.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param flatToFlat
	 *            The inner duct's (outer) flat-to-flat distance.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setInnerDuctFlatToFlat(double flatToFlat) {
		// begin-user-code

		// Only set the flat-to-flat if it is 0 or greater.
		if (flatToFlat >= 0.0) {
			this.innerDuctFlatToFlat = flatToFlat;
		}
		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the inner duct's (outer) flat-to-flat distance.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return The inner duct's (outer) flat-to-flat distance.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public double getInnerDuctFlatToFlat() {
		// begin-user-code
		return innerDuctFlatToFlat;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the inner duct's thickness.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param innerDuctThickness
	 *            The inner duct's thickness.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setInnerDuctThickness(double innerDuctThickness) {
		// begin-user-code

		// Only set the thickness if it is 0 or greater.
		if (innerDuctThickness >= 0.0) {
			this.innerDuctThickness = innerDuctThickness;
		}
		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the inner duct's thickness.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return The inner duct's thickness.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public double getInnerDuctThickness() {
		// begin-user-code
		return innerDuctThickness;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the pin type (primary control, secondary control, core fuel or
	 * blanket fuel).
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return Returns the pin type.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public PinType getPinType() {
		// begin-user-code
		return pinType;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Adds the specified SFRPin to the assembly; returns true if the operation
	 * was successful.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param pin
	 *            The pin to be added to the PinAssembly.
	 * @return Returns true if the operation was successful, otherwise false.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean addPin(SFRPin pin) {
		// begin-user-code

		// By default, we did not succeed in adding the Component.
		boolean success = false;

		// Check the parameters. Also make sure that the pin does not already
		// exist in the collection of Components.
		if (pin != null && !pin.equals(getComponent(pin.getName()))) {

			// Add the new Component to this Composite.
			super.addComponent(pin);

			// See if the pin was successfully added.
			success = pin.equals(getComponent(pin.getName()));
		}

		return success;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Adds the pin with the specified name to the assembly in the specified
	 * location. If the pin exists and the location is valid and is not occupied
	 * by the same pin, this returns true.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param name
	 *            The name of the pin to set. The name must correspond to an
	 *            existing pin.
	 * @param row
	 *            The row in which to put the pin.
	 * @param column
	 *            The column in which to put the pin.
	 * @return Returns whether or not the pin location was successfully set.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean setPinLocation(String name, int row, int column) {
		// begin-user-code

		// By default, we did not succeed in adding the Component.
		boolean success = false;

		// Check the parameters.
		// If the Component exists, add it to the GridManager location.
		int size = getSize();
		if (name != null && row >= 0 && row < size && column >= 0
				&& column < size && getComponent(name) != null) {
			success = pinManager.addComponent(name, row * size + column);
		}

		return success;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Removes the specified SFRPin from the assembly; returns true if the
	 * operation was successful.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param name
	 *            The name of the pin to be removed.
	 * @return Returns true if the operation was successful, otherwise false.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean removePin(String name) {
		// begin-user-code

		// By default, we did not succeed in removing the Component.
		boolean success = false;

		// Check the parameters.
		// If there is a pin with that name, remove it.
		if (name != null && getPinByName(name) != null) {
			// Remove the Component from this Composite.
			super.removeComponent(name);

			// Remove it from the GridManager.
			pinManager.removeComponent(name);

			success = true;

		}

		return success;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Removes the SFRPin from the specified location (x, y coordinates);
	 * returns true if operation was successful.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param row
	 *            The row in which the pin can be found.
	 * @param column
	 *            The column in which the pin can be found.
	 * @return Returns true if the operation was successful, otherwise false.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean removePinFromLocation(int row, int column) {
		// begin-user-code

		// By default, we did not succeed in removing the Component from the
		// location.
		boolean success = false;

		// Check the parameters.
		int size = getSize();
		if (row >= 0 && row < size && column >= 0 && column < size) {

			// Try removing the location from the GridManager.
			success = pinManager.removeComponent(row * size + column);
		}

		return success;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns an ArrayList of SFRPin names in the assembly.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return An ArrayList of pin names contained in the assembly.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<String> getPinNames() {
		// begin-user-code
		return getComponentNames();
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the SFRPin by the specified name.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param name
	 *            The name of the pin being searched for.
	 * @return Returns the pin corresponding to the name specified. Returns a
	 *         null pin if no match was found.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public SFRPin getPinByName(String name) {
		// begin-user-code
		return (SFRPin) getComponent(name);
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the SFRPin at the specified location (x, y coordinates) in the
	 * assembly.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param row
	 *            The row in which the pin is located.
	 * @param column
	 *            The column in which the pin is located.
	 * @return Returns the pin at the specified location. Returns a null pin if
	 *         no match was found.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public SFRPin getPinByLocation(int row, int column) {
		// begin-user-code

		// Initialize the default return value.
		SFRPin pin = null;

		// Check the parameters.
		int size = getSize();
		if (row >= 0 && row < size && column >= 0 && column < size) {

			// Get the name of the Component from the GridManager.
			String name = pinManager.getComponentName(row * size + column);

			// Get the Component from this Composite.
			pin = getPinByName(name);
		}

		return pin;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns an ArrayList of locations within the assembly that are occupied
	 * by the pin matching the specified name.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param name
	 *            The name of the pin being searched for.
	 * @return Returns an ArrayList of the locations the specified pin occupies.
	 *         If no match was found, returns an empty ArrayList.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<Integer> getPinLocations(String name) {
		// begin-user-code

		// We need to return a List of Integers representing pin locations.
		ArrayList<Integer> locations;

		// Check the parameters.
		if (name != null) {
			// Get the list of locations found by the GridManager.
			locations = (ArrayList<Integer>) pinManager
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
	 * Returns the number of pins in the assembly.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return The number of pins in the assembly.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int getNumberOfPins() {
		// begin-user-code
		return getNumberOfComponents();
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns an IDataProvider for the location in the assembly. This is
	 * distinct from the SFRPin, which, as an SFRComponent, is itself an
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
			provider = pinManager.getDataProvider(row * size + column);
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
		else if (otherObject != null && otherObject instanceof PinAssembly) {

			// We can now cast the other object.
			PinAssembly assembly = (PinAssembly) otherObject;

			// Compare the values between the two objects.
			equals = (super.equals(otherObject)
					&& pinPitch == assembly.pinPitch
					&& pinType == assembly.pinType
					&& innerDuctFlatToFlat == assembly.innerDuctFlatToFlat
					&& innerDuctThickness == assembly.innerDuctThickness && pinManager
					.equals(assembly.pinManager));
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
	 * @return The hash of the object.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int hashCode() {
		// begin-user-code

		// Hash based on super's hashCode.
		int hash = super.hashCode();

		// Add local hashes.
		hash += 31 * pinPitch;
		hash += 31 * pinType.hashCode();
		hash += 31 * pinManager.hashCode();

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
	 *            The object to be copied from.
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void copy(PinAssembly otherObject) {
		// begin-user-code

		// Check the parameters.
		if (otherObject == null) {
			return;
		}
		// Copy the super's values.
		super.copy(otherObject);

		// Copy the local values.
		pinPitch = otherObject.pinPitch;
		pinType = otherObject.pinType;
		innerDuctFlatToFlat = otherObject.innerDuctFlatToFlat;
		innerDuctThickness = otherObject.innerDuctThickness;
		pinManager.copy(otherObject.pinManager);

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
		PinAssembly object = new PinAssembly(getSize());

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