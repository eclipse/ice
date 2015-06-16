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
import org.eclipse.ice.reactor.sfr.core.assembly.ReflectorAssembly;
import org.eclipse.ice.reactor.sfr.core.assembly.SFRRod;
import org.junit.Test;

/**
 * <p>
 * Tests the operations of the ReflectorAssembly class.
 * </p>
 * 
 * @author Anna Wojtowicz
 */
public class ReflectorAssemblyTester {

	// FIXME - removed checkRod

	/**
	 * <p>
	 * Tests constructors and default values of the ReflectorAssembly class.
	 * </p>
	 * 
	 */
	@Test
	public void checkConstruction() {

		// An assembly to test.
		ReflectorAssembly assembly;

		// Invalid, default, and valid values to use in the constructor.
		int invalidSize = 0;
		int defaultSize = 1;
		int size = 31;

		String nullName = null;
		String emptyName = "            ";
		String defaultName = "SFR Reflector Assembly 1";
		String name = "Tricia";

		// Other defaults.
		AssemblyType defaultType = AssemblyType.Reflector;
		String defaultDescription = "SFR Reflector Assembly 1's Description";
		int defaultId = 1;
		double defaultDuctThickness = 0.0;
		double defaultRodPitch = 1.0;

		/* ---- Test the basic constructor. ---- */
		// Invalid parameters.
		assembly = new ReflectorAssembly(invalidSize);

		assertEquals(defaultId, assembly.getId());
		assertEquals(defaultName, assembly.getName());
		assertEquals(defaultDescription, assembly.getDescription());
		assertEquals(defaultSize, assembly.getSize());
		assertEquals(defaultType, assembly.getAssemblyType());
		assertEquals(defaultDuctThickness, assembly.getDuctThickness(), 0);
		assertEquals(defaultRodPitch, assembly.getRodPitch(), 0);

		// Valid parameters.
		assembly = new ReflectorAssembly(size);

		assertEquals(defaultId, assembly.getId());
		assertEquals(defaultName, assembly.getName());
		assertEquals(defaultDescription, assembly.getDescription());
		assertEquals(size, assembly.getSize());
		assertEquals(defaultType, assembly.getAssemblyType());
		assertEquals(defaultDuctThickness, assembly.getDuctThickness(), 0);
		assertEquals(defaultRodPitch, assembly.getRodPitch(), 0);
		/* ------------------------------------- */

		/* ---- Test the second constructor. ---- */
		// Invalid parameters.

		// All invalid.
		assembly = new ReflectorAssembly(nullName, invalidSize);

		assertEquals(defaultId, assembly.getId());
		assertEquals(defaultName, assembly.getName());
		assertEquals(defaultDescription, assembly.getDescription());
		assertEquals(defaultSize, assembly.getSize());
		assertEquals(defaultType, assembly.getAssemblyType());
		assertEquals(defaultDuctThickness, assembly.getDuctThickness(), 0);
		assertEquals(defaultRodPitch, assembly.getRodPitch(), 0);

		// Name invalid.
		assembly = new ReflectorAssembly(emptyName, size);

		assertEquals(defaultId, assembly.getId());
		assertEquals(defaultName, assembly.getName());
		assertEquals(defaultDescription, assembly.getDescription());
		assertEquals(size, assembly.getSize());
		assertEquals(defaultType, assembly.getAssemblyType());
		assertEquals(defaultDuctThickness, assembly.getDuctThickness(), 0);
		assertEquals(defaultRodPitch, assembly.getRodPitch(), 0);

		// Size invalid.
		assembly = new ReflectorAssembly(name, invalidSize);

		assertEquals(defaultId, assembly.getId());
		assertEquals(name, assembly.getName());
		assertEquals(defaultDescription, assembly.getDescription());
		assertEquals(defaultSize, assembly.getSize());
		assertEquals(defaultType, assembly.getAssemblyType());
		assertEquals(defaultDuctThickness, assembly.getDuctThickness(), 0);
		assertEquals(defaultRodPitch, assembly.getRodPitch(), 0);

		// Valid parameters.
		assembly = new ReflectorAssembly(name, size);

		assertEquals(defaultId, assembly.getId());
		assertEquals(name, assembly.getName());
		assertEquals(defaultDescription, assembly.getDescription());
		assertEquals(size, assembly.getSize());
		assertEquals(defaultType, assembly.getAssemblyType());
		assertEquals(defaultDuctThickness, assembly.getDuctThickness(), 0);
		assertEquals(defaultRodPitch, assembly.getRodPitch(), 0);
		/* -------------------------------------- */

		return;
	}

