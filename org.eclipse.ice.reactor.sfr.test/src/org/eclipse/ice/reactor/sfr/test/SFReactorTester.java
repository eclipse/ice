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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ice.reactor.sfr.base.SFRComponent;
import org.eclipse.ice.reactor.sfr.base.SFRComposite;
import org.eclipse.ice.reactor.sfr.base.SFRData;
import org.eclipse.ice.reactor.sfr.core.AssemblyType;
import org.eclipse.ice.reactor.sfr.core.SFReactor;
import org.eclipse.ice.reactor.sfr.core.assembly.PinAssembly;
import org.eclipse.ice.reactor.sfr.core.assembly.PinType;
import org.eclipse.ice.reactor.sfr.core.assembly.ReflectorAssembly;
import org.eclipse.ice.reactor.sfr.core.assembly.SFRAssembly;
import org.eclipse.ice.reactor.sfr.core.assembly.SFRPin;
import org.eclipse.ice.reactor.sfr.core.assembly.SFRRod;
import org.junit.Test;

/**
 * <p>
 * Tests the operations of the SFReactor class.
 * </p>
 * 
 * @author Anna Wojtowicz
 */
public class SFReactorTester {
	/**
	 * <p>
	 * Tests the constructors and default values of the SFReactor class.
	 * </p>
	 * 
	 */
	@Test
	public void checkConstruction() {

		// We have the following constructors to test:
		// new SFReactor(int size)

		int defaultSize = 1;
		String defaultName = "SFReactor 1";
		String defaultDescription = "SFReactor 1's Description";
		int defaultId = 1;
		double defaultLatticePitch = 1.0;
		double defaultOuterFlatToFlat = 1.0;
		int defaultNumberOfAssemblies = 0;

		int size = 15;

		SFReactor reactor;

		/* ---- Test a valid construction (size = 15 > 1). ---- */
		// Initialize the reactor.
		reactor = new SFReactor(size);

		// Check non-default values.
		assertEquals(size, reactor.getSize());

		// Check default values.
		assertEquals(defaultName, reactor.getName());
		assertEquals(defaultDescription, reactor.getDescription());
		assertEquals(defaultId, reactor.getId());
		assertEquals(defaultLatticePitch, reactor.getLatticePitch(), 0);
		assertEquals(defaultOuterFlatToFlat, reactor.getOuterFlatToFlat(), 0);
		for (AssemblyType type : AssemblyType.values()) {
			assertEquals(defaultNumberOfAssemblies,
					reactor.getNumberOfAssemblies(type));
			assertNotNull(reactor.getAssemblyNames(type));
			assertTrue(reactor.getAssemblyNames(type).isEmpty());
		}
		/* ---------------------------------------------------- */

		/* ---- Test an valid construction (size = 1 = 1). ---- */
		// Initialize the reactor.
		reactor = new SFReactor(defaultSize);

		// Check default values.
		assertEquals(defaultSize, reactor.getSize());
		assertEquals(defaultName, reactor.getName());
		assertEquals(defaultDescription, reactor.getDescription());
		assertEquals(defaultId, reactor.getId());
		assertEquals(defaultLatticePitch, reactor.getLatticePitch(), 0);
		assertEquals(defaultOuterFlatToFlat, reactor.getOuterFlatToFlat(), 0);
		for (AssemblyType type : AssemblyType.values()) {
			assertEquals(defaultNumberOfAssemblies,
					reactor.getNumberOfAssemblies(type));
			assertNotNull(reactor.getAssemblyNames(type));
			assertTrue(reactor.getAssemblyNames(type).isEmpty());
		}
		/* ----------------------------------------------------- */

		/* ---- Test an invalid construction (size = 0 < 1). ---- */
		// Initialize the reactor.
		reactor = new SFReactor(0);

		// Check default values.
		assertEquals(defaultSize, reactor.getSize());
		assertEquals(defaultName, reactor.getName());
		assertEquals(defaultDescription, reactor.getDescription());
		assertEquals(defaultId, reactor.getId());
		assertEquals(defaultLatticePitch, reactor.getLatticePitch(), 0);
		assertEquals(defaultOuterFlatToFlat, reactor.getOuterFlatToFlat(), 0);
		for (AssemblyType type : AssemblyType.values()) {
			assertEquals(defaultNumberOfAssemblies,
					reactor.getNumberOfAssemblies(type));
			assertNotNull(reactor.getAssemblyNames(type));
			assertTrue(reactor.getAssemblyNames(type).isEmpty());
		}
		/* ----------------------------------------------------- */

		/* ---- Test an invalid construction (size = -3 < 1). ---- */
		// Initialize the reactor.
		reactor = new SFReactor(-3);

		// Check default values.
		assertEquals(defaultSize, reactor.getSize());
		assertEquals(defaultName, reactor.getName());
		assertEquals(defaultDescription, reactor.getDescription());
		assertEquals(defaultId, reactor.getId());
		assertEquals(defaultLatticePitch, reactor.getLatticePitch(), 0);
		assertEquals(defaultOuterFlatToFlat, reactor.getOuterFlatToFlat(), 0);
		for (AssemblyType type : AssemblyType.values()) {
			assertEquals(defaultNumberOfAssemblies,
					reactor.getNumberOfAssemblies(type));
			assertNotNull(reactor.getAssemblyNames(type));
			assertTrue(reactor.getAssemblyNames(type).isEmpty());
		}
		/* ------------------------------------------------------ */

		return;
	}

