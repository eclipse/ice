/*******************************************************************************
 * Copyright (c) 2014- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings, Jordan Deyton
 *******************************************************************************/
package org.eclipse.ice.viz.service.visit.test;

import static org.junit.Assert.*;

import org.eclipse.ice.viz.service.visit.VisItVizService;
import org.junit.Ignore;
import org.junit.Test;

/**
 * This class is responsible for testing the {@link VisItVizService}.
 * 
 * @author Jay Jay Billings, Jordan Deyton
 * 
 */
public class VisItVizServiceTester {

	// TODO Implement these tests.

	/**
	 * This test checks the name of the visualization service.
	 * 
	 * @see VisItVizService#getName()
	 */
	@Test
	public void checkName() {
		final String expectedName = "VisIt";

		// The name should always be the same. Just try getting it a few times.
		VisItVizService service = new VisItVizService();
		assertEquals(expectedName, service.getName());
		assertEquals(expectedName, service.getName());
		assertEquals(expectedName, new VisItVizService().getName());

		return;
	}

	/**
	 * This test checks the version information for the service.
	 * 
	 * @see VisItVizService#getVersion()
	 */
	@Test
	public void checkVersion() {
		// TODO Update this test. For now, the version should always be the
		// same. However, it may be that we can connect to multiple versions at
		// run-time!
		final String expectedVersion = "1.0";

		// The name should always be the same. Just try getting it a few times.
		VisItVizService service = new VisItVizService();
		assertEquals(expectedVersion, service.getVersion());
		assertEquals(expectedVersion, service.getVersion());
		assertEquals(expectedVersion, new VisItVizService().getVersion());

		return;
	}

	/**
	 * This test checks the service's connection properties, including their
	 * default values.
	 * 
	 * @see VisItVizService#hasConnectionProperties()
	 * @see VisItVizService#getConnectionProperties()
	 * @see VisItVizService#setConnectionProperties(java.util.Map)
	 */
	@Ignore
	@Test
	public void checkConnectionProperties() {
		fail("Not implemented.");
	}

	/**
	 * This test checks that the service connects properly.
	 * 
	 * @see VisItVizService#connect()
	 */
	@Ignore
	@Test
	public void checkConnect() {
		fail("Not implemented.");
	}

	/**
	 * This test checks the plots created by the service.
	 * 
	 * @see VisItVizService#createPlot(java.net.URI)
	 */
	@Ignore
	@Test
	public void checkPlot() {
		fail("Not implemented.");
	}
}
