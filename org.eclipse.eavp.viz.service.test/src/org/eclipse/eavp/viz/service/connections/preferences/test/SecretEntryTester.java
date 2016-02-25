/*******************************************************************************
 * Copyright (c) 2015- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jordan Deyton
 *******************************************************************************/
package org.eclipse.eavp.viz.service.connections.preferences.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.eclipse.eavp.viz.service.connections.preferences.SecretEntry;
import org.eclipse.eavp.viz.service.datastructures.BasicVizEntryContentProvider;
import org.eclipse.eavp.viz.service.datastructures.IVizEntryContentProvider;
import org.eclipse.eavp.viz.service.datastructures.VizEntry;
import org.junit.Test;

/**
 * This class tests the {@link SecretEntry} and that it is appropriately defined
 * as a secret {@code Entry} at all times.
 * 
 * @author Jordan Deyton
 *
 */
public class SecretEntryTester {

	/**
	 * Checks that the secret flag is properly set at construction.
	 */
	@Test
	public void checkConstruction() {

		SecretEntry entry;

		// Make sure the secret flag is set for the nullary constructor.
		entry = new SecretEntry();
		assertTrue(entry.isSecret());
		assertTrue(entry.isSecret());

		// Make sure the secret flag is set when the IEntryContentProvider is
		// passed into the constructor.
		IVizEntryContentProvider contentProvider = new BasicVizEntryContentProvider();
		entry = new SecretEntry(contentProvider);
		assertTrue(entry.isSecret());
		assertTrue(entry.isSecret());

		return;
	}

	/**
	 * Checks the equals and hash code methods for an object, an equivalent
	 * object, an unequal object, and invalid arguments.
	 */
	@Test
	public void checkEquals() {

		SecretEntry object;
		Object equalObject;
		Object unequalObject;
		VizEntry superObject;

		// Set up the object under test.
		object = new SecretEntry();
		object.setName("Boba");

		// Set up the equivalent object.
		equalObject = new SecretEntry();
		((SecretEntry) equalObject).setName("Boba");

		// Set up the different object.
		unequalObject = new SecretEntry();
		((SecretEntry) unequalObject).setName("Jango"); // Different!

		// Set up the super object.
		superObject = new VizEntry();
		superObject.copy(object);

		// Check for equivalence (reflective case).
		assertTrue(object.equals(object));
		assertEquals(object.hashCode(), object.hashCode());

		// Check that the object and its equivalent object are, in fact, equal,
		// and that their hash codes match.
		assertNotSame(object, equalObject);
		assertTrue(object.equals(equalObject));
		assertTrue(equalObject.equals(object));
		assertTrue(object.hashCode() == equalObject.hashCode());

		// Check that the object and the different object are not equal and that
		// their hash codes are different.
		assertNotSame(object, unequalObject);
		assertFalse(object.equals(unequalObject));
		assertFalse(unequalObject.equals(object));
		assertFalse(object.hashCode() == unequalObject.hashCode());
		// Verify with the equivalent object as well.
		assertFalse(equalObject.equals(unequalObject));
		assertFalse(unequalObject.equals(equalObject));
		assertFalse(equalObject.hashCode() == unequalObject.hashCode());

		// Test invalid arguments.
		assertFalse(object.equals(null));
		assertFalse(object.equals("Bossk"));

		// Test against a super-class object that is technically equivalent.
		// While the super class may think it is equivalent, the same should not
		// be true in the reverse direction.
		assertNotSame(object, superObject);
		assertTrue(superObject.equals(object));
		assertFalse(object.equals(superObject));
		// Their hash codes should also be different.
		assertFalse(object.hashCode() == superObject.hashCode());

		return;
	}

	/**
	 * Checks that the secret flag is properly set when copying.
	 */
	@Test
	public void checkCopy() {

		SecretEntry object;
		SecretEntry copy;
		Object clone;

		final SecretEntry nullObject = null;

		// Create the initial object that will be copied and cloned later.
		object = new SecretEntry();
		object.setName("Adam Gibson");
		object.setDescription("There's someone in my house, "
				+ "eating my birthday cake, with my family, and it's not me!");
		// Double-check that its secret flag is set.
		assertTrue(object.isSecret());

		// Copying it should yield an equivalent object.
		copy = new SecretEntry(object);
		// After copying, check that they're unique references but equal.
		copy.copy(object);
		assertNotSame(object, copy);
		assertEquals(object, copy);
		assertEquals(copy, object);

		// Cloning it should yield an equivalent object of the correct type.
		clone = object.clone();
		// Check that it's a non-null object of the correct type.
		assertNotNull(clone);
		assertTrue(clone instanceof SecretEntry);
		// Check that they're unique references but equal.
		assertNotSame(object, clone);
		assertEquals(object, clone);
		assertEquals(clone, object);

		// Check invalid arguments to the copy constructor.
		try {
			copy = new SecretEntry(nullObject);
		} catch (NullPointerException e) {
			// Exception thrown as expected. Nothing to do.
		}

		return;
	}
}