	/**
	 * <p>
	 * Tests the getter and setter of the rodPitch attribute.
	 * </p>
	 * 
	 */
	@Test
	public void checkRodPitch() {
		ReflectorAssembly assembly = new ReflectorAssembly(1);
		double defaultRodPitch = 1.0;

		// Check the default.
		assertEquals(defaultRodPitch, assembly.getRodPitch(), 0);

		// Set it to 1 and check it.
		assembly.setRodPitch(1.0);
		assertEquals(1.0, assembly.getRodPitch(), 0);

		// Set it to an invalid number.
		assembly.setRodPitch(-1.0);
		assertEquals(1.0, assembly.getRodPitch(), 0);

		// Set it to a valid number.
		assembly.setRodPitch(500);
		assertEquals(500, assembly.getRodPitch(), 0);

		return;
	}

	/**
	 * <p>
	 * Tests the addition and removal of SFRRods in the ReflectorAssembly.
	 * </p>
	 * 
	 */
	@Test
	public void checkRodAddRem() {

		// Note: I only check assembly.getRodLocations() in a few spots
		// below since it's a late addition. Further testing may be required.

		int size = 10;

		// An assembly to test.
		ReflectorAssembly assembly = new ReflectorAssembly(10);

		// Initialize some rods to add/remove from the assembly.
		SFRRod rod1 = new SFRRod();
		rod1.setName("Anjie");
		SFRRod rod2 = new SFRRod();
		rod2.setName("Colin");
		SFRRod rod3 = new SFRRod();
		rod3.setName("Dish of the Day");

		// A List of boundary indexes to test.
		List<Integer> boundaryIndexes = new ArrayList<Integer>();
		boundaryIndexes.add(-1);
		boundaryIndexes.add(0);
		boundaryIndexes.add(size - 1);
		boundaryIndexes.add(size);

		// A set of indexes that will be empty. This makes it easier to check
		// rod locations.
		Set<Integer> emptyIndexes = new HashSet<Integer>();
		for (int i = 0; i < size * size; i++) {
			emptyIndexes.add(i);
		}
		String name;

		// Methods to test:
		// public boolean addRod(SFRRod rod)
		// public boolean removeRod(String name)
		// public boolean removeRodFromLocation(int row, int column)
		// public ArrayList<String> getRodNames()
		// public SFRRod getRodByName(String name)
		// public SFRRod getRodByLocation(int row, int column)
		// public boolean setRodLocation(String name, int row, int column)

		/* ---- Make sure the assembly is empty. ---- */
		// Check invalid locations.
		for (int row : boundaryIndexes) {
			for (int column : boundaryIndexes) {
				if (row < 0 || row == size || column < 0 || column == size) {
					assertNull(assembly.getRodByLocation(row, column));
				}
			}
		}

		// Check all valid locations.
		for (int i : emptyIndexes) {
			assertNull(assembly.getRodByLocation(i / size, i % size));
		}
		/* ------------------------------------------ */

		/* ---- Try various argument combinations for each method. ---- */
		name = rod1.getName();

		// Verify that there is no rod set.
		assertNull(assembly.getRodByName(name));
		assertEquals(0, assembly.getNumberOfRods());
		assertEquals(0, assembly.getRodNames().size());
		assertFalse(assembly.getRodNames().contains(name));

		// Check all the locations.
		for (int i : emptyIndexes) {
			assertNull(assembly.getRodByLocation(i / size, i % size));
		}
		// addRod
		assertFalse(assembly.addRod(null));

		// addRod (successful)
		assertTrue(assembly.addRod(rod1));

		// setRodLocation
		assertFalse(assembly.setRodLocation(null, -1, -1));
		assertFalse(assembly.setRodLocation(null, 0, 0));
		assertFalse(assembly.setRodLocation("Vogons", 0, 0));
		assertFalse(assembly.setRodLocation(name, -1, 0));
		assertFalse(assembly.setRodLocation(name, 0, size));

		// Check setting invalid locations.
		for (int row : boundaryIndexes) {
			for (int column : boundaryIndexes) {
				if (row < 0 || row >= size || column < 0 || column >= size) {
					assertFalse(assembly.setRodLocation(name, row, column));
					assertNull(assembly.getRodByLocation(row, column));
				}
			}
		}

		// setRodLocation (successful)
		assertTrue(assembly.setRodLocation(name, 0, 0));

		// getNumberOfRods (successful)
		assertEquals(1, assembly.getNumberOfRods());

		// getRodNames (successful)
		assertEquals(1, assembly.getRodNames().size());
		assertTrue(assembly.getRodNames().contains(name));

		// getRodByName
		assertNull(assembly.getRodByName(null));
		assertNull(assembly.getRodByName("Vogons"));

		// getRodByName (successful)
		assertEquals(rod1, assembly.getRodByName(name));

		// getRodByLocation
		assertNull(assembly.getRodByLocation(-1, -1));
		assertNull(assembly.getRodByLocation(-1, 0));
		assertNull(assembly.getRodByLocation(0, size));

		// getRodByLocation (successful)
		assertEquals(rod1, assembly.getRodByLocation(0, 0));

		// getRodLocations
		assertTrue(assembly.getRodLocations(null).isEmpty());
		assertTrue(assembly.getRodLocations("Vogons").isEmpty());

		// getRodLocations (successful)
		assertEquals(1, assembly.getRodLocations(name).size());
		assertTrue(assembly.getRodLocations(name).contains(0));

		// removeRodFromLocation
		assertFalse(assembly.removeRodFromLocation(-1, -1));
		assertFalse(assembly.removeRodFromLocation(-1, 0));
		assertFalse(assembly.removeRodFromLocation(0, size));

		// removeRodFromLocation (successful)
		assertTrue(assembly.removeRodFromLocation(0, 0));

		// removeRod
		assertFalse(assembly.removeRod(null));
		assertFalse(assembly.removeRod("Vogons"));

		// removeRod (successful)
		assertTrue(assembly.removeRod(name));

		// Verify that the rod has been completely removed.
		assertNull(assembly.getRodByName(name));
		assertEquals(0, assembly.getNumberOfRods());
		assertEquals(0, assembly.getRodNames().size());
		assertFalse(assembly.getRodNames().contains(name));

		// Check all the locations.
		for (int i : emptyIndexes) {
			assertNull(assembly.getRodByLocation(i / size, i % size));
		}
		/* ------------------------------------------------------------ */

		/* ---- Add an rod and set some locations. ---- */
		name = rod1.getName();
		assertTrue(assembly.addRod(rod1));
		assertFalse(assembly.addRod(rod1));

		// Verify that the rod was added.
		assertEquals(rod1, assembly.getRodByName(name));
		assertEquals(1, assembly.getNumberOfRods());
		assertEquals(1, assembly.getRodNames().size());
		assertTrue(assembly.getRodNames().contains(name));

		// All locations should be empty.
		for (int i : emptyIndexes) {
			assertNull(assembly.getRodByLocation(i / size, i % size));
		}
		assertTrue(assembly.getRodLocations(name).isEmpty());

		// The first attempt to set a location should succeed. Subsequent
		// attempts to set the same rod in the same location should fail.

		// Put it in the first spot.
		assertTrue(assembly.setRodLocation(name, 0, 0));
		assertFalse(assembly.setRodLocation(name, 0, 0));
		emptyIndexes.remove(0);

		// Put it in a middle spot.
		assertTrue(assembly.setRodLocation(name, 0, 6));
		assertFalse(assembly.setRodLocation(name, 0, 6));
		emptyIndexes.remove(6);

		// Put it in a middle spot.
		assertTrue(assembly.setRodLocation(name, 3, 7));
		assertFalse(assembly.setRodLocation(name, 3, 7));
		emptyIndexes.remove(3 * size + 7);

		// Put it in the last spot.
		assertTrue(assembly.setRodLocation(name, size - 1, size - 1));
		assertFalse(assembly.setRodLocation(name, size - 1, size - 1));
		emptyIndexes.remove(size * size - 1);

		// Verify the rod locations.
		assertEquals(rod1, assembly.getRodByLocation(0, 0));
		assertEquals(rod1, assembly.getRodByLocation(0, 6));
		assertEquals(rod1, assembly.getRodByLocation(3, 7));
		assertEquals(rod1, assembly.getRodByLocation(size - 1, size - 1));
		for (int i : emptyIndexes) {
			assertNull(assembly.getRodByLocation(i / size, i % size));
		}
		// We should still be able to get the rod by location.
		assertEquals(rod1, assembly.getRodByLocation(0, 0));

		// Verify the rod locations (getRodLocations()).
		assertEquals(4, assembly.getRodLocations(name).size());
		assertTrue(assembly.getRodLocations(name).contains(0));
		assertTrue(assembly.getRodLocations(name).contains(0 * size + 6));
		assertTrue(assembly.getRodLocations(name).contains(3 * size + 7));
		assertTrue(assembly.getRodLocations(name).contains(size * size - 1));
		/* -------------------------------------------- */

		/* ---- Test overriding a rod location. ---- */
		name = rod2.getName();
		assertTrue(assembly.addRod(rod2));
		assertFalse(assembly.addRod(rod2));

		// Verify that the rod was added.
		assertEquals(rod2, assembly.getRodByName(name));
		assertEquals(2, assembly.getNumberOfRods());
		assertEquals(2, assembly.getRodNames().size());
		assertTrue(assembly.getRodNames().contains(rod1.getName()));
		assertTrue(assembly.getRodNames().contains(name));

		// Verify the rod locations.
		assertEquals(rod1, assembly.getRodByLocation(0, 0));
		assertEquals(rod1, assembly.getRodByLocation(0, 6));
		assertEquals(rod1, assembly.getRodByLocation(3, 7));
		assertEquals(rod1, assembly.getRodByLocation(size - 1, size - 1));
		for (int i : emptyIndexes) {
			assertNull(assembly.getRodByLocation(i / size, i % size));
		}
		// Put it in a new spot.
		assertTrue(assembly.setRodLocation(name, 1, 1));
		assertFalse(assembly.setRodLocation(name, 1, 1));
		emptyIndexes.remove(size + 1);

		// Put it in a spot occupied by rod1.
		assertTrue(assembly.setRodLocation(name, 3, 7));
		assertFalse(assembly.setRodLocation(name, 3, 7));

		// Verify the rod locations.
		assertEquals(rod1, assembly.getRodByLocation(0, 0));
		assertEquals(rod1, assembly.getRodByLocation(0, 6));
		assertEquals(rod1, assembly.getRodByLocation(size - 1, size - 1));
		assertEquals(rod2, assembly.getRodByLocation(1, 1));
		assertEquals(rod2, assembly.getRodByLocation(3, 7));
		for (int i : emptyIndexes) {
			assertNull(assembly.getRodByLocation(i / size, i % size));
		}
		/* ----------------------------------------- */

		/* ---- Test adding yet another rod. ---- */
		name = rod3.getName();
		assertTrue(assembly.addRod(rod3));
		assertFalse(assembly.addRod(rod3));

		// Verify that the rod was added.
		assertEquals(rod3, assembly.getRodByName(name));
		assertEquals(3, assembly.getNumberOfRods());
		assertEquals(3, assembly.getRodNames().size());
		assertTrue(assembly.getRodNames().contains(rod1.getName()));
		assertTrue(assembly.getRodNames().contains(rod2.getName()));
		assertTrue(assembly.getRodNames().contains(name));

		// Verify the rod locations.
		assertEquals(rod1, assembly.getRodByLocation(0, 0));
		assertEquals(rod1, assembly.getRodByLocation(0, 6));
		assertEquals(rod1, assembly.getRodByLocation(size - 1, size - 1));
		assertEquals(rod2, assembly.getRodByLocation(1, 1));
		assertEquals(rod2, assembly.getRodByLocation(3, 7));
		for (int i : emptyIndexes) {
			assertNull(assembly.getRodByLocation(i / size, i % size));
		}
		// Put it in a new spot.
		assertTrue(assembly.setRodLocation(name, 3, 3));
		assertFalse(assembly.setRodLocation(name, 3, 3));
		emptyIndexes.remove(3 * size + 3);

		// Verify the rod locations.
		assertEquals(rod1, assembly.getRodByLocation(0, 0));
		assertEquals(rod1, assembly.getRodByLocation(0, 6));
		assertEquals(rod1, assembly.getRodByLocation(size - 1, size - 1));
		assertEquals(rod2, assembly.getRodByLocation(1, 1));
		assertEquals(rod2, assembly.getRodByLocation(3, 7));
		assertEquals(rod3, assembly.getRodByLocation(3, 3));
		for (int i : emptyIndexes) {
			assertNull(assembly.getRodByLocation(i / size, i % size));
		}
		/* -------------------------------------- */

		/* ---- Test removing a rod from a location. ---- */
		assertTrue(assembly.removeRodFromLocation(0, 6));
		assertFalse(assembly.removeRodFromLocation(0, 6));
		emptyIndexes.add(6);

		// Verify the rod locations.
		assertEquals(rod1, assembly.getRodByLocation(0, 0));
		assertEquals(rod1, assembly.getRodByLocation(size - 1, size - 1));
		assertEquals(rod2, assembly.getRodByLocation(1, 1));
		assertEquals(rod2, assembly.getRodByLocation(3, 7));
		assertEquals(rod3, assembly.getRodByLocation(3, 3));
		for (int i : emptyIndexes) {
			assertNull(assembly.getRodByLocation(i / size, i % size));
		}
		// Remove rod2 entirely manually.
		assertTrue(assembly.removeRodFromLocation(1, 1));
		assertFalse(assembly.removeRodFromLocation(1, 1));
		emptyIndexes.add(size + 1);

		assertTrue(assembly.removeRodFromLocation(3, 7));
		assertFalse(assembly.removeRodFromLocation(3, 7));
		emptyIndexes.add(3 * size + 7);

		// Verify the rod locations.
		assertEquals(rod1, assembly.getRodByLocation(0, 0));
		assertEquals(rod1, assembly.getRodByLocation(size - 1, size - 1));
		assertEquals(rod3, assembly.getRodByLocation(3, 3));
		for (int i : emptyIndexes) {
			assertNull(assembly.getRodByLocation(i / size, i % size));
		}
		// Verify the rod locations (getRodLocations()).
		assertEquals(2, assembly.getRodLocations(rod1.getName()).size());
		assertTrue(assembly.getRodLocations(rod1.getName()).contains(0));
		assertTrue(assembly.getRodLocations(rod1.getName()).contains(
				size * size - 1));

		// rod2 should still be in the assembly, though.

		// Verify the rods stored in the assembly.
		assertEquals(rod1, assembly.getRodByName(rod1.getName()));
		assertEquals(rod2, assembly.getRodByName(rod2.getName()));
		assertEquals(rod3, assembly.getRodByName(rod3.getName()));
		assertEquals(3, assembly.getNumberOfRods());
		assertEquals(3, assembly.getRodNames().size());
		assertTrue(assembly.getRodNames().contains(rod1.getName()));
		assertTrue(assembly.getRodNames().contains(rod2.getName()));
		assertTrue(assembly.getRodNames().contains(rod3.getName()));
		/* ---------------------------------------------- */

		/* ---- Test removing a rod completely. ---- */
		assertTrue(assembly.removeRod(rod1.getName()));
		assertFalse(assembly.removeRod(rod1.getName()));

		emptyIndexes.add(0);
		emptyIndexes.add(size * size - 1);

		// Verify the rod locations.
		assertEquals(rod3, assembly.getRodByLocation(3, 3));
		for (int i : emptyIndexes) {
			assertNull(assembly.getRodByLocation(i / size, i % size));
		}
		// rod1 should not have any locations. In fact, since the rod is not in
		// the assembly, this returns an empty List.
		assertTrue(assembly.getRodLocations(rod1.getName()).isEmpty());

		// Verify the rods stored in the assembly.
		assertNull(assembly.getRodByName(rod1.getName()));
		assertEquals(rod2, assembly.getRodByName(rod2.getName()));
		assertEquals(rod3, assembly.getRodByName(rod3.getName()));
		assertEquals(2, assembly.getNumberOfRods());
		assertEquals(2, assembly.getRodNames().size());
		assertFalse(assembly.getRodNames().contains(rod1.getName()));
		assertTrue(assembly.getRodNames().contains(rod2.getName()));
		assertTrue(assembly.getRodNames().contains(rod3.getName()));
		/* ----------------------------------------- */

		/* ---- Remove everything and verify. ---- */
		assertFalse(assembly.removeRod(rod1.getName()));
		assertTrue(assembly.removeRod(rod2.getName()));
		assertFalse(assembly.removeRod(rod2.getName()));
		assertTrue(assembly.removeRod(rod3.getName()));
		assertFalse(assembly.removeRod(rod3.getName()));

		// Add the last rod location back to the empty index set.
		emptyIndexes.add(size * 3 + 3);

		// Verify the rod locations.
		for (int i : emptyIndexes) {
			assertNull(assembly.getRodByLocation(i / size, i % size));
		}
		// Verify the rods stored in the assembly.
		assertNull(assembly.getRodByName(rod1.getName()));
		assertNull(assembly.getRodByName(rod2.getName()));
		assertNull(assembly.getRodByName(rod3.getName()));
		assertEquals(0, assembly.getNumberOfRods());
		assertEquals(0, assembly.getRodNames().size());
		/* --------------------------------------- */

		return;
	}

