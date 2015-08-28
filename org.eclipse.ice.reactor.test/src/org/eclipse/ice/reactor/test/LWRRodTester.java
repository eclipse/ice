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
package org.eclipse.ice.reactor.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.TreeSet;

import org.eclipse.ice.reactor.HDF5LWRTagType;
import org.eclipse.ice.reactor.LWRRod;
import org.eclipse.ice.reactor.Material;
import org.eclipse.ice.reactor.MaterialBlock;
import org.eclipse.ice.reactor.MaterialType;
import org.eclipse.ice.reactor.Ring;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * <p>
 * This class is responsible for testing the LWRRod class.
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public class LWRRodTester {
	// An @BeforeClass that sets up the library path. This should be added to
	// the model or removed if it can be fixed for local machine
	@BeforeClass
	public static void beforeClass() {

		// Set the path to the library
		// System.setProperty("java.library.path", "/usr/lib64");
		// System.setProperty("java.library.path", "/home/Scott Forest Hull
		// II/usr/local/lib64");
		// System.setProperty("java.library.path",
		// "/home/ICE/hdf-java/lib/linux");

	}

	/**
	 * <p>
	 * This operation checks the constructors and their default values.
	 * </p>
	 * 
	 */
	@Test
	public void checkConstruction() {
		// Local declarations
		LWRRod testRod;

		// Default Values
		double defaultGasPressure = 2200.0; // psig
		String defaultName = "LWRRod";
		String defaultDesc = "LWRRod's Description";
		int defaultId = 1;
		Material defaultFillGas = new Material("Void", MaterialType.GAS);
		Material defaultCladMaterial = new Material("Zirc", MaterialType.SOLID);
		Ring defaultClad = new Ring("Clad", defaultCladMaterial, 0.0, 0.0);
		HDF5LWRTagType type = HDF5LWRTagType.LWRROD;
		int materialBlockSizeDefault = 0;

		// New Values
		Material newFillGas = new Material("Helium", MaterialType.GAS);
		double newGasPressure = 2200.34344512434;
		String newName = "ControlRod";
		MaterialBlock materialBlock = new MaterialBlock();
		TreeSet<MaterialBlock> materialBlockList = new TreeSet<MaterialBlock>();
		materialBlockList.add(materialBlock);
		// New CLad cannot be set through the constructor for now

		// test Nullary
		testRod = new LWRRod();
		// Check default values
		assertEquals(defaultName, testRod.getName());
		assertEquals(defaultDesc, testRod.getDescription());
		assertEquals(defaultFillGas.getName(), testRod.getFillGas().getName());
		assertEquals(defaultFillGas.getMaterialType(),
				testRod.getFillGas().getMaterialType());
		assertEquals(defaultGasPressure, testRod.getPressure(), 0.0);
		assertEquals(defaultId, testRod.getId());
		assertEquals(materialBlockSizeDefault,
				testRod.getMaterialBlocks().size());
		assertEquals(defaultClad.getName(), testRod.getClad().getName());
		assertEquals(defaultClad.getMaterial().getMaterialType(),
				testRod.getClad().getMaterial().getMaterialType());
		assertEquals(type, testRod.getHDF5LWRTag());

		// test non-nullary constructor -name
		testRod = new LWRRod(newName);
		// Except set name everything else should be Default value
		assertEquals(newName, testRod.getName());
		assertEquals(defaultDesc, testRod.getDescription());
		assertEquals(defaultFillGas, testRod.getFillGas());
		assertEquals(defaultGasPressure, testRod.getPressure(), 0.0);
		assertEquals(defaultId, testRod.getId());
		assertEquals(materialBlockSizeDefault,
				testRod.getMaterialBlocks().size());
		assertEquals(defaultClad.getName(), testRod.getClad().getName());
		assertEquals(defaultClad.getMaterial().getMaterialType(),
				testRod.getClad().getMaterial().getMaterialType());
		assertEquals(type, testRod.getHDF5LWRTag());

		// test non-nullary constructor -name fillgas pressure, list of
		// MaterialBlocks
		testRod = new LWRRod(newName, newFillGas, newGasPressure,
				materialBlockList);
		// Check if the new values are set
		assertEquals(newName, testRod.getName());
		assertEquals(newFillGas, testRod.getFillGas());
		assertEquals(newGasPressure, testRod.getPressure(), 0.0);
		assertTrue(testRod.getMaterialBlocks().equals(materialBlockList));
		assertEquals(type, testRod.getHDF5LWRTag());

		// test non-nullary constructor with null name
		testRod = new LWRRod(null, newFillGas, newGasPressure,
				materialBlockList);
		assertEquals(defaultName, testRod.getName());
		assertEquals(type, testRod.getHDF5LWRTag());
		assertTrue(testRod.getMaterialBlocks().equals(materialBlockList));

		// test non-nullary constructor with null Fill Gas
		testRod = new LWRRod(newName, null, newGasPressure, materialBlockList);
		assertEquals(defaultFillGas, testRod.getFillGas());
		assertEquals(type, testRod.getHDF5LWRTag());
		assertTrue(testRod.getMaterialBlocks().equals(materialBlockList));

		// test non-nullary constructor with bad Pressure
		testRod = new LWRRod(newName, newFillGas, -1.0, materialBlockList);
		assertEquals(defaultGasPressure, testRod.getPressure(), 0.0);
		assertTrue(testRod.getMaterialBlocks().equals(materialBlockList));

		// test non-nullary constructor with null MaterialBlock
		testRod = new LWRRod(null, newFillGas, newGasPressure, null);
		assertEquals(materialBlockSizeDefault,
				testRod.getMaterialBlocks().size());
	}

	/**
	 * <p>
	 * This operation checks the getter and setter for the fill gas.
	 * </p>
	 * 
	 */
	@Test
	public void checkFillGas() {
		LWRRod testRod = new LWRRod();
		Material newFillGas = new Material("Helium", MaterialType.GAS);

		// check setter Legal value
		testRod.setFillGas(newFillGas);
		assertEquals(newFillGas, testRod.getFillGas());

		// check null, it should return previous value
		testRod.setFillGas(null);
		assertEquals(newFillGas, testRod.getFillGas());
	}

	/**
	 * <p>
	 * This operation checks the getter and setter for the pressure.
	 * </p>
	 * 
	 */
	@Test
	public void checkPressure() {
		LWRRod testRod = new LWRRod();
		double newGasPressure = 2200.34344512434;
		// check setter Legal value
		testRod.setPressure(newGasPressure);
		assertEquals(newGasPressure, testRod.getPressure(), 0.0);

		// check setting illegal 0.0 value
		testRod.setPressure(0.0);
		assertEquals(newGasPressure, testRod.getPressure(), 0.0);

		// check setting illegal negative value
		testRod.setPressure(-1.0);
		assertEquals(newGasPressure, testRod.getPressure(), 0.0);
	}

	/**
	 * <p>
	 * This operation checks the getter and setter for the MaterialBlock.
	 * </p>
	 * 
	 */
	@Test
	public void checkMaterialBlock() {
		LWRRod testRod = new LWRRod();
		MaterialBlock materialBlock = new MaterialBlock();
		TreeSet<MaterialBlock> materialBlocks = new TreeSet<MaterialBlock>();
		materialBlocks.add(materialBlock);

		// check if list of MaterialBlocks can be set to non-null;
		testRod.setMaterialBlocks(materialBlocks);
		assertTrue(testRod.getMaterialBlocks().equals(materialBlocks));

		// check setting illegal null value
		testRod.setMaterialBlocks(null);
		assertTrue(testRod.getMaterialBlocks().equals(materialBlocks));

		// Empty check
		testRod.setMaterialBlocks(new TreeSet<MaterialBlock>());
		assertTrue(testRod.getMaterialBlocks().equals(materialBlocks));

	}

	/**
	 * <p>
	 * This operations checks getters and setters for the clad object
	 * </p>
	 * 
	 */
	@Test
	public void checkClad() {

		LWRRod testRod = new LWRRod();
		Material cladMaterial = new Material("Steel304", MaterialType.SOLID);
		Ring newClad = new Ring("Clad", cladMaterial, 0.0, 0.0);

		// check if clad can be a set to a new clad
		testRod.setClad(newClad);
		assertEquals(newClad, testRod.getClad());

		// check setting illegal null value
		testRod.setClad(null);
		assertEquals(newClad, testRod.getClad());

	}

	/**
	 * <p>
	 * Checks the equals and hashCode operations.
	 * </p>
	 * 
	 */
	@Test
	public void checkEquality() {

		// Local Declarations
		LWRRod object, equalObject, unEqualObject, transitiveObject;
		Ring clad;
		MaterialBlock materialBlock;
		Material material;
		Ring ring1, ring2;
		double pressure = 2.0;
		double unEqualPressure = 5.0;
		TreeSet<MaterialBlock> blocks = new TreeSet<MaterialBlock>();

		// Setup Rings
		ring1 = new Ring("Bob", new Material("Billy"), 20.0, 5.0, 10.0);
		ring2 = new Ring("Bob2", new Material("Billy2"), 22.0, 52.0, 102.0);
		clad = new Ring("Bob3", new Material("Billy3"), 0, 52.0, 103.0);

		// Setup material
		material = new Material("MAterialSSSS464");

		// Setup MaterialBlock
		materialBlock = new MaterialBlock();
		materialBlock.addRing(ring1);
		materialBlock.addRing(ring2);
		blocks.add(materialBlock);

		// Setup Clad
		clad = new Ring("Bob3", new Material("Billy3"), 0.0, 53.0, 105.0);

		// Setup root object
		object = new LWRRod("ROD!", material, pressure, blocks);
		object.setClad(clad);

		// Setup equalObject equal to object
		equalObject = new LWRRod("ROD!", material, pressure, blocks);
		equalObject.setClad(clad);

		// Setup transitiveObject equal to object
		transitiveObject = new LWRRod("ROD!", material, pressure, blocks);
		transitiveObject.setClad(clad);

		// Set its data, not equal to object
		unEqualObject = new LWRRod("ROD!", material, unEqualPressure, blocks);
		unEqualObject.setClad(clad);

		// Assert that these two objects are equal
		assertTrue(object.equals(equalObject));

		// Assert that two unequal objects returns false
		assertFalse(object.equals(unEqualObject));

		// Check that equals() is Reflexive
		// x.equals(x) = true
		assertTrue(object.equals(object));

		// Check that equals() is Symmetric
		// x.equals(y) = true iff y.equals(x) = true
		assertTrue(object.equals(equalObject) && equalObject.equals(object));

		// Check that equals() is Transitive
		// x.equals(y) = true, y.equals(z) = true => x.equals(z) = true
		if (object.equals(equalObject)
				&& equalObject.equals(transitiveObject)) {
			assertTrue(object.equals(transitiveObject));
		} else {
			fail();
		}

		// Check the Consistent nature of equals()
		assertTrue(object.equals(equalObject) && object.equals(equalObject)
				&& object.equals(equalObject));
		assertTrue(
				!object.equals(unEqualObject) && !object.equals(unEqualObject)
						&& !object.equals(unEqualObject));

		// Assert checking equality with null value returns false
		assertFalse(object == null);

		// Assert that two equal objects have the same hashcode
		assertTrue(object.equals(equalObject)
				&& object.hashCode() == equalObject.hashCode());

		// Assert that hashcode is consistent
		assertTrue(object.hashCode() == object.hashCode());

		// Assert that hashcodes are different for unequal objects
		assertFalse(object.hashCode() == unEqualObject.hashCode());

	}

	/**
	 * <p>
	 * Checks the copy and clone routines.
	 * </p>
	 * 
	 */
	@Test
	public void checkCopying() {

		// Local Declarations
		LWRRod object, copyObject, clonedObject;
		Ring clad;
		MaterialBlock materialBlock;
		Material material;
		Ring ring1, ring2;
		double pressure = 2.0;
		TreeSet<MaterialBlock> blocks = new TreeSet<MaterialBlock>();

		// Setup Rings
		ring1 = new Ring("Bob", new Material("Billy"), 20.0, 5.0, 10.0);
		ring2 = new Ring("Bob2", new Material("Billy2"), 22.0, 52.0, 102.0);
		clad = new Ring("Bob3", new Material("Billy3"), 0, 52.0, 103.0);

		// Setup material
		material = new Material("MAterialSSSS464");

		// Setup MaterialBlocks
		materialBlock = new MaterialBlock();
		materialBlock.addRing(ring1);
		materialBlock.addRing(ring2);
		blocks.add(materialBlock);

		// Setup Clad
		clad = new Ring("Bob3", new Material("Billy3"), 0.0, 53.0, 105.0);

		// Setup root object
		object = new LWRRod("ROD!", material, pressure, blocks);
		object.setClad(clad);

		// Run the copy routine
		copyObject = new LWRRod();
		copyObject.copy(object);

		// Check contents
		assertTrue(object.equals(copyObject));

		// Run the clone routine
		clonedObject = (LWRRod) object.clone();

		// Check contents
		assertTrue(object.equals(clonedObject));

		// Pass null for the copy routine
		copyObject.copy(null);

		// Show that nothing as changed
		assertTrue(object.equals(copyObject));

	}

	@AfterClass
	public static void afterClass() {

		// Cleans up the datafile if it exists due to a failed test
		File dataFile = new File(System.getProperty("user.dir")
				+ System.getProperty("file.separator") + "test.h5");

		// If it exists, remove it
		if (dataFile.exists()) {
			dataFile.delete();
		}

	}

}