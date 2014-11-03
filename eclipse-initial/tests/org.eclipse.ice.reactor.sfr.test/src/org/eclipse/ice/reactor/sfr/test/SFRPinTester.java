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
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.TreeSet;

import org.eclipse.ice.reactor.sfr.base.SFRData;
import org.eclipse.ice.reactor.sfr.core.Material;
import org.eclipse.ice.reactor.sfr.core.MaterialBlock;
import org.eclipse.ice.reactor.sfr.core.assembly.Ring;
import org.eclipse.ice.reactor.sfr.core.assembly.SFRPin;
import org.junit.Test;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Tests the operations of the SFRPin class.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author w5q
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class SFRPinTester {

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Tests constructors and default values for SFRPin class.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkConstruction() {
		// begin-user-code

		// A pin to test.
		SFRPin pin;

		// Invalid, default and valid values to use in the constructor.
		String defaultName = "SFR Pin 1";
		String name = "Trillian";

		Ring defaultCladding;
		Ring cladding = new Ring("Hollow tube",
				new Material("Something solid"), 42.0, 15, 16);

		Material defaultFillGas;
		Material fillGas = new Material("Oxygen-based mixture");

		TreeSet<MaterialBlock> invalidMaterialBlocks = new TreeSet<MaterialBlock>();
		TreeSet<MaterialBlock> defaultMaterialBlocks;
		TreeSet<MaterialBlock> materialBlocks = new TreeSet<MaterialBlock>();

		// Other defaults.
		String defaultDescription = "SFR Pin 1's Description";
		int defaultId = 1;

		// Used to test equivalence of MaterialBlock TreeSets.
		TreeSet<MaterialBlock> set1;
		TreeSet<MaterialBlock> set2;
		Iterator<MaterialBlock> iter1;
		Iterator<MaterialBlock> iter2;

		/* ---- Update the default values. ---- */
		// Create a helium material.
		Material helium = new Material("He");
		helium.setDescription("Helium");

		// Create a stainless steel material.
		Material steel = new Material("SS-316");
		steel.setDescription("Stainless Steel");

		// Create a fuel material.
		Material fuel = new Material("UO2");
		fuel.setDescription("Uranium Oxide");

		defaultFillGas = helium;
		defaultCladding = new Ring("Cladding", steel, -1, 16.25, 17.5);

		MaterialBlock block = new MaterialBlock();

		block = new MaterialBlock();
		block.setVertPosition(0);

		// Add the cladding, fill gas, and fuel to the block.
		block.addRing(defaultCladding);
		block.addRing(new Ring("Fill Gas", helium, -1, 13.3333, 16.25));
		block.addRing(new Ring("Fuel", steel, -1, 0, 13.3333));

		materialBlocks.add(block);
		/* ------------------------------------ */

		/* ---- Test SFRPin() ---- */
		// Nullary constructor.
		pin = new SFRPin();

		assertEquals(defaultId, pin.getId());
		assertEquals(defaultName, pin.getName());
		assertEquals(defaultDescription, pin.getDescription());
		assertEquals(defaultFillGas, pin.getFillGas());
		assertEquals(defaultCladding, pin.getCladding());

		// Update the default MaterialBlock TreeSet.
		defaultMaterialBlocks = new TreeSet<MaterialBlock>();
		block = new MaterialBlock();
		block.setVertPosition(0);
		block.addRing(defaultCladding);
		block.addRing(new Ring("Fill Gas", helium, -1, 13.3333, 16.25));
		block.addRing(new Ring("Fuel", fuel, -1, 0, 13.3333));
		defaultMaterialBlocks.add(block);

		// Compare the contents of the MaterialBlock TreeSets.
		set1 = defaultMaterialBlocks;
		set2 = pin.getMaterialBlocks();
		assertEquals(set1.size(), set2.size());
		iter1 = set1.iterator();
		iter2 = set2.iterator();
		while (iter1.hasNext()) {
			assertEquals(iter1.next(), iter2.next());
		}
		/* ----------------------- */

		/* ---- Test SFRPin(String) ---- */

		// Null name String.
		pin = new SFRPin(null);

		assertEquals(defaultId, pin.getId());
		assertEquals(defaultName, pin.getName());
		assertEquals(defaultDescription, pin.getDescription());
		assertEquals(defaultFillGas, pin.getFillGas());
		assertEquals(defaultCladding, pin.getCladding());
		// Compare the contents of the MaterialBlock TreeSets.
		set1 = defaultMaterialBlocks;
		set2 = pin.getMaterialBlocks();
		assertEquals(set1.size(), set2.size());
		iter1 = set1.iterator();
		iter2 = set2.iterator();
		while (iter1.hasNext()) {
			assertEquals(iter1.next(), iter2.next());
		}

		// Empty name String.
		pin = new SFRPin("");

		assertEquals(defaultId, pin.getId());
		assertEquals(defaultName, pin.getName());
		assertEquals(defaultDescription, pin.getDescription());
		assertEquals(defaultFillGas, pin.getFillGas());
		assertEquals(defaultCladding, pin.getCladding());
		// Compare the contents of the MaterialBlock TreeSets.
		set1 = defaultMaterialBlocks;
		set2 = pin.getMaterialBlocks();
		assertEquals(set1.size(), set2.size());
		iter1 = set1.iterator();
		iter2 = set2.iterator();
		while (iter1.hasNext()) {
			assertEquals(iter1.next(), iter2.next());
		}

		// Valid name String.
		pin = new SFRPin(name);

		assertEquals(defaultId, pin.getId());
		assertEquals(name, pin.getName());
		assertEquals(defaultDescription, pin.getDescription());
		assertEquals(defaultFillGas, pin.getFillGas());
		assertEquals(defaultCladding, pin.getCladding());
		// Compare the contents of the MaterialBlock TreeSets.
		set1 = defaultMaterialBlocks;
		set2 = pin.getMaterialBlocks();
		assertEquals(set1.size(), set2.size());
		iter1 = set1.iterator();
		iter2 = set2.iterator();
		while (iter1.hasNext()) {
			assertEquals(iter1.next(), iter2.next());
		}
		/* ----------------------------- */

		/* ---- Test SFRPin(String, Ring, Material, TreeSet<MaterialBlock>) ---- */
		// All parameters invalid.
		pin = new SFRPin(null, null, null, null);

		assertEquals(defaultId, pin.getId());
		assertEquals(defaultName, pin.getName());
		assertEquals(defaultDescription, pin.getDescription());
		assertEquals(defaultFillGas, pin.getFillGas());
		assertEquals(defaultCladding, pin.getCladding());
		// Compare the contents of the MaterialBlock TreeSets.
		set1 = defaultMaterialBlocks;
		set2 = pin.getMaterialBlocks();
		assertEquals(set1.size(), set2.size());
		iter1 = set1.iterator();
		iter2 = set2.iterator();
		while (iter1.hasNext()) {
			assertEquals(iter1.next(), iter2.next());
		}

		// Null name String.
		pin = new SFRPin(null, cladding, fillGas, materialBlocks);

		assertEquals(defaultId, pin.getId());
		assertEquals(defaultName, pin.getName());
		assertEquals(defaultDescription, pin.getDescription());
		assertEquals(fillGas, pin.getFillGas());
		assertEquals(cladding, pin.getCladding());
		// Compare the contents of the MaterialBlock TreeSets.
		set1 = materialBlocks;
		set2 = pin.getMaterialBlocks();
		assertEquals(set1.size(), set2.size());
		iter1 = set1.iterator();
		iter2 = set2.iterator();
		while (iter1.hasNext()) {
			assertEquals(iter1.next(), iter2.next());
		}

		// Empty name String.
		pin = new SFRPin(" ", cladding, fillGas, materialBlocks);

		assertEquals(defaultId, pin.getId());
		assertEquals(defaultName, pin.getName());
		assertEquals(defaultDescription, pin.getDescription());
		assertEquals(fillGas, pin.getFillGas());
		assertEquals(cladding, pin.getCladding());
		// Compare the contents of the MaterialBlock TreeSets.
		set1 = materialBlocks;
		set2 = pin.getMaterialBlocks();
		assertEquals(set1.size(), set2.size());
		iter1 = set1.iterator();
		iter2 = set2.iterator();
		while (iter1.hasNext()) {
			assertEquals(iter1.next(), iter2.next());
		}

		// Null cladding.
		pin = new SFRPin(name, null, fillGas, materialBlocks);

		assertEquals(defaultId, pin.getId());
		assertEquals(name, pin.getName());
		assertEquals(defaultDescription, pin.getDescription());
		assertEquals(fillGas, pin.getFillGas());
		assertEquals(defaultCladding, pin.getCladding());
		// Compare the contents of the MaterialBlock TreeSets.
		set1 = materialBlocks;
		set2 = pin.getMaterialBlocks();
		assertEquals(set1.size(), set2.size());
		iter1 = set1.iterator();
		iter2 = set2.iterator();
		while (iter1.hasNext()) {
			assertEquals(iter1.next(), iter2.next());
		}

		// Null fill gas.
		pin = new SFRPin(name, cladding, null, materialBlocks);

		assertEquals(defaultId, pin.getId());
		assertEquals(name, pin.getName());
		assertEquals(defaultDescription, pin.getDescription());
		assertEquals(defaultFillGas, pin.getFillGas());
		assertEquals(cladding, pin.getCladding());
		// Compare the contents of the MaterialBlock TreeSets.
		set1 = materialBlocks;
		set2 = pin.getMaterialBlocks();
		assertEquals(set1.size(), set2.size());
		iter1 = set1.iterator();
		iter2 = set2.iterator();
		while (iter1.hasNext()) {
			assertEquals(iter1.next(), iter2.next());
		}

		// Null material block TreeSet.
		pin = new SFRPin(name, cladding, fillGas, null);

		assertEquals(defaultId, pin.getId());
		assertEquals(name, pin.getName());
		assertEquals(defaultDescription, pin.getDescription());
		assertEquals(fillGas, pin.getFillGas());
		assertEquals(cladding, pin.getCladding());

		// We need to update the default MaterialBlocks because it will use the
		// fillGas and cladding that we provided.
		defaultMaterialBlocks = new TreeSet<MaterialBlock>();
		block = new MaterialBlock();
		block.setVertPosition(0);
		block.addRing(cladding);
		block.addRing(new Ring("Fill Gas", fillGas, -1, cladding
				.getInnerRadius() - 2.9167, cladding.getInnerRadius()));
		block.addRing(new Ring("Fuel", fuel, -1, 0,
				cladding.getInnerRadius() - 2.9167));
		defaultMaterialBlocks.add(block);

		// Compare the contents of the MaterialBlock TreeSets.
		set1 = defaultMaterialBlocks;
		set2 = pin.getMaterialBlocks();
		assertEquals(set1.size(), set2.size());
		iter1 = set1.iterator();
		iter2 = set2.iterator();
		while (iter1.hasNext()) {
			assertEquals(iter1.next(), iter2.next());
		}

		// Invalid material block TreeSet.
		pin = new SFRPin(name, cladding, fillGas, invalidMaterialBlocks);

		assertEquals(defaultId, pin.getId());
		assertEquals(name, pin.getName());
		assertEquals(defaultDescription, pin.getDescription());
		assertEquals(fillGas, pin.getFillGas());
		assertEquals(cladding, pin.getCladding());
		// Compare the contents of the MaterialBlock TreeSets.
		set1 = defaultMaterialBlocks;
		set2 = pin.getMaterialBlocks();
		assertEquals(set1.size(), set2.size());
		iter1 = set1.iterator();
		iter2 = set2.iterator();
		while (iter1.hasNext()) {
			assertEquals(iter1.next(), iter2.next());
		}

		// All parameters valid.
		pin = new SFRPin(name, cladding, fillGas, materialBlocks);

		assertEquals(defaultId, pin.getId());
		assertEquals(name, pin.getName());
		assertEquals(defaultDescription, pin.getDescription());
		assertEquals(fillGas, pin.getFillGas());
		assertEquals(cladding, pin.getCladding());
		// Compare the contents of the MaterialBlock TreeSets.
		set1 = materialBlocks;
		set2 = pin.getMaterialBlocks();
		assertEquals(set1.size(), set2.size());
		iter1 = set1.iterator();
		iter2 = set2.iterator();
		while (iter1.hasNext()) {
			assertEquals(iter1.next(), iter2.next());
		}
		/* --------------------------------------------------------------- */

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Tests getters and setters for the location, fillGas, cladding and
	 * materialBlocks attributes.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkAttributes() {
		// begin-user-code

		SFRPin pin = new SFRPin();

		// Invalids, defaults, and non-defaults.
		Material defaultFillGas;
		Material fillGas = new Material("Martian atmosphere");
		Ring defaultCladding;
		Ring cladding;

		TreeSet<MaterialBlock> invalidMaterialBlocks = new TreeSet<MaterialBlock>();
		TreeSet<MaterialBlock> defaultMaterialBlocks = new TreeSet<MaterialBlock>();
		TreeSet<MaterialBlock> materialBlocks = new TreeSet<MaterialBlock>();

		// Used to test equivalence of MaterialBlock TreeSets.
		TreeSet<MaterialBlock> set1;
		TreeSet<MaterialBlock> set2;
		Iterator<MaterialBlock> iter1;
		Iterator<MaterialBlock> iter2;

		/* ---- Update the default values. ---- */
		// Create a helium material.
		Material helium = new Material("He");
		helium.setDescription("Helium");

		// Create a stainless steel material.
		Material steel = new Material("SS-316");
		steel.setDescription("Stainless Steel");

		// Create a fuel material.
		Material fuel = new Material("UO2");
		fuel.setDescription("Uranium Oxide");

		defaultFillGas = helium;
		defaultCladding = new Ring("Cladding", steel, -1, 16.25, 17.5);
		cladding = new Ring("Cladding", fuel, -1, 16.25, 17.5);

		MaterialBlock block = new MaterialBlock();
		block.setVertPosition(0);

		// Add the cladding, fill gas, and fuel to the block.
		block.addRing(defaultCladding);
		block.addRing(new Ring("Fill Gas", helium, -1, 13.3333, 16.25));
		block.addRing(new Ring("Fuel", fuel, -1, 0, 13.3333));

		defaultMaterialBlocks.add(block);

		block = new MaterialBlock();
		block.setVertPosition(0);

		// Add the cladding, fill gas, and fuel to the block.
		block.addRing(defaultCladding);
		block.addRing(new Ring("Fill Gas", helium, -1, 13.3333, 16.25));
		block.addRing(new Ring("Fuel", steel, -1, 0, 13.3333));

		materialBlocks.add(block);
		/* ------------------------------------ */

		/* ---- Test fill gas. ---- */
		// Check the default.
		assertEquals(defaultFillGas, pin.getFillGas());

		// Update the value.
		pin.setFillGas(fillGas);

		// Verify the value.
		assertEquals(fillGas, pin.getFillGas());

		// Try to set an invalid value.
		pin.setFillGas(null);

		// Verify the value.
		assertEquals(fillGas, pin.getFillGas());
		/* ------------------------ */

		/* ---- Test cladding. ---- */
		// Check the default.
		assertEquals(defaultCladding, pin.getCladding());

		// Update the value.
		pin.setCladding(cladding);

		// Verify the value.
		assertEquals(cladding, pin.getCladding());

		// Try to set an invalid value.
		pin.setCladding(null);

		// Verify the value.
		assertEquals(cladding, pin.getCladding());
		/* ------------------------ */

		/* ---- Test material blocks. ---- */
		// Check the default.
		// Compare the contents of the MaterialBlock TreeSets.
		set1 = defaultMaterialBlocks;
		set2 = pin.getMaterialBlocks();
		assertEquals(set1.size(), set2.size());
		iter1 = set1.iterator();
		iter2 = set2.iterator();
		while (iter1.hasNext()) {
			assertEquals(iter1.next(), iter2.next());
		}

		// Update the value.
		pin.setMaterialBlocks(materialBlocks);

		// Verify the value.
		// Compare the contents of the MaterialBlock TreeSets.
		set1 = materialBlocks;
		set2 = pin.getMaterialBlocks();
		assertEquals(set1.size(), set2.size());
		iter1 = set1.iterator();
		iter2 = set2.iterator();
		while (iter1.hasNext()) {
			assertEquals(iter1.next(), iter2.next());
		}

		// Try to set an invalid value.
		pin.setMaterialBlocks(null);

		// Verify the value.
		// Compare the contents of the MaterialBlock TreeSets.
		set1 = materialBlocks;
		set2 = pin.getMaterialBlocks();
		assertEquals(set1.size(), set2.size());
		iter1 = set1.iterator();
		iter2 = set2.iterator();
		while (iter1.hasNext()) {
			assertEquals(iter1.next(), iter2.next());
		}

		// Try to set an invalid value.
		pin.setMaterialBlocks(invalidMaterialBlocks);

		// Verify the value.
		// Compare the contents of the MaterialBlock TreeSets.
		set1 = materialBlocks;
		set2 = pin.getMaterialBlocks();
		assertEquals(set1.size(), set2.size());
		iter1 = set1.iterator();
		iter2 = set2.iterator();
		while (iter1.hasNext()) {
			assertEquals(iter1.next(), iter2.next());
		}
		/* ------------------------------- */

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

		// Initialize objects for testing.
		SFRPin object = new SFRPin();
		SFRPin equalObject = new SFRPin();
		SFRPin unequalObject = new SFRPin();

		// Set up the object and equalObject.
		object.setCladding(new Ring("Cladding!!!"));
		object.setFillGas(new Material("Fill gas!!!"));
		for (int i = 0; i < 10; i++) {
			object.addData(new SFRData("Data" + Integer.toString(i)), 0);
		}
		equalObject.setCladding(new Ring("Cladding!!!"));
		equalObject.setFillGas(new Material("Fill gas!!!"));
		for (int i = 0; i < 10; i++) {
			equalObject.addData(new SFRData("Data" + Integer.toString(i)), 0);
		}
		// Set up the unequalObject.
		unequalObject.setCladding(new Ring("Cladding!!!"));
		unequalObject.setFillGas(new Material("He makes your voice squeaky."));
		for (int i = 0; i < 10; i++) {
			unequalObject.addData(new SFRData("Data" + Integer.toString(i)), 0);
		}
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

		// Initialize objects for testing.
		SFRPin object = new SFRPin();
		SFRPin copy = new SFRPin();
		SFRPin clone = null;

		// Set up the object.
		object.setCladding(new Ring("Cladding!!!"));
		object.setFillGas(new Material("Fill gas!!!"));
		for (int i = 0; i < 10; i++) {
			object.addData(new SFRData("Data" + Integer.toString(i)), 0);
		}
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
		clone = (SFRPin) object.clone();

		// Make sure the references are different but contents the same.
		assertFalse(object == clone);
		assertTrue(object.equals(clone));
		assertFalse(copy == clone);
		assertTrue(copy.equals(clone));

		return;
		// end-user-code
	}

}