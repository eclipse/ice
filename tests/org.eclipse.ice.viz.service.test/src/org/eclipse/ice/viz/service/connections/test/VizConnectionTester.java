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

import org.eclipse.ice.viz.service.connections.IVizConnection;
import org.eclipse.ice.viz.service.connections.VizConnection;
import org.junit.Before;
import org.junit.Test;

/**
 * This class is used to test the basic implementation of {@link IVizConnection}
 * provided by {@link VizConnection} as well as its added functionality.
 * 
 * @author Jordan Deyton
 *
 */
public class VizConnectionTester {

	/**
	 * Initializes the viz connection that is tested as well as any other class
	 * variables frequently used to test the connection.
	 */
	@Before
	public void beforeEachTest() {
		// TODO
	}

	/**
	 * Checks all of the default values for {@link IVizConnection} getters.
	 */
	@Test
	public void checkDefaults() {
		fail("Not implemented");
	}

	/**
	 * Checks that listeners can be added and removed, as well as checking that
	 * they are actually notified.
	 */
	@Test
	public void checkListeners() {
		fail("Not implemented");
	}

	/**
	 * Checks that supported properties can be set.
	 */
	@Test
	public void checkSetProperty() {
		fail("Not implemented");
	}

	/**
	 * Checks the name setter properly sets the name property.
	 */
	@Test
	public void checkSetName() {
		fail("Not implemented");
	}

	/**
	 * Checks the description setter properly sets the description property.
	 */
	@Test
	public void checkSetDescription() {
		fail("Not implemented");
	}

	/**
	 * Checks the host setter properly sets the host property.
	 */
	@Test
	public void checkSetHost() {
		fail("Not implemented");
	}

	/**
	 * Checks the port setter properly sets the port property.
	 */
	@Test
	public void checkSetPort() {
		fail("Not implemented");
	}

	/**
	 * Checks the path setter properly sets the path property.
	 */
	@Test
	public void checkSetPath() {
		fail("Not implemented");
	}

	/**
	 * Checks the return value for the connect operation.
	 */
	@Test
	public void checkConnect() {
		fail("Not implemented");
	}

	/**
	 * Checks that the sub-class implementation is called when connecting.
	 */
	@Test
	public void checkConnectToWidget() {
		fail("Not implemented");
	}

	/**
	 * Checks the return value for the disconnect operation.
	 */
	@Test
	public void checkDisconnect() {
		fail("Not implemented");
	}

	/**
	 * Checks that the sub-class implementation is called when disconnecting.
	 */
	@Test
	public void checkDisconnectFromWidget() {
		fail("Not implemented");
	}
}
