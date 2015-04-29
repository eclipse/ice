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
package org.eclipse.ice.viz.service.connections.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.eclipse.ice.datastructures.form.BasicEntryContentProvider;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.IEntryContentProvider;
import org.eclipse.ice.viz.service.connections.SecretEntry;
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
		IEntryContentProvider contentProvider = new BasicEntryContentProvider();
		entry = new SecretEntry(contentProvider);
		assertTrue(entry.isSecret());
		assertTrue(entry.isSecret());

		return;
	}

	/**
	 * Checks that the secret flag is properly set when copying.
	 */
	@Test
	public void checkCopy() {
		SecretEntry entry;
		SecretEntry copy;
		Object clone;

		// Create the initial Entry that will be copied and cloned later.
		entry = new SecretEntry();
		entry.setName("Adam Gibson");
		entry.setDescription("There's someone in my house, "
				+ "eating my birthday cake, with my family, and it's not me!");
		// Double-check that its secret flag is set.
		assertTrue(entry.isSecret());

		// Copy it and it should still be equal.
		copy = new SecretEntry();
		copy.copy(entry);
		// Check that the copy's secret flag is set.
		assertTrue(copy.isSecret());
		// They should also be equal (but different references).
		assertNotSame(entry, copy);
		assertEquals(entry, copy);
		assertEquals(copy, entry);

		// Clone it and it should be an equivalent *SecretEntry*.
		clone = entry.clone();
		assertTrue(clone instanceof SecretEntry);
		// Check that the clone's secret flag is set.
		assertTrue(((Entry) clone).isSecret());
		// They should also be equal (but different references).
		assertNotSame(entry, clone);
		assertEquals(entry, clone);
		assertEquals(clone, entry);

		return;
	}
}