	/**
	 * <p>
	 * Tests the methods inherited from SFRComposite. Users should not be able
	 * to add SFRComponents to a ReflectorAssembly through these methods.
	 * </p>
	 * 
	 */
	@Test
	public void checkCompositeImplementation() {

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
		ReflectorAssembly assembly = new ReflectorAssembly(15);

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
		component = new SFRRod();
		component.setName(name);

		// Add it.
		assertTrue(assembly.addRod((SFRRod) component));

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
	}

	/**
	 * <p>
	 * Tests the equality and hashCode operations.
	 * </p>
	 */
	@Test
	public void checkEquality() {

		int size = 78;

		// Initialize objects for testing.
		ReflectorAssembly object = new ReflectorAssembly(size);
		ReflectorAssembly equalObject = new ReflectorAssembly(size);
		ReflectorAssembly unequalObject = new ReflectorAssembly(size);

		// Set up the object and equalObject.
		SFRRod component = new SFRRod();
		component.setName("Marvin");
		SFRData subComponent = new SFRData("Depressed");
		subComponent.setValue(10);
		component.addData(subComponent, 0);

		object.addRod(new SFRRod("Ford"));
		object.addRod(new SFRRod("Zaphod"));
		object.addRod(component);

		object.setRodLocation("Ford", 0, 0);
		object.setRodLocation("Ford", 3, 4);
		object.setRodLocation("Zaphod", 18, 35);
		object.setRodLocation("Zaphod", 15, 36);
		object.setRodLocation("Zaphod", 76, 77);
		object.setRodLocation("Marvin", 13, 13);
		object.setRodLocation("Marvin", 14, 14);
		object.setRodLocation("Marvin", 15, 15);

		component = new SFRRod();
		component.setName("Marvin");
		subComponent = new SFRData("Depressed");
		subComponent.setValue(10);
		component.addData(subComponent, 0);

		equalObject.addRod(new SFRRod("Ford"));
		equalObject.addRod(new SFRRod("Zaphod"));
		equalObject.addRod(component);

		equalObject.setRodLocation("Ford", 0, 0);
		equalObject.setRodLocation("Ford", 3, 4);
		equalObject.setRodLocation("Zaphod", 18, 35);
		equalObject.setRodLocation("Zaphod", 15, 36);
		equalObject.setRodLocation("Zaphod", 76, 77);
		equalObject.setRodLocation("Marvin", 13, 13);
		equalObject.setRodLocation("Marvin", 14, 14);
		equalObject.setRodLocation("Marvin", 15, 15);

		// Set up the unequalObject.
		component = new SFRRod();
		component.setName("Marvin");
		subComponent = new SFRData("Depressed");
		subComponent.setValue(10);
		component.addData(subComponent, 0);

		unequalObject.addRod(new SFRRod("Ford"));
		unequalObject.addRod(new SFRRod("Zaphod"));
		unequalObject.addRod(component);

		unequalObject.setRodLocation("Ford", 0, 0);
		unequalObject.setRodLocation("Ford", 3, 4);
		unequalObject.setRodLocation("Zaphod", 18, 35);
		unequalObject.setRodLocation("Zaphod", 15, 36);
		unequalObject.setRodLocation("Zaphod", 76, 77);
		unequalObject.setRodLocation("Marvin", 13, 13);
		unequalObject.setRodLocation("Marvin", 14, 14);
		unequalObject.setRodLocation("Marvin", 15, 16); // Different!

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
	}