	/**
	 * <p>
	 * Tests the getter and setter of SFReactor's latticePitch attribute.
	 * </p>
	 * 
	 */
	@Test
	public void checkLatticePitch() {

		// A reactor to play with.
		SFReactor reactor = new SFReactor(10);

		// Some lattice pitch values.
		double defaultLatticePitch = 1.0;
		double latticePitch = 2.0;
		double invalidLatticePitch = 0.0;

		// Check the default value.
		assertEquals(defaultLatticePitch, reactor.getLatticePitch(), 0);

		// We should be able to set a new value.
		reactor.setLatticePitch(latticePitch);
		assertEquals(latticePitch, reactor.getLatticePitch(), 0);

		// Setting to an invalid value should change nothing.
		reactor.setLatticePitch(invalidLatticePitch);
		assertEquals(latticePitch, reactor.getLatticePitch(), 0);

		// We should be able to set a new value.
		latticePitch = Double.MIN_VALUE;
		reactor.setLatticePitch(latticePitch);
		assertEquals(latticePitch, reactor.getLatticePitch(), 0);

		// Setting to an invalid value should change nothing.
		invalidLatticePitch = -5.0;
		reactor.setLatticePitch(invalidLatticePitch);
		assertEquals(latticePitch, reactor.getLatticePitch(), 0);

		return;
	}

	/**
	 * <p>
	 * Tests the getter and setter of SFReactor's outerFlatToFlat attribute.
	 * </p>
	 * 
	 */
	@Test
	public void checkOuterFlatToFlat() {

		// A reactor to play with.
		SFReactor reactor = new SFReactor(10);

		// Some outer flat-to-flat values.
		double defaultOuterFlatToFlat = 1.0;
		double outerFlatToFlat = 2.0;
		double invalidOuterFlatToFlat = 0.0;

		// Check the default value.
		assertEquals(defaultOuterFlatToFlat, reactor.getOuterFlatToFlat(), 0);

		// We should be able to set a new value.
		reactor.setOuterFlatToFlat(outerFlatToFlat);
		assertEquals(outerFlatToFlat, reactor.getOuterFlatToFlat(), 0);

		// Setting to an invalid value should change nothing.
		reactor.setOuterFlatToFlat(invalidOuterFlatToFlat);
		assertEquals(outerFlatToFlat, reactor.getOuterFlatToFlat(), 0);

		// We should be able to set a new value.
		outerFlatToFlat = Double.MIN_VALUE;
		reactor.setOuterFlatToFlat(outerFlatToFlat);
		assertEquals(outerFlatToFlat, reactor.getOuterFlatToFlat(), 0);

		// Setting to an invalid value should change nothing.
		invalidOuterFlatToFlat = -5.0;
		reactor.setOuterFlatToFlat(invalidOuterFlatToFlat);
		assertEquals(outerFlatToFlat, reactor.getOuterFlatToFlat(), 0);

		return;
	}

