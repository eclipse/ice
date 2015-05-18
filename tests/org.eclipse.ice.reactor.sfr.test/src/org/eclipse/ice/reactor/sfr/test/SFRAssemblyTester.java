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

import org.eclipse.ice.reactor.sfr.core.AssemblyType;
import org.eclipse.ice.reactor.sfr.core.assembly.SFRAssembly;
import org.junit.Test;

/**
 * <p>
 * Tests the operations of the SFRAssembly class.
 * </p>
 * 
 * @author w5q
 */
public class SFRAssemblyTester {

	/**
	 * <p>
	 * Tests the constructors and default values of the SFRAssembly class.
	 * </p>
	 * 
	 */
	@Test
	public void checkConstruction() {

		// An assembly to test.
		SFRAssembly assembly;

		// Invalid, default, and valid values to use in the constructor.
		int invalidSize = 0;
		;
		int defaultSize = 1;
		int size = 10;

		String nullName = null;
		String emptyName = "   ";
		String defaultName = "SFR Assembly 1";
		String name = "Arthur";

		AssemblyType invalidType = null;
		AssemblyType defaultType = AssemblyType.Fuel;
		AssemblyType type = AssemblyType.Control;

		// Other defaults.
		String defaultDescription = "SFR Assembly 1's Description";
		int defaultId = 1;
		double defaultDuctThickness = 0.0;

		/* ---- Test the basic constructor. ---- */
		// Invalid parameters.
		assembly = new SFRAssembly(invalidSize);

		assertEquals(defaultId, assembly.getId());
		assertEquals(defaultName, assembly.getName());
		assertEquals(defaultDescription, assembly.getDescription());
		assertEquals(defaultSize, assembly.getSize());
		assertEquals(defaultType, assembly.getAssemblyType());
		assertEquals(defaultDuctThickness, assembly.getDuctThickness(), 0);

		// Valid parameters.
		assembly = new SFRAssembly(size);

		assertEquals(defaultId, assembly.getId());
		assertEquals(defaultName, assembly.getName());
		assertEquals(defaultDescription, assembly.getDescription());
		assertEquals(size, assembly.getSize());
		assertEquals(defaultType, assembly.getAssemblyType());
		assertEquals(defaultDuctThickness, assembly.getDuctThickness(), 0);
		/* ------------------------------------- */

		/* ---- Test the second constructor. ---- */
		// Invalid parameters.

		// All invalid.
		assembly = new SFRAssembly(nullName, invalidType, invalidSize);

		assertEquals(defaultId, assembly.getId());
		assertEquals(defaultName, assembly.getName());
		assertEquals(defaultDescription, assembly.getDescription());
		assertEquals(defaultSize, assembly.getSize());
		assertEquals(defaultType, assembly.getAssemblyType());
		assertEquals(defaultDuctThickness, assembly.getDuctThickness(), 0);

		// Name invalid.
		assembly = new SFRAssembly(emptyName, type, size);

		assertEquals(defaultId, assembly.getId());
		assertEquals(defaultName, assembly.getName());
		assertEquals(defaultDescription, assembly.getDescription());
		assertEquals(size, assembly.getSize());
		assertEquals(type, assembly.getAssemblyType());
		assertEquals(defaultDuctThickness, assembly.getDuctThickness(), 0);

		// Type invalid.
		assembly = new SFRAssembly(name, invalidType, size);

		assertEquals(defaultId, assembly.getId());
		assertEquals(name, assembly.getName());
		assertEquals(defaultDescription, assembly.getDescription());
		assertEquals(size, assembly.getSize());
		assertEquals(defaultType, assembly.getAssemblyType());
		assertEquals(defaultDuctThickness, assembly.getDuctThickness(), 0);

		// Size invalid.
		assembly = new SFRAssembly(name, type, invalidSize);

		assertEquals(defaultId, assembly.getId());
		assertEquals(name, assembly.getName());
		assertEquals(defaultDescription, assembly.getDescription());
		assertEquals(defaultSize, assembly.getSize());
		assertEquals(type, assembly.getAssemblyType());
		assertEquals(defaultDuctThickness, assembly.getDuctThickness(), 0);

		// Valid parameters.
		assembly = new SFRAssembly(name, type, size);

		assertEquals(defaultId, assembly.getId());
		assertEquals(name, assembly.getName());
		assertEquals(defaultDescription, assembly.getDescription());
		assertEquals(size, assembly.getSize());
		assertEquals(type, assembly.getAssemblyType());
		assertEquals(defaultDuctThickness, assembly.getDuctThickness(), 0);
		/* -------------------------------------- */

		return;
	}

	/**
	 * <p>
	 * Tests the getter and setter of SFRAssembly's ductThickness attribute.
	 * </p>
	 * 
	 */
	@Test
	public void checkDuctThickness() {

		SFRAssembly assembly = new SFRAssembly(1);
		double defaultDuctThickness = 0.0;

		// Check the default.
		assertEquals(defaultDuctThickness, assembly.getDuctThickness(), 0);

		// Set it to 1 and check it.
		assembly.setDuctThickness(1.0);
		assertEquals(1.0, assembly.getDuctThickness(), 0);

		// Set it to an invalid number.
		assembly.setDuctThickness(-1.0);
		assertEquals(1.0, assembly.getDuctThickness(), 0);

		// Set it to a valid number.
		assembly.setDuctThickness(500);
		assertEquals(500, assembly.getDuctThickness(), 0);

		return;
	}

	/**
	 * <p>
	 * Tests the equality and hashCode operations.
	 * </p>
	 */
	@Test
	public void checkEquality() {

		int size = 18;
		String name = "So long, and thanks for all the fish!";

		// Initialize objects for testing.
		SFRAssembly object = new SFRAssembly(name, AssemblyType.Fuel, size);
		SFRAssembly equalObject = new SFRAssembly(name, AssemblyType.Fuel, size);
		SFRAssembly unequalObject = new SFRAssembly(name, AssemblyType.Control,
				size);

		// Set up the object and equalObject.
		object.setDuctThickness(17);

		equalObject.setDuctThickness(17);

		// Set up the unequalObject.
		// (AssemblyType is different for unequalObject.)
		unequalObject.setDuctThickness(17);

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

		int size = 18;
		String name = "So long, and thanks for all the fish!";

		// Initialize objects for testing.
		SFRAssembly object = new SFRAssembly(name, AssemblyType.Fuel, size);
		SFRAssembly copy = new SFRAssembly(size);
		SFRAssembly clone = null;

		// Set up the object.
		object.setDuctThickness(17);

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
		clone = (SFRAssembly) object.clone();

		// Make sure the references are different but contents the same.
		assertFalse(object == clone);
		assertTrue(object.equals(clone));
		assertFalse(copy == clone);
		assertTrue(copy.equals(clone));

		return;
	}
}