	/**
	 * <p>
	 * Tests the copying and cloning operations.
	 * </p>
	 */
	@Test
	public void checkCopying() {

		int size = 79;

		// Initialize objects for testing.
		ReflectorAssembly object = new ReflectorAssembly(size);
		ReflectorAssembly copy = new ReflectorAssembly(size);
		ReflectorAssembly clone = null;

		// Set up the object.
		SFRRod component = new SFRRod();
		component.setName("Marvin");
		SFRData subComponent = new SFRData("Depressed");
		subComponent.setValue(10);
		component.addData(subComponent, 0);

		object.addRod(new SFRRod("Ford"));
		object.addRod(new SFRRod("Zaphod"));
		object.addRod(component);

		object.setRodLocation("Ford", 0, 0);
		object.setRodLocation("Ford", 3, 4);
		object.setRodLocation("Zaphod", 18, 35);
		object.setRodLocation("Zaphod", 15, 36);
		object.setRodLocation("Zaphod", 76, 77);
		object.setRodLocation("Marvin", 13, 13);
		object.setRodLocation("Marvin", 14, 14);
		object.setRodLocation("Marvin", 15, 15);

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
		clone = (ReflectorAssembly) object.clone();

		// Make sure the references are different but contents the same.
		assertFalse(object == clone);
		assertTrue(object.equals(clone));
		assertFalse(copy == clone);
		assertTrue(copy.equals(clone));

		return;
	}
}