	/**
	 * <p>
	 * Tests the addition and removal of assemblies in the SFReactor.
	 * </p>
	 * 
	 */
	@Test
	public void checkAssemblyAddRem() {

		// Note: I only check reactor.getAssemblyLocations() in a few spots
		// below since it's a late addition. Further testing may be required.

		int size = 15;

		List<Integer> boundaryIndexes = new ArrayList<Integer>();
		boundaryIndexes.add(-1);
		boundaryIndexes.add(0);
		boundaryIndexes.add(size - 1);
		boundaryIndexes.add(size);

		// An SFReactor to test.
		SFReactor reactor = new SFReactor(size);

		// Initialize some assemblies to add/remove from the reactor.
		PinAssembly InnerFuelAssembly = new PinAssembly("Vogons",
				PinType.InnerFuel, 41);
		PinAssembly blanketFuelAssembly = new PinAssembly("Hooloovoo",
				PinType.BlanketFuel, 43);
		PinAssembly primaryControlAssembly = new PinAssembly("Mice",
				PinType.PrimaryControl, 42);
		PinAssembly secondaryControlAssembly = new PinAssembly("Dolphins",
				PinType.SecondaryControl, 32);
		ReflectorAssembly reflectorAssembly = new ReflectorAssembly(
				"Grebulons", 64);
		SFRAssembly plainAssembly = new SFRAssembly(42);

		AssemblyType assemblyType;
		String name;

		// Methods to test:
		// public boolean addAssembly(AssemblyType type, SFRComposite assembly)
		// public boolean removeAssembly(AssemblyType type, String assemblyName)
		// public boolean removeAssemblyFromLocation(AssemblyType type, int row,
		// int column)
		// public int getNumberOfAssemblies(AssemblyType type)
		// public ArrayList<String> getAssemblyNames(AssemblyType type)
		// public SFRComponent getAssemblyByName(AssemblyType type, String name)
		// public SFRComponent getAssemblyByLocation(AssemblyType type, int row,
		// int column)
		// public boolean setAssemblyLocation(AssemblyType type, String
		// assemblyName, int row, int column)

		// Make sure we have no assemblies of any type first.
		for (AssemblyType type : AssemblyType.values()) {
			assertEquals(0, reactor.getNumberOfAssemblies(type));
			assertTrue(reactor.getAssemblyNames(type).isEmpty());

			// Check invalid locations.
			for (int row : boundaryIndexes) {
				for (int column : boundaryIndexes) {
					if (row < 0 || row == size || column < 0 || column == size) {
						assertNull(reactor.getAssemblyByLocation(type, row,
								column));
					}
				}
			}

			// Check all valid locations.
			for (int row = 0; row < size; row++) {
				for (int column = 0; column < size; column++) {
					assertNull(reactor.getAssemblyByLocation(type, row, column));
				}
			}
		}

		/* ---- Try various argument combinations for each method. ---- */
		for (AssemblyType type : AssemblyType.values()) {

			// The name of the assembly that we'll be adding.
			name = plainAssembly.getName();

			// Verify that there is no assembly data yet.
			assertNull(reactor.getAssemblyByName(type, name));
			assertEquals(0, reactor.getNumberOfAssemblies(type));
			assertFalse(reactor.getAssemblyNames(type).contains(name));

			// Check all the locations.
			for (int row = 0; row < size; row++) {
				for (int column = 0; column < size; column++) {
					assertNull(reactor.getAssemblyByLocation(type, row, column));
				}
			}

			// addAssembly
			assertFalse(reactor.addAssembly(null, null));
			assertFalse(reactor.addAssembly(null, plainAssembly));
			assertFalse(reactor.addAssembly(type, null));

			// addAssembly (successful)
			assertTrue(reactor.addAssembly(type, plainAssembly));

			// setAssemblyLocation
			assertFalse(reactor.setAssemblyLocation(null, null, -1, -1));
			assertFalse(reactor.setAssemblyLocation(null, name, 0, 0));
			assertFalse(reactor.setAssemblyLocation(type, null, 0, 0));
			assertFalse(reactor.setAssemblyLocation(type, "Vogons", 0, 0));
			assertFalse(reactor.setAssemblyLocation(type, name, -1, 0));
			assertFalse(reactor.setAssemblyLocation(type, name, 0, size));

			// Check setting invalid locations.
			for (int row : boundaryIndexes) {
				for (int column : boundaryIndexes) {
					if (row < 0 || row >= size || column < 0 || column >= size) {
						assertFalse(reactor.setAssemblyLocation(type, name,
								row, column));
						assertNull(reactor.getAssemblyByLocation(type, row,
								column));
					}
				}
			}

			// setAssemblyLocation (successful)
			assertTrue(reactor.setAssemblyLocation(type, name, 0, 0));

			// getNumberOfAssemblies
			assertEquals(0, reactor.getNumberOfAssemblies(null));

			// getNumberOfAssemblies (successful)
			assertEquals(1, reactor.getNumberOfAssemblies(type));

			// getAssemblyNames
			assertTrue(reactor.getAssemblyNames(null).isEmpty());

			// getAssemblyNames (successful)
			assertEquals(1, reactor.getAssemblyNames(type).size());
			assertTrue(reactor.getAssemblyNames(type).contains(name));

			// getAssemblyByName
			assertNull(reactor.getAssemblyByName(null, null));
			assertNull(reactor.getAssemblyByName(null, name));
			assertNull(reactor.getAssemblyByName(type, null));
			assertNull(reactor.getAssemblyByName(type, "Vogons"));

			// getAssemblyByName (successful)
			assertEquals(plainAssembly, reactor.getAssemblyByName(type, name));

			// getAssemblyByLocation
			assertNull(reactor.getAssemblyByLocation(null, -1, -1));
			assertNull(reactor.getAssemblyByLocation(null, 0, 0));
			assertNull(reactor.getAssemblyByLocation(type, -1, 0));
			assertNull(reactor.getAssemblyByLocation(type, 0, size));
			assertNull(reactor.getAssemblyByLocation(type, size, -1));
			assertNull(reactor.getAssemblyByLocation(type, 1, 1));

			// getAssemblyByLocation (successful)
			assertEquals(plainAssembly,
					reactor.getAssemblyByLocation(type, 0, 0));

			// getAssemblyLocations
			assertTrue(reactor.getAssemblyLocations(null, null).isEmpty());
			assertTrue(reactor.getAssemblyLocations(null, name).isEmpty());
			assertTrue(reactor.getAssemblyLocations(type, null).isEmpty());
			assertTrue(reactor.getAssemblyLocations(type, "Vogons").isEmpty());

			// getAssemblyLocations (successful)
			assertEquals(1, reactor.getAssemblyLocations(type, name).size());
			assertTrue(reactor.getAssemblyLocations(type, name).contains(0));

			// removeAssemblyFromLocation
			assertFalse(reactor.removeAssemblyFromLocation(null, -1, -1));
			assertFalse(reactor.removeAssemblyFromLocation(null, 0, 0));
			assertFalse(reactor.removeAssemblyFromLocation(type, -1, 0));
			assertFalse(reactor.removeAssemblyFromLocation(type, 0, size));
			assertFalse(reactor.removeAssemblyFromLocation(type, size, -1));
			assertFalse(reactor.removeAssemblyFromLocation(type, 1, 1));

			// removeAssemblyFromLocation (successful)
			assertTrue(reactor.removeAssemblyFromLocation(type, 0, 0));

			// removeAssembly
			assertFalse(reactor.removeAssembly(null, null));
			assertFalse(reactor.removeAssembly(null, name));
			assertFalse(reactor.removeAssembly(type, null));

			// removeAssembly (successful).
			assertTrue(reactor.removeAssembly(type, name));

			// Verify that the assembly has been completely removed.
			assertNull(reactor.getAssemblyByName(type, name));
			assertEquals(0, reactor.getNumberOfAssemblies(type));
			assertFalse(reactor.getAssemblyNames(type).contains(name));

			// Check all the locations.
			for (int row = 0; row < size; row++) {
				for (int column = 0; column < size; column++) {
					assertNull(reactor.getAssemblyByLocation(type, row, column));
				}
			}
		}
		/* ------------------------------------------------------------ */

		/* ---- Test adding an assembly. ---- */
		// public boolean addAssembly()
		assemblyType = AssemblyType.Fuel;
		name = InnerFuelAssembly.getName();

		// Add an assembly.
		assertTrue(reactor.addAssembly(AssemblyType.Fuel, InnerFuelAssembly));
		assertFalse(reactor.addAssembly(AssemblyType.Fuel, InnerFuelAssembly));

		// Make sure the assembly was added, but it is not in any location.
		assertEquals(InnerFuelAssembly,
				reactor.getAssemblyByName(assemblyType, name));
		assertTrue(reactor.getAssemblyNames(assemblyType).contains(name));
		assertEquals(1, reactor.getNumberOfAssemblies(assemblyType));

		// All assembly locations should be empty.
		for (int row = 0; row < size; row++) {
			for (int column = 0; column < size; column++) {
				assertNull(reactor.getAssemblyByLocation(assemblyType, row,
						column));
			}
		}
		assertTrue(reactor.getAssemblyLocations(assemblyType, name).isEmpty());

		// Add the assembly to some locations.

		// Put it in the first spot.
		assertTrue(reactor.setAssemblyLocation(assemblyType, name, 0, 0));
		assertEquals(InnerFuelAssembly,
				reactor.getAssemblyByLocation(assemblyType, 0, 0));

		// Put it in a middle spot.
		assertTrue(reactor.setAssemblyLocation(assemblyType, name, 12, 10));
		assertEquals(InnerFuelAssembly,
				reactor.getAssemblyByLocation(assemblyType, 12, 10));

		// Put it in a middle spot.
		assertTrue(reactor.setAssemblyLocation(assemblyType, name, 4, 2));
		assertEquals(InnerFuelAssembly,
				reactor.getAssemblyByLocation(assemblyType, 4, 2));

		// Put it in the last spot.
		assertTrue(reactor.setAssemblyLocation(assemblyType, name, size - 1,
				size - 1));
		assertEquals(InnerFuelAssembly,
				reactor.getAssemblyByLocation(assemblyType, 0, 0));

		// Put it in an invalid spot.
		assertFalse(reactor.setAssemblyLocation(assemblyType, name, 0, -1));
		assertNull(reactor.getAssemblyByLocation(assemblyType, 0, -1));

		// Check the assembly's locations.
		assertEquals(4, reactor.getAssemblyLocations(assemblyType, name).size());
		assertTrue(reactor.getAssemblyLocations(assemblyType, name).contains(0));
		assertTrue(reactor.getAssemblyLocations(assemblyType, name).contains(
				12 * size + 10));
		assertTrue(reactor.getAssemblyLocations(assemblyType, name).contains(
				4 * size + 2));
		assertTrue(reactor.getAssemblyLocations(assemblyType, name).contains(
				size * size - 1));
		/* ---------------------------------- */

		/* ---- Test overriding an assembly in a location. ---- */

		// Try overriding a location with the assembly that's already there.
		assertFalse(reactor.setAssemblyLocation(assemblyType, name, 0, 0));

		// Verify that the core fuel assembly is still in location 0, 0.
		assertEquals(InnerFuelAssembly,
				reactor.getAssemblyByLocation(assemblyType, 0, 0));

		// Add the other fuel assembly.
		assertTrue(reactor.addAssembly(assemblyType, blanketFuelAssembly));

		// Make sure it was added and that the core fuel assembly is still
		// there.
		assertEquals(2, reactor.getNumberOfAssemblies(assemblyType));
		assertEquals(InnerFuelAssembly,
				reactor.getAssemblyByName(assemblyType, name));
		assertTrue(reactor.getAssemblyNames(assemblyType).contains(name));
		assertEquals(
				blanketFuelAssembly,
				reactor.getAssemblyByName(assemblyType,
						blanketFuelAssembly.getName()));
		assertTrue(reactor.getAssemblyNames(assemblyType).contains(
				blanketFuelAssembly.getName()));

		// Verify that the core fuel assembly is still in location 0, 0.
		assertEquals(InnerFuelAssembly,
				reactor.getAssemblyByLocation(assemblyType, 0, 0));

		// Override location 0, 0.
		assertTrue(reactor.setAssemblyLocation(assemblyType,
				blanketFuelAssembly.getName(), 0, 0));

		// It should now be the blanket fuel assembly in that spot.
		assertEquals(blanketFuelAssembly,
				reactor.getAssemblyByLocation(assemblyType, 0, 0));
		/* ---------------------------------------------------- */

		/* ---- Test adding another type of assembly. ---- */
		// Add another type of assembly.
		assertTrue(reactor.addAssembly(AssemblyType.Control,
				primaryControlAssembly));

		// Make sure it was added.
		assertEquals(primaryControlAssembly, reactor.getAssemblyByName(
				AssemblyType.Control, primaryControlAssembly.getName()));
		assertEquals(1, reactor.getNumberOfAssemblies(AssemblyType.Control));

		// Make sure the fuel assemblies are still there.
		assertEquals(2, reactor.getNumberOfAssemblies(assemblyType));
		assertEquals(InnerFuelAssembly,
				reactor.getAssemblyByName(assemblyType, name));
		assertTrue(reactor.getAssemblyNames(assemblyType).contains(name));
		assertEquals(
				blanketFuelAssembly,
				reactor.getAssemblyByName(assemblyType,
						blanketFuelAssembly.getName()));
		assertTrue(reactor.getAssemblyNames(assemblyType).contains(
				blanketFuelAssembly.getName()));

		// Add it to location 12, 10.
		assertTrue(reactor.setAssemblyLocation(AssemblyType.Control,
				primaryControlAssembly.getName(), 12, 10));

		// It should be there.
		assertEquals(primaryControlAssembly,
				reactor.getAssemblyByLocation(AssemblyType.Control, 12, 10));

		// The core fuel assembly should still be there for the fuel type.
		assertEquals(InnerFuelAssembly,
				reactor.getAssemblyByLocation(assemblyType, 12, 10));
		/* ----------------------------------------------- */

		/* ---- Test removing an assembly from a location. ---- */
		// public boolean removeAssemblyFromLocation()
		assertTrue(reactor.removeAssemblyFromLocation(assemblyType, 12, 10));

		// The core fuel assembly shouldn't be there.
		assertNull(reactor.getAssemblyByLocation(assemblyType, 12, 10));

		// The control assembly should still be there.
		assertEquals(primaryControlAssembly,
				reactor.getAssemblyByLocation(AssemblyType.Control, 12, 10));
		/* ---------------------------------------------------- */

		/* ---- Test removing an assembly completely. ---- */
		// public boolean removeAssembly()
		assertTrue(reactor.removeAssembly(assemblyType, name));

		// The reactor shouldn't know about the core fuel assembly.
		assertNull(reactor.getAssemblyByName(assemblyType, name));
		assertEquals(1, reactor.getNumberOfAssemblies(assemblyType));
		assertTrue(reactor.getAssemblyNames(assemblyType).contains(
				blanketFuelAssembly.getName()));
		assertFalse(reactor.getAssemblyNames(assemblyType).contains(name));

		// The core fuel assembly should not be in any location.
		for (int row = 0; row < size; row++) {
			for (int column = 0; column < size; column++) {
				if (row == 0 && column == 0) {
					assertEquals(blanketFuelAssembly,
							reactor.getAssemblyByLocation(assemblyType, row,
									column));
				} else {
					assertNull(reactor.getAssemblyByLocation(assemblyType, row,
							column));
				}
			}
		}
		assertTrue(reactor.getAssemblyLocations(assemblyType, name).isEmpty());
		/* ----------------------------------------------- */

		// Add another control assembly.
		assertTrue(reactor.addAssembly(AssemblyType.Control,
				secondaryControlAssembly));
		assertTrue(reactor.setAssemblyLocation(AssemblyType.Control,
				secondaryControlAssembly.getName(), 10, 10));

		// Add a reflector assembly and set a location for it.
		assertTrue(reactor.addAssembly(AssemblyType.Reflector,
				reflectorAssembly));
		assertTrue(reactor.setAssemblyLocation(AssemblyType.Reflector,
				reflectorAssembly.getName(), 10, 10));

		/* ---- Check the fuel assemblies in the reactor. ---- */
		// Check the blanket fuel assembly.
		assemblyType = AssemblyType.Fuel;

		// Check the number of assemblies and their names.
		assertEquals(1, reactor.getNumberOfAssemblies(assemblyType));
		assertTrue(reactor.getAssemblyNames(assemblyType).contains(
				blanketFuelAssembly.getName()));

		// Make sure we can look up the assembly by name.
		assertEquals(
				blanketFuelAssembly,
				reactor.getAssemblyByName(assemblyType,
						blanketFuelAssembly.getName()));

		// Check the contents of each location.
		for (int row = 0; row < size; row++) {
			for (int column = 0; column < size; column++) {
				if (row == 0 && column == 0) {
					assertEquals(blanketFuelAssembly,
							reactor.getAssemblyByLocation(assemblyType, row,
									column));
				} else {
					assertNull(reactor.getAssemblyByLocation(assemblyType, row,
							column));
				}
			}
		}
		/* --------------------------------------------------- */

		/* ---- Check the control assemblies in the reactor. ---- */
		// Check the control assemblies.
		assemblyType = AssemblyType.Control;

		// Check the number of assemblies and their names.
		assertEquals(2, reactor.getNumberOfAssemblies(assemblyType));
		assertTrue(reactor.getAssemblyNames(assemblyType).contains(
				primaryControlAssembly.getName()));
		assertTrue(reactor.getAssemblyNames(assemblyType).contains(
				secondaryControlAssembly.getName()));

		// Make sure we can look up the assembly by name.
		assertEquals(
				primaryControlAssembly,
				reactor.getAssemblyByName(assemblyType,
						primaryControlAssembly.getName()));
		assertEquals(secondaryControlAssembly, reactor.getAssemblyByName(
				assemblyType, secondaryControlAssembly.getName()));

		// Check the contents of each location.
		for (int row = 0; row < size; row++) {
			for (int column = 0; column < size; column++) {
				if (row == 12 && column == 10) {
					assertEquals(primaryControlAssembly,
							reactor.getAssemblyByLocation(assemblyType, row,
									column));
				} else if (row == 10 && column == 10) {
					assertEquals(secondaryControlAssembly,
							reactor.getAssemblyByLocation(assemblyType, row,
									column));
				} else {
					assertNull(reactor.getAssemblyByLocation(assemblyType, row,
							column));
				}
			}
		}
		/* ------------------------------------------------------ */

		/* ---- Check the reflector assemblies in the reactor. ---- */
		// Check the reflector assembly.
		assemblyType = AssemblyType.Reflector;

		// Check the number of assemblies and their names.
		assertEquals(1, reactor.getNumberOfAssemblies(assemblyType));
		assertTrue(reactor.getAssemblyNames(assemblyType).contains(
				reflectorAssembly.getName()));

		// Make sure we can look up the assembly by name.
		assertEquals(
				reflectorAssembly,
				reactor.getAssemblyByName(assemblyType,
						reflectorAssembly.getName()));

		// Check the contents of each location.
		for (int row = 0; row < size; row++) {
			for (int column = 0; column < size; column++) {
				if (row == 10 && column == 10) {
					assertEquals(reflectorAssembly,
							reactor.getAssemblyByLocation(assemblyType, row,
									column));
				} else {
					assertNull(reactor.getAssemblyByLocation(assemblyType, row,
							column));
				}
			}
		}
		/* -------------------------------------------------------- */

		/* ---- Remove everything and verify. ---- */

		// Remove them.
		assertTrue(reactor.removeAssembly(AssemblyType.Fuel,
				blanketFuelAssembly.getName()));
		assertTrue(reactor.removeAssembly(AssemblyType.Control,
				primaryControlAssembly.getName()));
		assertTrue(reactor.removeAssembly(AssemblyType.Control,
				secondaryControlAssembly.getName()));
		assertTrue(reactor.removeAssembly(AssemblyType.Reflector,
				reflectorAssembly.getName()));

		// Make sure you can't look them up by name any more.
		assertNull(reactor.getAssemblyByName(AssemblyType.Fuel,
				blanketFuelAssembly.getName()));
		assertNull(reactor.getAssemblyByName(AssemblyType.Control,
				primaryControlAssembly.getName()));
		assertNull(reactor.getAssemblyByName(AssemblyType.Control,
				secondaryControlAssembly.getName()));
		assertNull(reactor.getAssemblyByName(AssemblyType.Reflector,
				reflectorAssembly.getName()));

		// Make sure we have no assemblies of any type first.
		for (AssemblyType type : AssemblyType.values()) {
			assertEquals(0, reactor.getNumberOfAssemblies(type));
			assertTrue(reactor.getAssemblyNames(type).isEmpty());

			// Check invalid locations.
			for (int row : boundaryIndexes) {
				for (int column : boundaryIndexes) {
					if (row < 0 || row == size || column < 0 || column == size) {
						assertNull(reactor.getAssemblyByLocation(type, row,
								column));
					}
				}
			}

			// Check all valid locations.
			for (int row = 0; row < size; row++) {
				for (int column = 0; column < size; column++) {
					assertNull(reactor.getAssemblyByLocation(type, row, column));
				}
			}
		}
		/* --------------------------------------- */

		return;
	}

