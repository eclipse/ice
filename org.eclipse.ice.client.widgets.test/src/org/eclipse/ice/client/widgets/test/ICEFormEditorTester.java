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
package org.eclipse.ice.client.widgets.test;

import static org.junit.Assert.assertTrue;

import org.eclipse.ice.client.widgets.ICEFormEditor;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>
 * This class is responsible for testing the ICEFormEditor.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class ICEFormEditorTester {
	/**
	 * 
	 */
	private ICEFormEditor ICEFormEditor;

	/**
	 * 
	 */
	private TestListener testListener;

	/**
	 * <p>
	 * This operation is run before the tests to setup the ICEFormEditor.
	 * </p>
	 * 
	 */
	@Before
	public void before() {

		// Allocate the FormWidget
		ICEFormEditor = new ICEFormEditor();

		// Create the TestListener
		testListener = new TestListener();

		// Register the listener
		ICEFormEditor.registerProcessListener(testListener);
		ICEFormEditor.registerUpdateListener(testListener);

	}

	/**
	 * <p>
	 * This operation checks the ICEFormEditor to make sure that it can properly
	 * handle update event notifications.
	 * </p>
	 * 
	 */
	@Test
	public void checkUpdateEvents() {

		// Test updates 21 times
		for (int i = 0; i < 21; i++) {
			// Fire an update
			ICEFormEditor.notifyUpdateListeners();

			// Check for the update
			assertTrue(testListener.wasUpdated());

			// Reset the listener
			testListener.reset();
		}

	}

	/**
	 * <p>
	 * This operation checks the ICEFormEditor to make sure that it can properly
	 * handle update event notifications.
	 * </p>
	 * 
	 */
	@Test
	public void checkProcessEvents() {

		// Test processing 23 times
		for (int i = 0; i < 23; i++) {
			// Fire an update
			ICEFormEditor.notifyProcessListeners("disorganize");

			// Check for the update
			assertTrue(testListener.wasProcessed());

			// Reset the listener
			testListener.reset();
		}

		// FIXME! - How should cancellation be tested?

		return;
	}
}