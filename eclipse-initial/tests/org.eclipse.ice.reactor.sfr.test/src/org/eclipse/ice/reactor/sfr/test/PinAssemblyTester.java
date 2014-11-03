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
package org.eclipse.ice.reactor.sfr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.ice.reactor.sfr.base.SFRComponent;
import org.eclipse.ice.reactor.sfr.base.SFRComposite;
import org.eclipse.ice.reactor.sfr.base.SFRData;
import org.eclipse.ice.reactor.sfr.core.AssemblyType;
import org.eclipse.ice.reactor.sfr.core.assembly.PinAssembly;
import org.eclipse.ice.reactor.sfr.core.assembly.PinType;
import org.eclipse.ice.reactor.sfr.core.assembly.SFRPin;
import org.junit.Test;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Tests the operations of the PinAssembly class.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author w5q
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class PinAssemblyTester {

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Tests the constructors and default values of the PinAssembly class.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkConstruction() {
		// begin-user-code

		// An assembly to test.
		PinAssembly assembly;

		// Invalid, default, and valid values to use in the constructor.
		int invalidSize = 0;
		int defaultSize = 1;
		int size = 31;

		String nullName = null;
		String emptyName = "            ";
		String defaultName = "SFR Pin Assembly 1";
		String name = "Tricia";

		PinType invalidPinType = null;
		PinType defaultPinType = PinType.InnerFuel;
		PinType pinType = PinType.BlanketFuel;

		// Other defaults.
		AssemblyType defaultType = AssemblyType.Fuel;
		String defaultDescription = "SFR Pin Assembly 1's Description";
		int defaultId = 1;
		double defaultDuctThickness = 0.0;
		double defaultPinPitch = 1.0;
		double defaultInnerDuctFlatToFlat = 0.0;
		double defaultInnerDuctThickness = 0.0;

		/* ---- Test the basic constructor. ---- */
		// Invalid parameters.
		assembly = new PinAssembly(invalidSize);

		assertEquals(defaultId, assembly.getId());
		assertEquals(defaultName, assembly.getName());
		assertEquals(defaultDescription, assembly.getDescription());
		assertEquals(defaultSize, assembly.getSize());
		assertEquals(defaultType, assembly.getAssemblyType());
		assertEquals(defaultPinType, assembly.getPinType());
		assertEquals(defaultDuctThickness, assembly.getDuctThickness(), 0);
		assertEquals(defaultPinPitch, assembly.getPinPitch(), 0);

		// Valid parameters.
		assembly = new PinAssembly(size);

		assertEquals(defaultId, assembly.getId());
		assertEquals(defaultName, assembly.getName());
		assertEquals(defaultDescription, assembly.getDescription());
		assertEquals(size, assembly.getSize());
		assertEquals(defaultType, assembly.getAssemblyType());
		assertEquals(defaultPinType, assembly.getPinType());
		assertEquals(defaultDuctThickness, assembly.getDuctThickness(), 0);
		assertEquals(defaultPinPitch, assembly.getPinPitch(), 0);
		assertEquals(defaultInnerDuctFlatToFlat,
				assembly.getInnerDuctFlatToFlat(), 0);
		assertEquals(defaultInnerDuctThickness,
				assembly.getInnerDuctThickness(), 0);
		/* ------------------------------------- */

		/* ---- Test the second constructor. ---- */
		// Invalid parameters.

		// All invalid.
		assembly = new PinAssembly(nullName, invalidPinType, invalidSize);

		assertEquals(defaultId, assembly.getId());
		assertEquals(defaultName, assembly.getName());
		assertEquals(defaultDescription, assembly.getDescription());
		assertEquals(defaultSize, assembly.getSize());
		assertEquals(defaultType, assembly.getAssemblyType());
		assertEquals(defaultPinType, assembly.getPinType());
		assertEquals(defaultDuctThickness, assembly.getDuctThickness(), 0);
		assertEquals(defaultPinPitch, assembly.getPinPitch(), 0);
		assertEquals(defaultInnerDuctFlatToFlat,
				assembly.getInnerDuctFlatToFlat(), 0);
		assertEquals(defaultInnerDuctThickness,
				assembly.getInnerDuctThickness(), 0);

		// Name invalid.
		assembly = new PinAssembly(emptyName, pinType, size);

		assertEquals(defaultId, assembly.getId());
		assertEquals(defaultName, assembly.getName());
		assertEquals(defaultDescription, assembly.getDescription());
		assertEquals(size, assembly.getSize());
		assertEquals(defaultType, assembly.getAssemblyType());
		assertEquals(pinType, assembly.getPinType());
		assertEquals(defaultDuctThickness, assembly.getDuctThickness(), 0);
		assertEquals(defaultPinPitch, assembly.getPinPitch(), 0);
		assertEquals(defaultInnerDuctFlatToFlat,
				assembly.getInnerDuctFlatToFlat(), 0);
		assertEquals(defaultInnerDuctThickness,
				assembly.getInnerDuctThickness(), 0);

		// Pin type invalid.
		assembly = new PinAssembly(name, invalidPinType, size);

		assertEquals(defaultId, assembly.getId());
		assertEquals(name, assembly.getName());
		assertEquals(defaultDescription, assembly.getDescription());
		assertEquals(size, assembly.getSize());
		assertEquals(defaultType, assembly.getAssemblyType());
		assertEquals(defaultPinType, assembly.getPinType());
		assertEquals(defaultDuctThickness, assembly.getDuctThickness(), 0);
		assertEquals(defaultPinPitch, assembly.getPinPitch(), 0);
		assertEquals(defaultInnerDuctFlatToFlat,
				assembly.getInnerDuctFlatToFlat(), 0);
		assertEquals(defaultInnerDuctThickness,
				assembly.getInnerDuctThickness(), 0);

		// Size invalid.
		assembly = new PinAssembly(name, pinType, invalidSize);

		assertEquals(defaultId, assembly.getId());
		assertEquals(name, assembly.getName());
		assertEquals(defaultDescription, assembly.getDescription());
		assertEquals(defaultSize, assembly.getSize());
		assertEquals(defaultType, assembly.getAssemblyType());
		assertEquals(pinType, assembly.getPinType());
		assertEquals(defaultDuctThickness, assembly.getDuctThickness(), 0);
		assertEquals(defaultPinPitch, assembly.getPinPitch(), 0);
		assertEquals(defaultInnerDuctFlatToFlat,
				assembly.getInnerDuctFlatToFlat(), 0);
		assertEquals(defaultInnerDuctThickness,
				assembly.getInnerDuctThickness(), 0);

		// Valid parameters.
		assembly = new PinAssembly(name, pinType, size);

		assertEquals(defaultId, assembly.getId());
		assertEquals(name, assembly.getName());
		assertEquals(defaultDescription, assembly.getDescription());
		assertEquals(size, assembly.getSize());
		assertEquals(defaultType, assembly.getAssemblyType());
		assertEquals(pinType, assembly.getPinType());
		assertEquals(defaultDuctThickness, assembly.getDuctThickness(), 0);
		assertEquals(defaultPinPitch, assembly.getPinPitch(), 0);
		assertEquals(defaultInnerDuctFlatToFlat,
				assembly.getInnerDuctFlatToFlat(), 0);
		assertEquals(defaultInnerDuctThickness,
				assembly.getInnerDuctThickness(), 0);
		/* -------------------------------------- */

		/* ---- Test all PinTypes. ---- */
		// InnerFuel
		AssemblyType assemblyType = AssemblyType.Fuel;
		pinType = PinType.InnerFuel;

		assembly = new PinAssembly(name, pinType, size);

		assertEquals(defaultId, assembly.getId());
		assertEquals(name, assembly.getName());
		assertEquals(defaultDescription, assembly.getDescription());
		assertEquals(size, assembly.getSize());
		assertEquals(assemblyType, assembly.getAssemblyType());
		assertEquals(pinType, assembly.getPinType());
		assertEquals(defaultDuctThickness, assembly.getDuctThickness(), 0);
		assertEquals(defaultPinPitch, assembly.getPinPitch(), 0);
		assertEquals(defaultInnerDuctFlatToFlat,
				assembly.getInnerDuctFlatToFlat(), 0);
		assertEquals(defaultInnerDuctThickness,
				assembly.getInnerDuctThickness(), 0);

		// OuterFuel
		assemblyType = AssemblyType.Fuel;
		pinType = PinType.OuterFuel;

		assembly = new PinAssembly(name, pinType, size);

		assertEquals(defaultId, assembly.getId());
		assertEquals(name, assembly.getName());
		assertEquals(defaultDescription, assembly.getDescription());
		assertEquals(size, assembly.getSize());
		assertEquals(assemblyType, assembly.getAssemblyType());
		assertEquals(pinType, assembly.getPinType());
		assertEquals(defaultDuctThickness, assembly.getDuctThickness(), 0);
		assertEquals(defaultPinPitch, assembly.getPinPitch(), 0);
		assertEquals(defaultInnerDuctFlatToFlat,
				assembly.getInnerDuctFlatToFlat(), 0);
		assertEquals(defaultInnerDuctThickness,
				assembly.getInnerDuctThickness(), 0);

		// BlanketFuel
		assemblyType = AssemblyType.Fuel;
		pinType = PinType.BlanketFuel;

		assembly = new PinAssembly(name, pinType, size);

		assertEquals(defaultId, assembly.getId());
		assertEquals(name, assembly.getName());
		assertEquals(defaultDescription, assembly.getDescription());
		assertEquals(size, assembly.getSize());
		assertEquals(assemblyType, assembly.getAssemblyType());
		assertEquals(pinType, assembly.getPinType());
		assertEquals(defaultDuctThickness, assembly.getDuctThickness(), 0);
		assertEquals(defaultPinPitch, assembly.getPinPitch(), 0);
		assertEquals(defaultInnerDuctFlatToFlat,
				assembly.getInnerDuctFlatToFlat(), 0);
		assertEquals(defaultInnerDuctThickness,
				assembly.getInnerDuctThickness(), 0);

		// PrimaryControl
		assemblyType = AssemblyType.Control;
		pinType = PinType.PrimaryControl;

		assembly = new PinAssembly(name, pinType, size);

		assertEquals(defaultId, assembly.getId());
		assertEquals(name, assembly.getName());
		assertEquals(defaultDescription, assembly.getDescription());
		assertEquals(size, assembly.getSize());
		assertEquals(assemblyType, assembly.getAssemblyType());
		assertEquals(pinType, assembly.getPinType());
		assertEquals(defaultDuctThickness, assembly.getDuctThickness(), 0);
		assertEquals(defaultPinPitch, assembly.getPinPitch(), 0);
		assertEquals(0.75, assembly.getInnerDuctFlatToFlat(), 0);
		assertEquals(0.05, assembly.getInnerDuctThickness(), 0);

		// SecondaryControl
		assemblyType = AssemblyType.Control;
		pinType = PinType.SecondaryControl;

		assembly = new PinAssembly(name, pinType, size);

		assertEquals(defaultId, assembly.getId());
		assertEquals(name, assembly.getName());
		assertEquals(defaultDescription, assembly.getDescription());
		assertEquals(size, assembly.getSize());
		assertEquals(assemblyType, assembly.getAssemblyType());
		assertEquals(pinType, assembly.getPinType());
		assertEquals(defaultDuctThickness, assembly.getDuctThickness(), 0);
		assertEquals(defaultPinPitch, assembly.getPinPitch(), 0);
		assertEquals(0.75, assembly.getInnerDuctFlatToFlat(), 0);
		assertEquals(0.05, assembly.getInnerDuctThickness(), 0);
		/* ------------------------------ */

		// Shield
		assemblyType = AssemblyType.Shield;
		pinType = PinType.Shield;

		assembly = new PinAssembly(name, pinType, size);

		assertEquals(defaultId, assembly.getId());
		assertEquals(name, assembly.getName());
		assertEquals(defaultDescription, assembly.getDescription());
		assertEquals(size, assembly.getSize());
		assertEquals(assemblyType, assembly.getAssemblyType());
		assertEquals(pinType, assembly.getPinType());
		assertEquals(defaultDuctThickness, assembly.getDuctThickness(), 0);
		assertEquals(defaultPinPitch, assembly.getPinPitch(), 0);
		assertEquals(defaultInnerDuctFlatToFlat,
				assembly.getInnerDuctFlatToFlat(), 0);
		assertEquals(defaultInnerDuctThickness,
				assembly.getInnerDuctThickness(), 0);
		/* ------------------------------ */

		// MaterialTest
		assemblyType = AssemblyType.Test;
		pinType = PinType.MaterialTest;

		assembly = new PinAssembly(name, pinType, size);

		assertEquals(defaultId, assembly.getId());
		assertEquals(name, assembly.getName());
		assertEquals(defaultDescription, assembly.getDescription());
		assertEquals(size, assembly.getSize());
		assertEquals(assemblyType, assembly.getAssemblyType());
		assertEquals(pinType, assembly.getPinType());
		assertEquals(defaultDuctThickness, assembly.getDuctThickness(), 0);
		assertEquals(defaultPinPitch, assembly.getPinPitch(), 0);
		assertEquals(defaultInnerDuctFlatToFlat,
				assembly.getInnerDuctFlatToFlat(), 0);
		assertEquals(defaultInnerDuctThickness,
				assembly.getInnerDuctThickness(), 0);
		/* ------------------------------ */

		// FuelTest
		assemblyType = AssemblyType.Test;
		pinType = PinType.FuelTest;

		assembly = new PinAssembly(name, pinType, size);

		assertEquals(defaultId, assembly.getId());
		assertEquals(name, assembly.getName());
		assertEquals(defaultDescription, assembly.getDescription());
		assertEquals(size, assembly.getSize());
		assertEquals(assemblyType, assembly.getAssemblyType());
		assertEquals(pinType, assembly.getPinType());
		assertEquals(defaultDuctThickness, assembly.getDuctThickness(), 0);
		assertEquals(defaultPinPitch, assembly.getPinPitch(), 0);
		assertEquals(defaultInnerDuctFlatToFlat,
				assembly.getInnerDuctFlatToFlat(), 0);
		assertEquals(defaultInnerDuctThickness,
				assembly.getInnerDuctThickness(), 0);
		/* ------------------------------ */

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Tests the getter and setter for the pinPitch attribute.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkPinPitch() {
		// begin-user-code
		PinAssembly assembly = new PinAssembly(1);
		double defaultPinPitch = 1.0;

		// Check the default.
		assertEquals(defaultPinPitch, assembly.getPinPitch(), 0);

		// Set it to 1 and check it.
		assembly.setPinPitch(1.0);
		assertEquals(1.0, assembly.getPinPitch(), 0);

		// Set it to an invalid number.
		assembly.setPinPitch(-1.0);
		assertEquals(1.0, assembly.getPinPitch(), 0);

		// Set it to a valid number.
		assembly.setPinPitch(500);
		assertEquals(500, assembly.getPinPitch(), 0);

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Tests the getter and setter for the inner duct attributes.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkInnerDuct() {
		// begin-user-code
		PinAssembly assembly = new PinAssembly(1);
		double defaultInnerDuctFlatToFlat = 0.0;
		double defaultInnerDuctThickness = 0.0;

		// Check the default.
		assertEquals(defaultInnerDuctFlatToFlat,
				assembly.getInnerDuctFlatToFlat(), 0);
		assertEquals(defaultInnerDuctThickness,
				assembly.getInnerDuctThickness(), 0);

		// Set inner duct flat-to-flat to 1 and check it.
		assembly.setInnerDuctFlatToFlat(1.0);
		assertEquals(1.0, assembly.getInnerDuctFlatToFlat(), 0);

		// Set inner duct flat-to-flat to an invalid number.
		assembly.setInnerDuctFlatToFlat(-1.0);
		assertEquals(1.0, assembly.getInnerDuctFlatToFlat(), 0);

		// Set inner duct flat-to-flat to a valid number.
		assembly.setInnerDuctFlatToFlat(500);
		assertEquals(500, assembly.getInnerDuctFlatToFlat(), 0);

		// Set inner duct thickness to 1 and check it.
		assembly.setInnerDuctThickness(1.0);
		assertEquals(1.0, assembly.getInnerDuctThickness(), 0);

		// Set inner duct thickness to to an invalid number.
		assembly.setInnerDuctThickness(-1.0);
		assertEquals(1.0, assembly.getInnerDuctThickness(), 0);

		// Set inner duct thickness to a valid number.
		assembly.setInnerDuctThickness(500);
		assertEquals(500, assembly.getInnerDuctThickness(), 0);

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Tests the addition and removal of SFRPins to the PinAssembly.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkPinAddRem() {
		// begin-user-code

		// Note: I only check assembly.getPinLocations() in a few spots
		// below since it's a late addition. Further testing may be required.

		int size = 10;

		// An assembly to test.
		PinAssembly assembly = new PinAssembly(10);

		// Initialize some pins to add/remove from the assembly.
		SFRPin pin1 = new SFRPin();
		pin1.setName("Anjie");
		SFRPin pin2 = new SFRPin();
		pin2.setName("Colin");
		SFRPin pin3 = new SFRPin();
		pin3.setName("Dish of the Day");

		// A List of boundary indexes to test.
		List<Integer> boundaryIndexes = new ArrayList<Integer>();
		boundaryIndexes.add(-1);
		boundaryIndexes.add(0);
		boundaryIndexes.add(size - 1);
		boundaryIndexes.add(size);

		// A set of indexes that will be empty. This makes it easier to check
		// pin locations.
		Set<Integer> emptyIndexes = new HashSet<Integer>();
		for (int i = 0; i < size * size; i++) {
			emptyIndexes.add(i);
		}
		String name;

		// Methods to test:
		// public boolean addPin(SFRPin pin)
		// public boolean removePin(String name)
		// public boolean removePinFromLocation(int row, int column)
		// public ArrayList<String> getPinNames()
		// public SFRPin getPinByName(String name)
		// public SFRPin getPinByLocation(int row, int column)
		// public boolean setPinLocation(String name, int row, int column)

		/* ---- Make sure the assembly is empty. ---- */
		// Check invalid locations.
		for (int row : boundaryIndexes) {
			for (int column : boundaryIndexes) {
				if (row < 0 || row == size || column < 0 || column == size) {
					assertNull(assembly.getPinByLocation(row, column));
				}
			}
		}

		// Check all valid locations.
		for (int i : emptyIndexes) {
			assertNull(assembly.getPinByLocation(i / size, i % size));
		}
		/* ------------------------------------------ */

		/* ---- Try various argument combinations for each method. ---- */
		name = pin1.getName();

		// Verify that there is no pin set.
		assertNull(assembly.getPinByName(name));
		assertEquals(0, assembly.getNumberOfPins());
		assertEquals(0, assembly.getPinNames().size());
		assertFalse(assembly.getPinNames().contains(name));

		// Check all the locations.
		for (int i : emptyIndexes) {
			assertNull(assembly.getPinByLocation(i / size, i % size));
		}
		// addPin
		assertFalse(assembly.addPin(null));

		// addPin (successful)
		assertTrue(assembly.addPin(pin1));

		// setPinLocation
		assertFalse(assembly.setPinLocation(null, -1, -1));
		assertFalse(assembly.setPinLocation(null, 0, 0));
		assertFalse(assembly.setPinLocation("Vogons", 0, 0));
		assertFalse(assembly.setPinLocation(name, -1, 0));
		assertFalse(assembly.setPinLocation(name, 0, size));

		// Check setting invalid locations.
		for (int row : boundaryIndexes) {
			for (int column : boundaryIndexes) {
				if (row < 0 || row >= size || column < 0 || column >= size) {
					assertFalse(assembly.setPinLocation(name, row, column));
					assertNull(assembly.getPinByLocation(row, column));
				}
			}
		}

		// setPinLocation (successful)
		assertTrue(assembly.setPinLocation(name, 0, 0));

		// getNumberOfPins (successful)
		assertEquals(1, assembly.getNumberOfPins());

		// getPinNames (successful)
		assertEquals(1, assembly.getPinNames().size());
		assertTrue(assembly.getPinNames().contains(name));

		// getPinByName
		assertNull(assembly.getPinByName(null));
		assertNull(assembly.getPinByName("Vogons"));

		// getPinByName (successful)
		assertEquals(pin1, assembly.getPinByName(name));

		// getPinByLocation
		assertNull(assembly.getPinByLocation(-1, -1));
		assertNull(assembly.getPinByLocation(-1, 0));
		assertNull(assembly.getPinByLocation(0, size));

		// getPinByLocation (successful)
		assertEquals(pin1, assembly.getPinByLocation(0, 0));

		// getPinLocations
		assertTrue(assembly.getPinLocations(null).isEmpty());
		assertTrue(assembly.getPinLocations("Vogons").isEmpty());

		// getPinLocations (successful)
		assertEquals(1, assembly.getPinLocations(name).size());
		assertTrue(assembly.getPinLocations(name).contains(0));

		// removePinFromLocation
		assertFalse(assembly.removePinFromLocation(-1, -1));
		assertFalse(assembly.removePinFromLocation(-1, 0));
		assertFalse(assembly.removePinFromLocation(0, size));

		// removePinFromLocation (successful)
		assertTrue(assembly.removePinFromLocation(0, 0));

		// removePin
		assertFalse(assembly.removePin(null));
		assertFalse(assembly.removePin("Vogons"));

		// removePin (successful)
		assertTrue(assembly.removePin(name));

		// Verify that the pin has been completely removed.
		assertNull(assembly.getPinByName(name));
		assertEquals(0, assembly.getNumberOfPins());
		assertEquals(0, assembly.getPinNames().size());
		assertFalse(assembly.getPinNames().contains(name));

		// Check all the locations.
		for (int i : emptyIndexes) {
			assertNull(assembly.getPinByLocation(i / size, i % size));
		}
		/* ------------------------------------------------------------ */

		/* ---- Add an pin and set some locations. ---- */
		name = pin1.getName();
		assertTrue(assembly.addPin(pin1));
		assertFalse(assembly.addPin(pin1));

		// Verify that the pin was added.
		assertEquals(pin1, assembly.getPinByName(name));
		assertEquals(1, assembly.getNumberOfPins());
		assertEquals(1, assembly.getPinNames().size());
		assertTrue(assembly.getPinNames().contains(name));

		// All locations should be empty.
		for (int i : emptyIndexes) {
			assertNull(assembly.getPinByLocation(i / size, i % size));
		}
		assertTrue(assembly.getPinLocations(name).isEmpty());

		// The first attempt to set a location should succeed. Subsequent
		// attempts to set the same pin in the same location should fail.

		// Put it in the first spot.
		assertTrue(assembly.setPinLocation(name, 0, 0));
		assertFalse(assembly.setPinLocation(name, 0, 0));
		emptyIndexes.remove(0);

		// Put it in a middle spot.
		assertTrue(assembly.setPinLocation(name, 0, 6));
		assertFalse(assembly.setPinLocation(name, 0, 6));
		emptyIndexes.remove(6);

		// Put it in a middle spot.
		assertTrue(assembly.setPinLocation(name, 3, 7));
		assertFalse(assembly.setPinLocation(name, 3, 7));
		emptyIndexes.remove(3 * size + 7);

		// Put it in the last spot.
		assertTrue(assembly.setPinLocation(name, size - 1, size - 1));
		assertFalse(assembly.setPinLocation(name, size - 1, size - 1));
		emptyIndexes.remove(size * size - 1);

		// Verify the pin locations.
		assertEquals(pin1, assembly.getPinByLocation(0, 0));
		assertEquals(pin1, assembly.getPinByLocation(0, 6));
		assertEquals(pin1, assembly.getPinByLocation(3, 7));
		assertEquals(pin1, assembly.getPinByLocation(size - 1, size - 1));
		for (int i : emptyIndexes) {
			assertNull(assembly.getPinByLocation(i / size, i % size));
		}
		// We should still be able to get the pin by location.
		assertEquals(pin1, assembly.getPinByLocation(0, 0));

		// Verify the pin locations (getPinLocations()).
		assertEquals(4, assembly.getPinLocations(name).size());
		assertTrue(assembly.getPinLocations(name).contains(0));
		assertTrue(assembly.getPinLocations(name).contains(0 * size + 6));
		assertTrue(assembly.getPinLocations(name).contains(3 * size + 7));
		assertTrue(assembly.getPinLocations(name).contains(size * size - 1));
		/* -------------------------------------------- */

		/* ---- Test overriding a pin location. ---- */
		name = pin2.getName();
		assertTrue(assembly.addPin(pin2));
		assertFalse(assembly.addPin(pin2));

		// Verify that the pin was added.
		assertEquals(pin2, assembly.getPinByName(name));
		assertEquals(2, assembly.getNumberOfPins());
		assertEquals(2, assembly.getPinNames().size());
		assertTrue(assembly.getPinNames().contains(pin1.getName()));
		assertTrue(assembly.getPinNames().contains(name));

		// Verify the pin locations.
		assertEquals(pin1, assembly.getPinByLocation(0, 0));
		assertEquals(pin1, assembly.getPinByLocation(0, 6));
		assertEquals(pin1, assembly.getPinByLocation(3, 7));
		assertEquals(pin1, assembly.getPinByLocation(size - 1, size - 1));
		for (int i : emptyIndexes) {
			assertNull(assembly.getPinByLocation(i / size, i % size));
		}
		// Put it in a new spot.
		assertTrue(assembly.setPinLocation(name, 1, 1));
		assertFalse(assembly.setPinLocation(name, 1, 1));
		emptyIndexes.remove(size + 1);

		// Put it in a spot occupied by pin1.
		assertTrue(assembly.setPinLocation(name, 3, 7));
		assertFalse(assembly.setPinLocation(name, 3, 7));

		// Verify the pin locations.
		assertEquals(pin1, assembly.getPinByLocation(0, 0));
		assertEquals(pin1, assembly.getPinByLocation(0, 6));
		assertEquals(pin1, assembly.getPinByLocation(size - 1, size - 1));
		assertEquals(pin2, assembly.getPinByLocation(1, 1));
		assertEquals(pin2, assembly.getPinByLocation(3, 7));
		for (int i : emptyIndexes) {
			assertNull(assembly.getPinByLocation(i / size, i % size));
		}
		/* ----------------------------------------- */

		/* ---- Test adding yet another pin. ---- */
		name = pin3.getName();
		assertTrue(assembly.addPin(pin3));
		assertFalse(assembly.addPin(pin3));

		// Verify that the pin was added.
		assertEquals(pin3, assembly.getPinByName(name));
		assertEquals(3, assembly.getNumberOfPins());
		assertEquals(3, assembly.getPinNames().size());
		assertTrue(assembly.getPinNames().contains(pin1.getName()));
		assertTrue(assembly.getPinNames().contains(pin2.getName()));
		assertTrue(assembly.getPinNames().contains(name));

		// Verify the pin locations.
		assertEquals(pin1, assembly.getPinByLocation(0, 0));
		assertEquals(pin1, assembly.getPinByLocation(0, 6));
		assertEquals(pin1, assembly.getPinByLocation(size - 1, size - 1));
		assertEquals(pin2, assembly.getPinByLocation(1, 1));
		assertEquals(pin2, assembly.getPinByLocation(3, 7));
		for (int i : emptyIndexes) {
			assertNull(assembly.getPinByLocation(i / size, i % size));
		}

		// Put it in a new spot.
		assertTrue(assembly.setPinLocation(name, 3, 3));
		assertFalse(assembly.setPinLocation(name, 3, 3));
		emptyIndexes.remove(3 * size + 3);

		// Verify the pin locations.
		assertEquals(pin1, assembly.getPinByLocation(0, 0));
		assertEquals(pin1, assembly.getPinByLocation(0, 6));
		assertEquals(pin1, assembly.getPinByLocation(size - 1, size - 1));
		assertEquals(pin2, assembly.getPinByLocation(1, 1));
		assertEquals(pin2, assembly.getPinByLocation(3, 7));
		assertEquals(pin3, assembly.getPinByLocation(3, 3));
		for (int i : emptyIndexes) {
			assertNull(assembly.getPinByLocation(i / size, i % size));
		}
		/* -------------------------------------- */

		/* ---- Test removing a pin from a location. ---- */
		assertTrue(assembly.removePinFromLocation(0, 6));
		assertFalse(assembly.removePinFromLocation(0, 6));
		emptyIndexes.add(6);

		// Verify the pin locations.
		assertEquals(pin1, assembly.getPinByLocation(0, 0));
		assertEquals(pin1, assembly.getPinByLocation(size - 1, size - 1));
		assertEquals(pin2, assembly.getPinByLocation(1, 1));
		assertEquals(pin2, assembly.getPinByLocation(3, 7));
		assertEquals(pin3, assembly.getPinByLocation(3, 3));
		for (int i : emptyIndexes) {
			assertNull(assembly.getPinByLocation(i / size, i % size));
		}
		// Remove pin2 entirely manually.
		assertTrue(assembly.removePinFromLocation(1, 1));
		assertFalse(assembly.removePinFromLocation(1, 1));
		emptyIndexes.add(size + 1);

		assertTrue(assembly.removePinFromLocation(3, 7));
		assertFalse(assembly.removePinFromLocation(3, 7));
		emptyIndexes.add(3 * size + 7);

		// Verify the pin locations.
		assertEquals(pin1, assembly.getPinByLocation(0, 0));
		assertEquals(pin1, assembly.getPinByLocation(size - 1, size - 1));
		assertEquals(pin3, assembly.getPinByLocation(3, 3));
		for (int i : emptyIndexes) {
			assertNull(assembly.getPinByLocation(i / size, i % size));
		}
		// Verify the pin locations (getPinLocations()).
		assertEquals(2, assembly.getPinLocations(pin1.getName()).size());
		assertTrue(assembly.getPinLocations(pin1.getName()).contains(0));
		assertTrue(assembly.getPinLocations(pin1.getName()).contains(
				size * size - 1));

		// pin2 should still be in the assembly, though.

		// Verify the pins stored in the assembly.
		assertEquals(pin1, assembly.getPinByName(pin1.getName()));
		assertEquals(pin2, assembly.getPinByName(pin2.getName()));
		assertEquals(pin3, assembly.getPinByName(pin3.getName()));
		assertEquals(3, assembly.getNumberOfPins());
		assertEquals(3, assembly.getPinNames().size());
		assertTrue(assembly.getPinNames().contains(pin1.getName()));
		assertTrue(assembly.getPinNames().contains(pin2.getName()));
		assertTrue(assembly.getPinNames().contains(pin3.getName()));
		/* ---------------------------------------------- */

		/* ---- Test removing a pin completely. ---- */
		assertTrue(assembly.removePin(pin1.getName()));
		assertFalse(assembly.removePin(pin1.getName()));

		emptyIndexes.add(0);
		emptyIndexes.add(size * size - 1);

		// Verify the pin locations.
		assertEquals(pin3, assembly.getPinByLocation(3, 3));
		for (int i : emptyIndexes) {
			assertNull(assembly.getPinByLocation(i / size, i % size));
		}
		// pin1 should not have any locations. In fact, since the pin is not in
		// the assembly, this returns an empty List.
		assertTrue(assembly.getPinLocations(pin1.getName()).isEmpty());

		// Verify the pins stored in the assembly.
		assertNull(assembly.getPinByName(pin1.getName()));
		assertEquals(pin2, assembly.getPinByName(pin2.getName()));
		assertEquals(pin3, assembly.getPinByName(pin3.getName()));
		assertEquals(2, assembly.getNumberOfPins());
		assertEquals(2, assembly.getPinNames().size());
		assertFalse(assembly.getPinNames().contains(pin1.getName()));
		assertTrue(assembly.getPinNames().contains(pin2.getName()));
		assertTrue(assembly.getPinNames().contains(pin3.getName()));
		/* ----------------------------------------- */

		/* ---- Remove everything and verify. ---- */
		assertFalse(assembly.removePin(pin1.getName()));
		assertTrue(assembly.removePin(pin2.getName()));
		assertFalse(assembly.removePin(pin2.getName()));
		assertTrue(assembly.removePin(pin3.getName()));
		assertFalse(assembly.removePin(pin3.getName()));

		// Add the last pin location back to the empty index set.
		emptyIndexes.add(size * 3 + 3);

		// Verify the pin locations.
		for (int i : emptyIndexes) {
			assertNull(assembly.getPinByLocation(i / size, i % size));
		}
		// Verify the pins stored in the assembly.
		assertNull(assembly.getPinByName(pin1.getName()));
		assertNull(assembly.getPinByName(pin2.getName()));
		assertNull(assembly.getPinByName(pin3.getName()));
		assertEquals(0, assembly.getNumberOfPins());
		assertEquals(0, assembly.getPinNames().size());
		/* --------------------------------------- */

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Tests the methods inherited from SFRComposite. Users should not be able
	 * to add SFRComponents to a PinAssembly through these methods.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkCompositeImplementation() {
		// begin-user-code

		// Tests the following methods:

		// public Component getComponent(int childId)
		// public SFRComponent getComponent(String name)
		// public ArrayList<String> getComponentNames()
		// public int getNumberOfComponents()
		// public ArrayList<Component> getComponents()

		// public void addComponent(Component child)
		// public void removeComponent(int childId)
		// public void removeComponent(String name)

		// Initialize an assembly to test.
		PinAssembly assembly = new PinAssembly(15);

		SFRComposite composite;
		SFRComponent component;
		String name = "Ford";
		int id = 1;
		int numberOfComponents = 0;

		/* ---- Check the initial state of the Composite. ---- */
		assertEquals(numberOfComponents, assembly.getNumberOfComponents());
		assertTrue(assembly.getComponents().isEmpty());
		assertTrue(assembly.getComponentNames().isEmpty());
		/* --------------------------------------------------- */

		/* ---- Make sure we cannot add components directly. ---- */
		component = new SFRComponent();
		assembly.addComponent(component);

		assertEquals(numberOfComponents, assembly.getNumberOfComponents());
		assertTrue(assembly.getComponents().isEmpty());
		assertTrue(assembly.getComponentNames().isEmpty());
		assertNull(assembly.getComponent(component.getName()));

		// Try the same with a Composite.
		composite = new SFRComposite();
		assembly.addComponent(composite);

		assertEquals(numberOfComponents, assembly.getNumberOfComponents());
		assertTrue(assembly.getComponents().isEmpty());
		assertTrue(assembly.getComponentNames().isEmpty());
		assertNull(assembly.getComponent(composite.getName()));
		/* ------------------------------------------------------ */

		/* ---- Add a component the proper way. ---- */
		// Create a valid component.
		component = new SFRPin();
		component.setName(name);

		// Add it.
		assertTrue(assembly.addPin((SFRPin) component));

		numberOfComponents = 1;

		// Verify that it's there.
		assertEquals(numberOfComponents, assembly.getNumberOfComponents());
		assertEquals(numberOfComponents, assembly.getComponents().size());
		assertTrue(assembly.getComponents().contains(component));
		assertEquals(numberOfComponents, assembly.getComponentNames().size());
		assertTrue(assembly.getComponentNames().contains(name));
		assertEquals(component, assembly.getComponent(name));
		/* ----------------------------------------- */

		/* ---- Make sure we cannot remove components directly. ---- */
		id = component.getId();

		// Try to remove it via ID.
		assembly.removeComponent(id);

		// Verify that the component is still there.
		assertEquals(numberOfComponents, assembly.getNumberOfComponents());
		assertEquals(numberOfComponents, assembly.getComponents().size());
		assertTrue(assembly.getComponents().contains(component));
		assertEquals(numberOfComponents, assembly.getComponentNames().size());
		assertTrue(assembly.getComponentNames().contains(name));
		assertEquals(component, assembly.getComponent(name));

		// Try to remove it via name.
		assembly.removeComponent(name);

		// Verify that the component is still there.
		assertEquals(numberOfComponents, assembly.getNumberOfComponents());
		assertEquals(numberOfComponents, assembly.getComponents().size());
		assertTrue(assembly.getComponents().contains(component));
		assertEquals(numberOfComponents, assembly.getComponentNames().size());
		assertTrue(assembly.getComponentNames().contains(name));
		assertEquals(component, assembly.getComponent(name));

		// Try removing an invalid component.
		assembly.removeComponent(null);

		// Verify that the component is still there.
		assertEquals(numberOfComponents, assembly.getNumberOfComponents());
		assertEquals(numberOfComponents, assembly.getComponents().size());
		assertTrue(assembly.getComponents().contains(component));
		assertEquals(numberOfComponents, assembly.getComponentNames().size());
		assertTrue(assembly.getComponentNames().contains(name));
		assertEquals(component, assembly.getComponent(name));
		/* --------------------------------------------------------- */

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Tests the equality and hashCode operations.
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	@Test
	public void checkEquality() {
		// begin-user-code

		int size = 78;

		// Initialize objects for testing.
		PinAssembly object = new PinAssembly(size);
		PinAssembly equalObject = new PinAssembly(size);
		PinAssembly unequalObject = new PinAssembly(size);

		// Set up the object and equalObject.
		SFRPin component = new SFRPin();
		component.setName("Marvin");
		SFRData subComponent = new SFRData("Depressed");
		subComponent.setValue(10);
		component.addData(subComponent, 0);

		object.addPin(new SFRPin("Ford"));
		object.addPin(new SFRPin("Zaphod"));
		object.addPin(component);

		object.setPinLocation("Ford", 0, 0);
		object.setPinLocation("Ford", 3, 4);
		object.setPinLocation("Zaphod", 18, 35);
		object.setPinLocation("Zaphod", 15, 36);
		object.setPinLocation("Zaphod", 76, 77);
		object.setPinLocation("Marvin", 13, 13);
		object.setPinLocation("Marvin", 14, 14);
		object.setPinLocation("Marvin", 15, 15);

		component = new SFRPin();
		component.setName("Marvin");
		subComponent = new SFRData("Depressed");
		subComponent.setValue(10);
		component.addData(subComponent, 0);

		equalObject.addPin(new SFRPin("Ford"));
		equalObject.addPin(new SFRPin("Zaphod"));
		equalObject.addPin(component);

		equalObject.setPinLocation("Ford", 0, 0);
		equalObject.setPinLocation("Ford", 3, 4);
		equalObject.setPinLocation("Zaphod", 18, 35);
		equalObject.setPinLocation("Zaphod", 15, 36);
		equalObject.setPinLocation("Zaphod", 76, 77);
		equalObject.setPinLocation("Marvin", 13, 13);
		equalObject.setPinLocation("Marvin", 14, 14);
		equalObject.setPinLocation("Marvin", 15, 15);

		// Set up the unequalObject.
		component = new SFRPin();
		component.setName("Marvin");
		subComponent = new SFRData("Depressed");
		subComponent.setValue(9); // Different!
		component.addData(subComponent, 0);

		unequalObject.addPin(new SFRPin("Ford"));
		unequalObject.addPin(new SFRPin("Zaphod"));
		unequalObject.addPin(component);

		unequalObject.setPinLocation("Ford", 0, 0);
		unequalObject.setPinLocation("Ford", 3, 4);
		unequalObject.setPinLocation("Zaphod", 18, 35);
		unequalObject.setPinLocation("Zaphod", 15, 36);
		unequalObject.setPinLocation("Zaphod", 76, 77);
		unequalObject.setPinLocation("Marvin", 13, 13);
		unequalObject.setPinLocation("Marvin", 14, 14);
		unequalObject.setPinLocation("Marvin", 15, 15);

		// Make sure the references are different.
		assertFalse(object == equalObject);
		assertFalse(object == unequalObject);
		assertFalse(equalObject == unequalObject);

		// Check that equality is reflexive and symmetric.
		assertTrue(object.equals(object));
		assertTrue(object.equals(equalObject));
		assertTrue(equalObject.equals(object));

		// Check that equals will fail when it should.
		assertFalse(object == null);
		assertFalse(object.equals(42));
		assertFalse("just a string".equals(object));
		assertFalse(object.equals(unequalObject));
		assertFalse(unequalObject.equals(object));

		// Check the hash codes.
		assertTrue(object.hashCode() == object.hashCode());
		assertTrue(object.hashCode() == equalObject.hashCode());
		assertFalse(object.hashCode() == unequalObject.hashCode());

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Tests the copying and cloning operations.
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	@Test
	public void checkCopying() {
		// begin-user-code

		int size = 79;

		// Initialize objects for testing.
		PinAssembly object = new PinAssembly(size);
		PinAssembly copy = new PinAssembly(size);
		PinAssembly clone = null;

		// Set up the object.
		SFRPin component = new SFRPin();
		component.setName("Marvin");
		SFRData subComponent = new SFRData("Depressed");
		subComponent.setValue(10);
		component.addData(subComponent, 0);

		object.addPin(new SFRPin("Ford"));
		object.addPin(new SFRPin("Zaphod"));
		object.addPin(component);

		object.setPinLocation("Ford", 0, 0);
		object.setPinLocation("Ford", 3, 4);
		object.setPinLocation("Zaphod", 18, 35);
		object.setPinLocation("Zaphod", 15, 36);
		object.setPinLocation("Zaphod", 76, 77);
		object.setPinLocation("Marvin", 13, 13);
		object.setPinLocation("Marvin", 14, 14);
		object.setPinLocation("Marvin", 15, 15);

		// Make sure the objects are not equal before copying.
		assertFalse(object == copy);
		assertFalse(object.equals(copy));

		// Copy the object.
		copy.copy(object);

		// Make sure the references are different but contents the same.
		assertFalse(object == copy);
		assertTrue(object.equals(copy));

		// Do the same for the clone operation.

		// Make sure the objects are not equal before copying.
		assertFalse(object == clone);
		assertFalse(object.equals(clone));

		// Copy the object.
		clone = (PinAssembly) object.clone();

		// Make sure the references are different but contents the same.
		assertFalse(object == clone);
		assertTrue(object.equals(clone));
		assertFalse(copy == clone);
		assertTrue(copy.equals(clone));

		return;
		// end-user-code
	}
}