	/**
	 * <p>
	 * Tests the methods inherited from SFRComposite. Users should not be able
	 * to add SFRComponents to an SFReactor through these methods.
	 * </p>
	 * 
	 */
	@Test
	public void checkCompositeImplementation() {

		// Initialize a reactor to test.
		SFReactor reactor = new SFReactor(15);

		SFRComposite composite;
		SFRComponent component;
		String name;

		/* ---- Test getComponent and its variations. ---- */
		// Tests the following methods:
		// public Component getComponent(int childId)
		// public SFRComponent getComponent(String name)
		// public ArrayList<String> getComponentNames()
		// public int getNumberOfComponents()
		// public ArrayList<Component> getComponents()

		int numberOfComposites = AssemblyType.values().length;

		// Make sure the reactor maintains a List of Composites based on the
		// possible AssemblyTypes.
		int id = 1;
		for (AssemblyType type : AssemblyType.values()) {
			String typeName = type.toString();
			name = typeName + " Composite";

			// Put a new Composite for the AssemblyType into the Map.
			composite = new SFRComposite();

			// Set the name, description, and ID of each Composite. This also
			// increments the ID counter.
			composite.setName(name);
			composite.setDescription("A Composite that contains many "
					+ typeName + " Components.");
			composite.setId(id);

			assertTrue(composite.equals(reactor.getComponent(id++)));
			assertTrue(composite.equals(reactor.getComponent(name)));
			assertTrue(reactor.getComponentNames().contains(name));
		}

		// Make sure the reactor only has these Composites.
		assertEquals(numberOfComposites, reactor.getNumberOfComponents());
		assertEquals(numberOfComposites, reactor.getComponents().size());
		assertEquals(numberOfComposites, reactor.getComponentNames().size());
		/* ----------------------------------------------- */

		// Make sure we cannot add or remove components through the API
		// inherited from SFRComposite (tests below).

		// Tests the following methods:
		// public void addComponent(Component child)
		// public void removeComponent(int childId)
		// public void removeComponent(String name)

		/* ---- Make sure we cannot add components directly. ---- */
		component = new SFRComponent();
		reactor.addComponent(component);

		assertEquals(numberOfComposites, reactor.getNumberOfComponents());
		assertFalse(reactor.getComponents().contains(component));

		// Try the same with a Composite.
		composite = new SFRComposite();
		reactor.addComponent(composite);

		assertEquals(numberOfComposites, reactor.getNumberOfComponents());
		assertFalse(reactor.getComponents().contains(composite));
		/* ------------------------------------------------------ */

		/* ---- Make sure we cannot remove components directly. ---- */
		// Get the first Composite stored (and make sure it's valid!).
		id = 1;
		name = AssemblyType.values()[0].toString() + " Composite";
		composite = (SFRComposite) reactor.getComponent(id);

		// Try to remove it via ID.
		reactor.removeComponent(id);
		assertEquals(numberOfComposites, reactor.getNumberOfComponents());
		assertTrue(reactor.getComponents().contains(composite));

		// Try to remove it via name.
		reactor.removeComponent(name);
		assertEquals(numberOfComposites, reactor.getNumberOfComponents());
		assertTrue(reactor.getComponents().contains(composite));

		// Try removing an invalid component.
		reactor.removeComponent(null);
		assertEquals(numberOfComposites, reactor.getNumberOfComponents());
		/* --------------------------------------------------------- */

		return;
	}

