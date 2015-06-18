/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton (UT-Battelle, LLC.) - Initial API and implementation and/or 
 *     initial documentation
 *******************************************************************************/
package org.eclipse.ice.viz.service.connections.test;

import static org.junit.Assert.fail;

import org.eclipse.ice.viz.service.connections.VizConnectionManager;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests the basic functionality provided by
 * {@link VizConnectionManager}.
 * 
 * @author Jordan Deyton
 *
 */
public class VizConnectionManagerTester {

	/**
	 * Initializes the viz connection manager that is tested as well as any
	 * other class variables frequently used to test the connection.
	 */
	@Before
	public void beforeEachTest() {
		// TODO
	}

	/**
	 * Checks the default connections (empty).
	 */
	@Test
	public void checkDefaults() {
		fail("Not implemented");
	}

	/**
	 * Checks that connections can be added successfully by a property change
	 * event.
	 */
	@Test
	public void checkAddConnection() {
		fail("Not implemented");
	}

	/**
	 * Checks that connections can be removed successfully by a property change
	 * event.
	 */
	@Test
	public void checkRemoveConnection() {
		fail("Not implemented");
	}

	/**
	 * Checks that connections can be updated successfully by a property change
	 * event.
	 */
	@Test
	public void checkUpdateConnection() {
		fail("Not implemented");
	}

	/**
	 * Checks that the preference store can be set and loaded.
	 */
	@Test
	public void checkSetPreferenceStore() {
		fail("Not implemented");
	}

	/**
	 * Checks that the proper exceptions are thrown when the preference store is
	 * set with invalid values.
	 */
	@Test
	public void checkSetPreferenceStoreExceptions() {
		fail("Not implemented");
	}
}
