/*******************************************************************************
 * Copyright (c) 2016 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.ice.tests.vibe.kvPair;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.datastructures.entry.StringEntry;
import org.eclipse.ice.vibe.kvPair.VibeKVPairRow;
import org.junit.Test;

/**
 * A class to test the functionality of the VibeKVPairRow
 * 
 * @author Robert Smith
 *
 */
public class VibeKVPairRowTester {

	/**
	 * Test that rows can be cloned and copied.
	 */
	@Test
	public void checkClone() {

		// Initialize the test row
		StringEntry testKey = new StringEntry();
		StringEntry testValue = new StringEntry();
		VibeKVPairRow row = new VibeKVPairRow(testKey, testValue);

		// Set some values to the row
		testKey.setValue("key");
		testValue.setValue("value");
		row.setName("row");

		// Initialize another row
		VibeKVPairRow copyRow = new VibeKVPairRow(new StringEntry(),
				new StringEntry());

		// Copy the first row's data into the second
		copyRow.copy(row);

		// Check that the data members were all copied correctly
		assertTrue("key".equals(copyRow.getKey().getValue()));
		assertTrue("value".equals(copyRow.getValue().getValue()));
		assertTrue("row".equals(copyRow.getName()));

		// Clone the row
		VibeKVPairRow clone = (VibeKVPairRow) row.clone();

		// Check that the data members were all copied correctly
		assertTrue("key".equals(clone.getKey().getValue()));
		assertTrue("value".equals(clone.getValue().getValue()));
		assertTrue("row".equals(clone.getName()));
	}

	/**
	 * Check that the getters and setters for the class work correctly.
	 */
	@Test
	public void checkSetters() {

		// Initiailze the test row
		StringEntry testKey = new StringEntry();
		StringEntry testValue = new StringEntry();
		VibeKVPairRow row = new VibeKVPairRow(testKey, testValue);

		// Check that the key and value entries are correct
		assertTrue(testKey == row.getKey());
		assertTrue(testValue == row.getValue());

		// Check the context methods
		row.setContext("context");
		assertTrue("context".equals(row.getContext()));

		// Check the description methods
		row.setDescription("description");
		assertTrue("description".equals(row.getDescription()));

		// Check the ID methods
		row.setId(2);
		assertEquals(2, row.getId());

		// Check the name methods
		row.setName("name");
		assertTrue("name".equals(row.getName()));

	}

	/**
	 * Test the row's ability to give and receive updates
	 */
	@Test
	public void checkUpdate() {

		// Initiailze the test row
		StringEntry testKey = new StringEntry();
		StringEntry testValue = new StringEntry();
		VibeKVPairRow row = new VibeKVPairRow(testKey, testValue);

		// Add a listener
		TestListener listener = new TestListener(row);
		listener.wasUpdated();

		// Update the value and make sure that the listener was notified.
		testValue.setValue("test");
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Exception while waiting for update.");
		}
		assertTrue(listener.wasUpdated());

	}

	/**
	 * A simple class to listen to a VibeKVPairRow and record when it sends an
	 * update.
	 * 
	 * @author Robert Smith
	 *
	 */
	private class TestListener implements IUpdateableListener {

		/**
		 * Whether or not this object has received an update since the last time
		 * it was checked.
		 */
		private boolean updated = false;

		public TestListener(VibeKVPairRow row) {
			row.register(this);
		}

		/**
		 * Checks whether the listener was updated since the last time this
		 * message was called
		 * 
		 * @return True if update() was called since the last time this method
		 *         was invoked. False otherwise.
		 */
		public boolean wasUpdated() {
			boolean temp = updated;
			updated = false;
			return temp;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.ice.datastructures.ICEObject.IUpdateableListener#update(
		 * org.eclipse.ice.datastructures.ICEObject.IUpdateable)
		 */
		@Override
		public void update(IUpdateable component) {
			updated = true;
		}
	}

}