	/**
	 * <p>
	 * Tests the equality and hashCode operations.
	 * </p>
	 */
	@Test
	public void checkEquality() {

		int size = 19;

		// Initialize objects for testing.
		SFReactor object = new SFReactor(size);
		SFReactor equalObject = new SFReactor(size);
		SFReactor unequalObject = new SFReactor(size);

		// Set up the object and equalObject.

		/* ---- Set up some data to go into the object. ---- */
		PinAssembly fuelAssembly = new PinAssembly("Assembly A",
				PinType.InnerFuel, 15);
		PinAssembly controlAssembly = new PinAssembly("Assembly B",
				PinType.PrimaryControl, 14);
		ReflectorAssembly reflectorAssembly = new ReflectorAssembly(
				"Assembly C", 17);

		SFRPin pin = new SFRPin("Zaphod");
		SFRData data1 = new SFRData("From Earth");
		data1.setValue(0);
		SFRData data2 = new SFRData("From Space");
		data2.setValue(1);

		fuelAssembly.addPin(pin);
		fuelAssembly.setPinLocation(pin.getName(), 0, 0);
		fuelAssembly.setPinLocation(pin.getName(), 1, 3);
		fuelAssembly.setPinLocation(pin.getName(), 4, 1);
		controlAssembly.addPin(pin);
		controlAssembly.setPinLocation(pin.getName(), 0, 0);
		controlAssembly.setPinLocation(pin.getName(), 2, 4);
		controlAssembly.setPinLocation(pin.getName(), 3, 2);

		pin = new SFRPin("Trillian");
		data1 = new SFRData("From Earth");
		data1.setValue(1);
		data2 = new SFRData("From Space");
		data2.setValue(0);

		fuelAssembly.addPin(pin);
		fuelAssembly.setPinLocation(pin.getName(), 1, 1);
		fuelAssembly.setPinLocation(pin.getName(), 4, 5);
		fuelAssembly.setPinLocation(pin.getName(), 8, 8);
		controlAssembly.addPin(pin);
		controlAssembly.setPinLocation(pin.getName(), 2, 4);
		controlAssembly.setPinLocation(pin.getName(), 5, 6);
		controlAssembly.setPinLocation(pin.getName(), 1, 3);

		SFRRod rod = new SFRRod("Arthur");
		data1 = new SFRData("From Earth");
		data1.setValue(1);
		data2 = new SFRData("From Space");
		data2.setValue(0);

		reflectorAssembly.addRod(rod);
		reflectorAssembly.setRodLocation(rod.getName(), 0, 0);
		reflectorAssembly.setRodLocation(rod.getName(), 1, 3);
		reflectorAssembly.setRodLocation(rod.getName(), 4, 1);
		reflectorAssembly.setRodLocation(rod.getName(), 1, 1);

		rod = new SFRRod("Ford");
		data1 = new SFRData("From Earth");
		data1.setValue(0);
		data2 = new SFRData("From Space");
		data2.setValue(1);

		reflectorAssembly.addRod(rod);
		reflectorAssembly.setRodLocation(rod.getName(), 1, 0);
		reflectorAssembly.setRodLocation(rod.getName(), 6, 3);
		reflectorAssembly.setRodLocation(rod.getName(), 4, 7);
		reflectorAssembly.setRodLocation(rod.getName(), 6, 1);

		object.addAssembly(fuelAssembly.getAssemblyType(), fuelAssembly);
		object.addAssembly(controlAssembly.getAssemblyType(), controlAssembly);
		object.addAssembly(reflectorAssembly.getAssemblyType(),
				reflectorAssembly);

		object.setAssemblyLocation(fuelAssembly.getAssemblyType(),
				fuelAssembly.getName(), 15, 15);
		object.setAssemblyLocation(fuelAssembly.getAssemblyType(),
				fuelAssembly.getName(), 12, 10);
		object.setAssemblyLocation(fuelAssembly.getAssemblyType(),
				fuelAssembly.getName(), 17, 17);
		object.setAssemblyLocation(controlAssembly.getAssemblyType(),
				controlAssembly.getName(), 15, 15);
		object.setAssemblyLocation(controlAssembly.getAssemblyType(),
				controlAssembly.getName(), 12, 10);
		object.setAssemblyLocation(controlAssembly.getAssemblyType(),
				controlAssembly.getName(), 17, 17);
		object.setAssemblyLocation(reflectorAssembly.getAssemblyType(),
				reflectorAssembly.getName(), 12, 10);
		object.setAssemblyLocation(reflectorAssembly.getAssemblyType(),
				reflectorAssembly.getName(), 17, 17);
		/* ------------------------------------------------- */

		/* ---- Set up some data to go into the equalObject. ---- */
		fuelAssembly = new PinAssembly("Assembly A", PinType.InnerFuel, 15);
		controlAssembly = new PinAssembly("Assembly B", PinType.PrimaryControl,
				14);
		reflectorAssembly = new ReflectorAssembly("Assembly C", 17);

		pin = new SFRPin("Zaphod");
		data1 = new SFRData("From Earth");
		data1.setValue(0);
		data2 = new SFRData("From Space");
		data2.setValue(1);

		fuelAssembly.addPin(pin);
		fuelAssembly.setPinLocation(pin.getName(), 0, 0);
		fuelAssembly.setPinLocation(pin.getName(), 1, 3);
		fuelAssembly.setPinLocation(pin.getName(), 4, 1);
		controlAssembly.addPin(pin);
		controlAssembly.setPinLocation(pin.getName(), 0, 0);
		controlAssembly.setPinLocation(pin.getName(), 2, 4);
		controlAssembly.setPinLocation(pin.getName(), 3, 2);

		pin = new SFRPin("Trillian");
		data1 = new SFRData("From Earth");
		data1.setValue(1);
		data2 = new SFRData("From Space");
		data2.setValue(0);

		fuelAssembly.addPin(pin);
		fuelAssembly.setPinLocation(pin.getName(), 1, 1);
		fuelAssembly.setPinLocation(pin.getName(), 4, 5);
		fuelAssembly.setPinLocation(pin.getName(), 8, 8);
		controlAssembly.addPin(pin);
		controlAssembly.setPinLocation(pin.getName(), 2, 4);
		controlAssembly.setPinLocation(pin.getName(), 5, 6);
		controlAssembly.setPinLocation(pin.getName(), 1, 3);

		rod = new SFRRod("Arthur");
		data1 = new SFRData("From Earth");
		data1.setValue(1);
		data2 = new SFRData("From Space");
		data2.setValue(0);

		reflectorAssembly.addRod(rod);
		reflectorAssembly.setRodLocation(rod.getName(), 0, 0);
		reflectorAssembly.setRodLocation(rod.getName(), 1, 3);
		reflectorAssembly.setRodLocation(rod.getName(), 4, 1);
		reflectorAssembly.setRodLocation(rod.getName(), 1, 1);

		rod = new SFRRod("Ford");
		data1 = new SFRData("From Earth");
		data1.setValue(0);
		data2 = new SFRData("From Space");
		data2.setValue(1);

		reflectorAssembly.addRod(rod);
		reflectorAssembly.setRodLocation(rod.getName(), 1, 0);
		reflectorAssembly.setRodLocation(rod.getName(), 6, 3);
		reflectorAssembly.setRodLocation(rod.getName(), 4, 7);
		reflectorAssembly.setRodLocation(rod.getName(), 6, 1);

		equalObject.addAssembly(fuelAssembly.getAssemblyType(), fuelAssembly);
		equalObject.addAssembly(controlAssembly.getAssemblyType(),
				controlAssembly);
		equalObject.addAssembly(reflectorAssembly.getAssemblyType(),
				reflectorAssembly);

		equalObject.setAssemblyLocation(fuelAssembly.getAssemblyType(),
				fuelAssembly.getName(), 15, 15);
		equalObject.setAssemblyLocation(fuelAssembly.getAssemblyType(),
				fuelAssembly.getName(), 12, 10);
		equalObject.setAssemblyLocation(fuelAssembly.getAssemblyType(),
				fuelAssembly.getName(), 17, 17);
		equalObject.setAssemblyLocation(controlAssembly.getAssemblyType(),
				controlAssembly.getName(), 15, 15);
		equalObject.setAssemblyLocation(controlAssembly.getAssemblyType(),
				controlAssembly.getName(), 12, 10);
		equalObject.setAssemblyLocation(controlAssembly.getAssemblyType(),
				controlAssembly.getName(), 17, 17);
		equalObject.setAssemblyLocation(reflectorAssembly.getAssemblyType(),
				reflectorAssembly.getName(), 12, 10);
		equalObject.setAssemblyLocation(reflectorAssembly.getAssemblyType(),
				reflectorAssembly.getName(), 17, 17);
		/* ------------------------------------------------------ */

		// Set up the unequalObject.

		/* ---- Set up some data to go into the unequalObject. ---- */
		fuelAssembly = new PinAssembly("Assembly A", PinType.InnerFuel, 15);
		controlAssembly = new PinAssembly("Assembly B", PinType.PrimaryControl,
				14);
		reflectorAssembly = new ReflectorAssembly("Assembly C", 17);

		pin = new SFRPin("Zaphod");
		data1 = new SFRData("From Earth");
		data1.setValue(0);
		data2 = new SFRData("From Space");
		data2.setValue(1);

		fuelAssembly.addPin(pin);
		fuelAssembly.setPinLocation(pin.getName(), 0, 0);
		fuelAssembly.setPinLocation(pin.getName(), 1, 3);
		fuelAssembly.setPinLocation(pin.getName(), 4, 1);
		controlAssembly.addPin(pin);
		controlAssembly.setPinLocation(pin.getName(), 0, 0);
		controlAssembly.setPinLocation(pin.getName(), 2, 4);
		controlAssembly.setPinLocation(pin.getName(), 3, 2);

		pin = new SFRPin("Trillian");
		data1 = new SFRData("From Earth");
		data1.setValue(1);
		data2 = new SFRData("From Space");
		data2.setValue(0);

		fuelAssembly.addPin(pin);
		fuelAssembly.setPinLocation(pin.getName(), 1, 1);
		fuelAssembly.setPinLocation(pin.getName(), 4, 5);
		fuelAssembly.setPinLocation(pin.getName(), 8, 9); // Different!
		controlAssembly.addPin(pin);
		controlAssembly.setPinLocation(pin.getName(), 2, 4);
		controlAssembly.setPinLocation(pin.getName(), 5, 6);
		controlAssembly.setPinLocation(pin.getName(), 1, 3);

		rod = new SFRRod("Arthur");
		data1 = new SFRData("From Earth");
		data1.setValue(1);
		data2 = new SFRData("From Space");
		data2.setValue(0);

		reflectorAssembly.addRod(rod);
		reflectorAssembly.setRodLocation(rod.getName(), 0, 0);
		reflectorAssembly.setRodLocation(rod.getName(), 1, 3);
		reflectorAssembly.setRodLocation(rod.getName(), 4, 1);
		reflectorAssembly.setRodLocation(rod.getName(), 1, 1);

		rod = new SFRRod("Ford");
		data1 = new SFRData("From Earth");
		data1.setValue(0);
		data2 = new SFRData("From Space");
		data2.setValue(1);

		reflectorAssembly.addRod(rod);
		reflectorAssembly.setRodLocation(rod.getName(), 1, 0);
		reflectorAssembly.setRodLocation(rod.getName(), 6, 3);
		reflectorAssembly.setRodLocation(rod.getName(), 4, 7);
		reflectorAssembly.setRodLocation(rod.getName(), 6, 1);

		unequalObject.addAssembly(fuelAssembly.getAssemblyType(), fuelAssembly);
		unequalObject.addAssembly(controlAssembly.getAssemblyType(),
				controlAssembly);
		unequalObject.addAssembly(reflectorAssembly.getAssemblyType(),
				reflectorAssembly);

		unequalObject.setAssemblyLocation(fuelAssembly.getAssemblyType(),
				fuelAssembly.getName(), 15, 15);
		unequalObject.setAssemblyLocation(fuelAssembly.getAssemblyType(),
				fuelAssembly.getName(), 12, 10);
		unequalObject.setAssemblyLocation(fuelAssembly.getAssemblyType(),
				fuelAssembly.getName(), 17, 17);
		unequalObject.setAssemblyLocation(controlAssembly.getAssemblyType(),
				controlAssembly.getName(), 15, 15);
		unequalObject.setAssemblyLocation(controlAssembly.getAssemblyType(),
				controlAssembly.getName(), 12, 10);
		unequalObject.setAssemblyLocation(controlAssembly.getAssemblyType(),
				controlAssembly.getName(), 17, 17);
		unequalObject.setAssemblyLocation(reflectorAssembly.getAssemblyType(),
				reflectorAssembly.getName(), 12, 10);
		unequalObject.setAssemblyLocation(reflectorAssembly.getAssemblyType(),
				reflectorAssembly.getName(), 17, 17);
		/* -------------------------------------------------------- */

		// Make sure the references are different.
		assertFalse(object == equalObject);
		assertFalse(object == unequalObject);
		assertFalse(equalObject == unequalObject);

		// Check that equality is reflexive and symmetric.
		assertTrue(object.equals(object));
		assertTrue(object.equals(equalObject));
		assertTrue(equalObject.equals(object));

		// Check that equals will fail when it should.
		assertFalse(object==null);
		assertFalse(object.equals(42));
		assertFalse("just a string".equals(object));
		assertFalse(object.equals(unequalObject));
		assertFalse(unequalObject.equals(object));

		// Check the hash codes.
		assertTrue(object.hashCode() == object.hashCode());
		assertTrue(object.hashCode() == equalObject.hashCode());
		assertFalse(object.hashCode() == unequalObject.hashCode());

		return;
	}

	/**
	 * <p>
	 * Tests the copying and cloning operations.
	 * </p>
	 */
	@Test
	public void checkCopying() {

		int size = 34;

		// Initialize objects for testing.
		SFReactor object = new SFReactor(size);
		SFReactor copy = new SFReactor(15);
		SFReactor clone = null;

		// Set up the object.

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
		clone = (SFReactor) object.clone();

		// Make sure the references are different but contents the same.
		assertFalse(object == clone);
		assertTrue(object.equals(clone));
		assertFalse(copy == clone);
		assertTrue(copy.equals(clone));

		return;
	}
}